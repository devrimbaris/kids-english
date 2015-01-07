(ns hello-world.core.ordered-handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [hello-world.core.utils :as utils]
            [hello-world.core.views-ordered :as vo]
            [hello-world.core.handler-common :as hacommon]
            [clojure.string :as stri]
            [ring.util.response :as resp]
            [ring.middleware.session.memory :refer [memory-store]]
            [ring.middleware.params :as prms]
            [ring.middleware.stacktrace :as stack]
            [noir.session :as nses]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults api-defaults]]
))


(defn- print-next-question [selection]
  (let [question (utils/get-random-ordered-question selection (nses/get :questions-asked))]
    (nses/put! :question question)
    (vo/print-ordered-question question)))

(defn do-start-ordered
  "Selection is a map with keys :missing :f1 :f2 :options"
  [selection]
  (do
    (nses/clear!)
    (nses/put! :c-progress 1)
    (nses/put! :c-rights 0)
    (nses/put! :total-questions 3)
    (nses/put! :selection selection)
    (nses/put! :questions-asked [])
    (print-next-question selection)))


(def  ordered-routes
  (routes
   (context "/ordered" []

            (GET "/start-ordered" [selection]
                 (do-start-ordered selection))

            (GET "/check-answer" [answer]
                 (let [{correct-answer :missing} (nses/get :question)]
                   (if (= answer correct-answer)
                     (do
                       (nses/put! :questions-asked (conj (nses/get :questions-asked) answer))
                       (if (< (:c-progress (hacommon/get-progress)) (nses/get :total-questions))
                         (do
                           (hacommon/increase-progress) 
                           (print-next-question (nses/get :selection)))
                         (vo/print-report (hacommon/get-progress))))
                     (do
                       (hacommon/record-wrong correct-answer )
                       (vo/print-ordered-question (nses/get :question)))))))))



















