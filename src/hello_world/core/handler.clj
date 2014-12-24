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

       ;; {{:keys [answer :as params]} :form-params   {:keys [correct-answer :as session]}  :session}

;;__ routings
(defroutes app-routes
  (GET "/" {session :session}
       {:body (views/print-start)
        :session (assoc session :cards-list (utils/get-cards) :exclude-list [] :correct-answer {})})

  (GET "/print-question" {{:keys [cards-list exclude-list :as se]}  :session}
       (let [[selected-card options] (utils/get-card-and-options cards-list 3 exclude-list)]
         {:body  (views/print-question selected-card options)
          :session (assoc se :correct-answer selected-card)}))

  (GET "/check-answer" {session :session :as request}
       {:body  (str ":sayu" ( str  request))})

  (GET "/deneme"  {session :session}
       {:body  (str ":sayu" ( str  session))})

  (route/resources "/")

  (route/not-found "Not Found"))

(defn enforce-content-type-middleware [hndlr content-type]
  (fn [request]
    (let [response (hndlr request)]
      (assoc response :headers (assoc (:headers response)  "Content-Type" content-type)))))

;;__ application
(def app (-> #'app-routes
             (enforce-content-type-middleware "text/html")
             (wrap-defaults site-defaults)))


(def m {:a 1 :b 2 :c 3})

(m :c)


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

