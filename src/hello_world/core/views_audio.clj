(ns hello-world.core.views-audio
  (:require
   [hello-world.core.views :as vo]
   [clojure.string :as stri]
   [hiccup.core :refer [html]]
   [hiccup.page :refer [html5]]))


(def mmm {:selected-card
 {:url "http://dictionary.cambridge.org/media/american-english/us_pron/e/elb/elbow/elbow.mp3",
  :status :WORKING,
  :card-id 7,
  :category "body-parts",
  :word "elbow",
  :au-file "http://dictionary.cambridge.org/media/british/us_pron/e/elb/elbow/elbow.mp3",
  :img-file "body-parts/elbow.gif"},
 :options
 [{:card-id 3,
   :category "body-parts",
   :word "cheek",
   :au-file "http://dictionary.cambridge.org/media/british/us_pron/c/che/cheek/cheek.mp3",
   :img-file "body-parts/cheek.jpg"}
  {:card-id 4,
   :category "body-parts",
   :word "chin",
   :au-file "http://dictionary.cambridge.org/media/british/us_pron/c/chi/chin_/chin.mp3",
   :img-file "body-parts/chin.gif"}
  {:url "http://dictionary.cambridge.org/media/american-english/us_pron/e/elb/elbow/elbow.mp3",
   :status :WORKING,
   :card-id 7,
   :category "body-parts",
   :word "elbow",
   :au-file "http://dictionary.cambridge.org/media/british/us_pron/e/elb/elbow/elbow.mp3",
   :img-file "body-parts/elbow.gif"}
  {:card-id 2,
   :category "body-parts",
   :word "back",
   :au-file "http://dictionary.cambridge.org/media/british/us_pron/b/bac/back_/back.mp3",
   :img-file "body-parts/back.gif"}
  {:card-id 1,
   :category "body-parts",
   :word "arm",
   :au-file "http://dictionary.cambridge.org/media/british/us_pron/a/arm/arm__/arm.mp3",
   :img-file "body-parts/arm.gif"}]})


(defn- with-page-template [f & args]
  (html5 [:head
          [:title "Ordered"]
          [:link {:rel "stylesheet" :href "http://yui.yahooapis.com/pure/0.5.0/pure-min.css"}]]
         [:body (apply f args)]))


(defn- get-question-text [{mp3url :url}  options]
  (html
   [:b  [:div {:class "pure-g"} [:div {:class "pure-u-1"}  "Duydugun kelimeyi işaretler misin?"]]]
   [:div {:class "pure-g"} [:div {:class "pure-u-1"} [:p]]]
   [:div {:class "pure-g"}
    [:div {:class "pure-u-1-3"}
     (vo/embed-audio  {:au-file  mp3url})
     [:div {:style "padding-top: 5px; padding-left: 35;"}
      [:form {:action "/ordered/check-answer" :method "GET"}
       (for [x options]
         [:p  [:input {:type "radio" :name "answer" :value (:word x)} [:img {:src (str "/" (:img-file  x))}]]])
       [:input {:type "submit" :name "submit" :class "pure-button pure-button-primary" :value "submit"}]]]]]))


(defn print-audio-question [{selected-card :selected-card options :options} ]
  (with-page-template get-question-text selected-card options)
  )

(defn- get-report [ & all]
  (apply str all))

(defn print-report [progress]
  (with-page-template get-report progress)
  )

(print-audio-question mmm)
