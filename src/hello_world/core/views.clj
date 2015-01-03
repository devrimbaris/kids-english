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


;;usage ;   [:p (embed-audio selected-card)]
(defn embed-audio
  "Map vasitasiyla gecilen card bilgilerine gore bir audio dosyasi linki ekler."
  [{au-file :au-file}]
  (str "<audio controls play autoplay>"
       (html [ :source
              {:src au-file
               :type "audio/mpeg"}])
       "</audio>"))

(defn do-print-results [wrongs-list allcount]
  (let [result-map (utils/get-result-map wrongs-list allcount)
        entries (for [x result-map] x)]
    entries))

;;__ html page generators
(defn print-question-form [selected-card options question-text]
  (html5
   
   [:b  [:div {:class "pure-g"} [:div {:class "pure-u-1"} question-text] ]]
   [:div {:class "pure-g"}
    [:div {:class "pure-u-1-3"}  [:img {:class "pure-img" :src (:img-file selected-card)  }]]
    [:div {:class "pure-u-1-3"}  [:div {:style "padding-top: 5px; padding-left: 35;"} 
                          [:form {:action "/check-answer" :method "GET"}
                           (for [x options]
                             [:p  [:input
                                   {:type "radio" :name "answer" :value (:card-id x)}
                                   (:word x)]])
                           [:input {:type "submit" :name "submit" :class "pure-button pure-button-primary" :value "submit"}]]]]]))

(defn- print-question [selected-card  options question-text]
  (html5 [:html
          [:head
           [:title "Word maze"]
           [:link {:rel "stylesheet" :href "http://yui.yahooapis.com/pure/0.5.0/pure-min.css"}]]
          [:body

           (print-question-form selected-card options question-text)
           ]]))

(defn html-print-question [feedback c-progress c-cards selected-card options question-text]
  (str
   (html [:p  (str  feedback  "     (" c-progress " / " c-cards ")")])
       (print-question selected-card options question-text)))

(defn print-remaining-cards [rem-cards]
  (let [id-list (utils/find-all-values-in-map-with-key :card-id rem-cards)]
    (html [:p (reduce str (interpose "-" id-list))])))

(defn print-options []
  (html5 [:html
          [:head
           [:title "Word maze"]
           [:link {:rel "stylesheet" :href "http://yui.yahooapis.com/pure/0.5.0/pure-min.css"}]]
          [:head [:title "Word maze start"]]
          [:body
           [:p  [:a {:href "/start-word-maze"} "Word Tree Cards"]]
           [:p  [:a {:href "/ordered/start-ordered?selection=Aylar-Türkçe"} "Yılın Ayları"]]
           [:p  [:a {:href "/ordered/start-ordered?selection=Months-English"} "Months of the Year"]]
           [:p  [:a {:href "/ordered/start-ordered?selection=Günler-Türkçe"} "Haftanın Günleri"]]
           [:p  [:a {:href "/ordered/start-ordered?selection=Days-English"} "Days of the Week"]]]]))

