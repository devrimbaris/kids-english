;;dogru cevapsa yeni soru
;;yanlis cevapsa alloptions kullanarak tekrar ayni cardlardla soruyu sorma
;;lein ring server-headless
(ns hello-world.core.views
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [hello-world.core.utils :as utils]
            [clojure.string :as stri]
            [ring.util.response :as resp]
            [ring.middleware.session :as sess]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults api-defaults]]
            [hiccup.page :refer [html5]]
            [hiccup.core :refer [html]]))


;;__ html page generators
(defn print-question-form [selected-card options]
  (html5    [:img {:src (:img-file selected-card) }] 
            [:form {:action "/check-answer" :method "GET"}
             (for [x options]
               [:p  [:input
                     {:type "radio" :name "answer" :value (:card-id x)}
                     (:word x)]])
             [:input {:type "submit" :name "submit" :value "submit"}]]))


(defn print-question [selected-card  options]
  (html5 [:html
         [:head [:title "Word maze"]]
         [:body
          [:p
           [:a {:href "/"} "Reload"]]
          (print-question-form selected-card options)]]))


(defn print-remaining-cards [rem-cards]
  (let [id-list (utils/find-all-values-in-map-with-key :card-id rem-cards)]
    (html [:p (reduce str (interpose "-" id-list))])))

(defn print-start []
  (html5 [:html
          [:head [:title "Word maze start"]]
          [:body
           [:p
            [:a {:href "/print-question"} "Start"]]
           ]]))









