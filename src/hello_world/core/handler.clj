(ns hello-world.core.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [hello-world.core.utils :as utils]
            [hello-world.core.views :as views]
            [hello-world.core.bulmaca :as bulmaca]
            [hello-world.core.handler-common :as hacommon]
            [hello-world.core.handler-ordered :as ordered]
            [hello-world.core.handler-audio :as audio]
            [clojure.string :as stri]
            [ring.util.response :as resp]
            [ring.middleware.session.memory :refer [memory-store]]
            [ring.middleware.params :as prms]
            [ring.middleware.stacktrace :as stack]
            [noir.session :as nses]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults api-defaults]]
            [hiccup.core :refer [html]]))

;;__ program logic

;; {{:keys [answer :as params]} :form-params   {:keys [correct-answer :as session]}  :session}

;;__ routings
(defn do-print-question [answer-status?]
  (let [cards-list (nses/get :cards-list)]
    (if answer-status?
      (let [[selected-card options] (utils/get-card-and-options cards-list 5 )]
        (nses/put! :correct-answer selected-card)
        (nses/put! :options options)
        (views/html-print-question (nses/get :feedback) (nses/get :c-progress) (nses/get :c-cards)  selected-card options (utils/find-qu-text selected-card) (nses/get :puzzle-data)    ))
      (let [selected-card (nses/get :correct-answer)
            options (nses/get :options)]
        (views/html-print-question (nses/get :feedback) (nses/get :c-progress) (nses/get :c-cards)  selected-card options (utils/find-qu-text selected-card) (nses/get :puzzle-data)  )))))

(defroutes app-routes

  (GET "/" [] ;;TODO burada once session temizlenmeli
       (do
         (nses/clear!)
         (str (views/print-options (utils/ordered-questions-map)))))

  (GET "/start-word-maze" [] ;;TODO burada once session temizlenmeli
       (let [all-cards (utils/get-cards)]
         (nses/clear!)
         (nses/put! :feedback "HAYDİ BAŞLAYALIM") 
         (nses/put! :cards-list all-cards)
         (nses/put! :wrongs-list [])
         (nses/put! :c-cards (count all-cards))
         (nses/put! :c-progress 1)
         (nses/put! :answer-status true)
         (nses/put! :puzzle-data (bulmaca/generate-puzzle-data))
         (resp/redirect "/print-question")))

  (GET "/print-question" []
       (let [answer-status? (nses/get :answer-status)]
         (do-print-question answer-status?)))

  
  (GET "/print-results" []
       (views/do-print-results (hacommon/get-progress)))

  (GET "/check-answer" [answer]
       (if (nil? answer)
         (do (nses/put! :answer-status false)
             (resp/redirect "print-question")) 
         (let [correct-answer (nses/get :correct-answer) ans (Long. answer) ]
           (if (= ans (:card-id correct-answer))
             (do 
               (nses/put! :cards-list (utils/remove-cards-with-id (nses/get :cards-list) ans))
               (nses/put! :feedback "BRAVO")
               (nses/put! :answer-status true)
               (hacommon/increase-progress)
               (nses/put! :puzzle-data (bulmaca/increase-progress (nses/get :puzzle-data)))
               (if (> (count (nses/get :cards-list)) 0)
                 (resp/redirect "/print-question")
                 (resp/redirect "/print-results")))
             (do
               (hacommon/record-wrong correct-answer)
               (nses/put! :feedback "TEKRAR DENE")
               (nses/put! :answer-status false)
               (resp/redirect "/print-question"))))))

  #'ordered/ordered-routes
  #'audio/audio-routes
  
  (route/resources "/")
  (route/not-found "Sayfa bulunamadı."))



(defn enforce-content-type-middleware [hndlr content-type]
  (fn [request]
    (let [response (hndlr request)]
      (assoc response :headers (assoc (:headers response)  "Content-Type" content-type)))))

;;__ application
;;th ordering is important
(def app (-> #'app-routes
             ;;(enforce-content-type-middleware "text/html")
             ;;(wrap-defaults site-defaults)
             (stack/wrap-stacktrace)
             (prms/wrap-params)
             (nses/wrap-noir-session {:store (memory-store nses/mem)})
             ))

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

