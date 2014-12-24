;;dogru cevapsa yeni soru
;;yanlis cevapsa alloptions kullanarak tekrar ayni cardlardla soruyu sorma
;;lein ring server-headless
(ns hello-world.core.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [hello-world.core.utils :as utils]
            [hello-world.core.views :as views]
            [clojure.string :as stri]
            [ring.util.response :as resp]
            [ring.middleware.session :as sess]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults api-defaults]]
            [hiccup.core :refer [html]]))


;;__ program logic


;;__ routings
(defroutes app-routes
  (GET "/" {session :session}
       {:body (views/print-start)
        :session (assoc session :cards-list (utils/get-cards) :exclude-list [])})

  (GET "/print-question" {{:keys [cards-list exclude-list]}  :session}
       {:body (let [[selected-card options] (utils/get-card-and-options cards-list 3 exclude-list)]
                (views/print-question selected-card options))})

  (GET "/check-answer" [answer correct-answer currentcards alloptions]
       (if (= correct-answer answer)
         (views/print-question-form (utils/remove-cards-with-id [correct-answer] [] ) currentcards)
         "YYYYY"))

  (GET "/output" {session :session} {:body (str (:cards-list session)) })

  (route/resources "/")

  (route/not-found "Not Found"))

(defn enforce-content-type-middleware [hndlr content-type]
  (fn [request]
    (let [response (hndlr request)]
      (assoc response :headers (assoc (:headers response)  "Content-Type" content-type)))))

;;__ application
(def app (-> app-routes
             (enforce-content-type-middleware "text/html")
             (wrap-defaults site-defaults)))





(utils/get-card-and-options (utils/get-cards) 3  [] )

;; ;;__ middleware functions
;; (defn deneme-middleware [hndlr]
;;   (fn [request]
;;     (let [response (hndlr request)]
;;       (assoc response :body "iste bu"))))


;; (def app (wrap-defaults (d-middleware app-routes) site-defaults))

;; (def  app (wrap-defaults (sess/wrap-session app-routes)  site-defaults))



;; (defn handler [{session :session}]
;;   (let [count   (:count session 0)
;;         session (assoc session :count (inc count))]
;;     (-> (resp/response (str "You accessed this page " count " times."))
;;         (assoc :session session))))


;; (defn handler [{session :session}]
;;   (resp/response (str "Hello " (:username session))))

;; (def app
;;   (sess/wrap-session handler))

