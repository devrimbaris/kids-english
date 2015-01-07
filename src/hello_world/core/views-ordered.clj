(ns hello-world.core.views-ordered
  (:require 
            [clojure.string :as stri]
            [hiccup.core :refer [html]]
            [hiccup.page :refer [html5]]))

(defn- with-page-template [f & args]
  (html5 [:html
          [:head
           [:title "Ordered"]
           [:link {:rel "stylesheet" :href "http://yui.yahooapis.com/pure/0.5.0/pure-min.css"}]]
          [:body (apply f args)]]))


(defn- get-question-text [question]
  (let [{ :keys [missing f1 f2 options]}  question]
    (html5
     [:b  [:div {:class "pure-g"} [:div {:class "pure-u-1"}  "Çizgili yere gelmesi gerekeni bulur musun?"]]]
     [:div {:class "pure-g"} [:div {:class "pure-u-1"} [:p]]]
     [:div {:class "pure-g"}
      [:div {:class "pure-u-1-3"}
       (str f1 "   ________    " f2  )
       [:div {:style "padding-top: 5px; padding-left: 35;"} 
        [:form {:action "/ordered/check-answer" :method "GET"}
         (for [x options]
           [:p  [:input {:type "radio" :name "answer" :value x} x]])
         [:input {:type "submit" :name "submit" :class "pure-button pure-button-primary" :value "submit"}]]]]])))


(defn print-ordered-question [question]
  (with-page-template get-question-text question)
  )

(defn- get-report [ & all]
  (apply str all))

(defn print-report [progress]
  (with-page-template get-report progress)
  )

















