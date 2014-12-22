;;dogru cevapsa yeni soru
;;yanlis cevapsa alloptions kullanarak tekrar ayni cardlardla soruyu sorma
;;lein ring server-headless
(ns hello-world.core.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [hello-world.core.utils :as utils]
            [clojure.string :as stri]
            [ring.util.response :as resp]
            [ring.middleware.session :as session]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [hiccup.core :refer [html]]))


;;__ html page generators
(defn print-question-form [cards exclude-list]
  (let [[card all] (utils/get-card-and-options cards 5 [])]
    (html [:p
           [:img {:src (:img-file card)  :alt (:word card)}] (:word card)]
          [:form {:action "/check-answer" :method "get"}
           (for [x all]
             [:p  [:input
                   {:type "radio" :name "answer" :value (:card-id x)}
                   (:word x)]])
           [:input {:type "hidden" :name "correct-answer" :value (:card-id card)}]
           [:input {:type "hidden" :name "currentcards" :value (stri/join "-" (utils/find-all-values-in-map-with-key :card-id cards))}]
           [:input {:type "hidden" :name "alloptions" :value (stri/join "-" (utils/find-all-values-in-map-with-key :card-id all))}]
           [:input {:type "submit" :name "submit" :value "submit"}]])))



(defn print-question-page [cards]
  (html [:html
       [:head [:title "Word maze"]]
       [:body
        [:p
         [:a {:href "/"} "Reload"]]
        (print-question-form cards [])]]))

;;__ program logic


;;__ routings

(defn set-session-var [session]
  (if (:my-var session)
    {:body "Session variable already set"}
    {:body "Nothing in session, setting the var"
     :session (assoc session :my-var "foo")}))


(defroutes app-routes

  (GET "/" [] (print-question-page (utils/get-cards)))
  (GET "/check-answer" [answer correct-answer currentcards alloptions]
       (if (= correct-answer answer)
         (print-question-form (utils/remove-cards-with-id [correct-answer] [] ))
         "YYYYY"))
  (route/resources "/")
  (route/not-found "Not Found"))

(defn deneme-middleware [hndlr]
  (fn [request]
    (let [response (hndlr request)]
      (assoc response :ahaha "iste bu")))

  
  )

(def app
  (wrap-defaults app-routes site-defaults deneme-middleware))










