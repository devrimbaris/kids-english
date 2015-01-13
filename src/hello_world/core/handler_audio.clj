(ns hello-world.core.handler-audio
  (:require
   [compojure.core :refer :all]
   [compojure.route :as route]
   [hello-world.core.utils :as utils]
   [hello-world.core.handler-common :as hacommon]
   [hello-world.core.views-audio :as vo]
   [noir.session :as nses]
   [clojure.string :as stri]))


(defn- print-next-question []
  (let [question (utils/get-random-audio-question-and-options (nses/get :questions-asked))]
    (nses/put! :question question)
    (vo/print-audio-question question)))

(defn do-start-audio
  "Selection is a map with keys :missing :f1 :f2 :options"
  []
  (do
    (nses/clear!)
    (nses/put! :c-progress 1)
    (nses/put! :c-rights 0)
    (nses/put! :total-questions 3)
    (nses/put! :questions-asked [])
    (print-next-question)))


(def  audio-routes
  (routes
   (context "/audio" []

            (GET "/start-audio" []
                 (do-start-audio))

            (GET "/check-answer" [answer]
                 (let [{correct-answer :audio-txt} (nses/get :question)]
                   (if (= answer correct-answer)
                     (do
                       (nses/put! :questions-asked (conj (nses/get :questions-asked) answer))
                       (if (< (:c-progress (hacommon/get-progress)) (nses/get :total-questions))
                         (do
                           (hacommon/increase-progress)
                           (print-next-question))
                         (vo/print-report (hacommon/get-progress))))
                     (do
                       (hacommon/record-wrong correct-answer )
                       (vo/print-audio-question (nses/get :question)))))))))
