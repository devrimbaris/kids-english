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

;;__ html page generators



(defn print-question-form [selected-card options]
  (html5
   [:table [:tr
            [:td [:img {:src (:img-file selected-card) }]]
            [:td {:valign "top"} [:div {:style "padding-top: 5px; padding-left: 35;"} 
                     [:form {:action "/check-answer" :method "GET"}
                      (for [x options]
                        [:p  [:input
                              {:type "radio" :name "answer" :value (:card-id x)}
                              (:word x)]])
                      [:input {:type "submit" :name "submit" :value "submit"}]]]]]]))

(defn print-question [selected-card  options]
  (html5 [:html
          [:head
           [:title "Word maze"]
           [:link {:rel "stylesheet" :href "http://yui.yahooapis.com/pure/0.5.0/pure-min.css"}]]
          [:body

           (print-question-form selected-card options)
           [:p
            [:a {:href "/"} "Restart"]]]]))

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
