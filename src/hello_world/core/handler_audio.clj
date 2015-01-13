(ns hello-world.core.handler-audio
  (:require
   [compojure.core :refer :all]
   [compojure.route :as route]
   [hello-world.core.utils :as utils]
   [hello-world.core.handler-common :as hacommon]
   [ring.util.response :as resp]
   [hello-world.core.views-audio :as vo]
   [noir.session :as nses]
   [clojure.string :as stri]))


(defn do-start-audio
  "Selection is a map with keys :missing :f1 :f2 :options"
  []
  (do
    (nses/clear!)
    (nses/put! :c-progress 1)
    (nses/put! :c-rights 0)
    (nses/put! :total-questions (utils/find-count-of-working-audio))
    (nses/put! :questions-asked [])
    (resp/redirect "/audio/ask-audio-question")))


(def  audio-routes
  (routes
   (context "/audio" []

            (GET "/start-audio" []
                 (do-start-audio))

            (GET "/ask-audio-question" []
                 (if-let [question (nses/get :question)]
                   (vo/print-audio-question question)
                   (let [newQuestion (utils/get-random-audio-question-and-options (nses/get :questions-asked))]
                     (nses/put! :question newQuestion)
                     (vo/print-audio-question newQuestion))))

            (GET "/check-answer" [answer]
                 (let [{{correct-answer :word}  :selected-card} (nses/get :question)]
                   (if (= answer correct-answer)
                     (do
                       (nses/put! :questions-asked (conj (nses/get :questions-asked) answer))
                       (if (< (:c-progress (hacommon/get-progress)) (nses/get :total-questions))
                         (do
                           (hacommon/increase-progress)
                           (nses/put! :question nil)
                           (resp/redirect "/audio/ask-audio-question"))
                         (vo/print-report (hacommon/get-progress))))
                     (do
                       (hacommon/record-wrong correct-answer)
                       (resp/redirect "/audio/ask-audio-question"))))))))





