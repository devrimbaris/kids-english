(ns hello-world.core.bulmaca
  (:require [hello-world.core.utils :as utils]
            [clojure.string :as stri]
            [hiccup.page :refer [html5]]
            [hiccup.core :refer [html]]))





(defn deneme []
  (html [:script "function drawOnCanvas() {

    var canvas = document.getElementById(\"canvas_1\");""

    if (canvas.getContext) {

        var canvas_context = canvas.getContext(\"2d\");""
        var img = document.getElementById(\"london_eye\");""

        canvas_context.drawImage(img, 0, 0, 150, 200, 20, 20, 100, 100);
        canvas_context.drawImage(img, 200, 0, 180, 300, 200, 20, 150, 300);
        canvas_context.drawImage(img, 350, 0, 180, 300, 380, 20, 150, 300);
    }
    }"]
        [:section  {:style "border-style: solid; border-width: 2px; width: 600px;"}
         [:canvas {:width 600 :height 400 :ID "canvas_1"} "Canvas tag not supported"]]
        [:p [:input {:type "Button" :value "Draw" :onClick "drawOnCanvas()"}]]
        [:p [:img {:src "http://www.homeandlearn.co.uk/JS/images/london.jpg" :ID "london_eye"}]]
        )


  )





;; <SECTION style='border-style: solid; border-width: 2px; width: 600px;'>
;;     <CANVAS WIDTH='600' HEIGHT='400' ID='canvas_1'>Canvas tag not supported</CANVAS>
;; </SECTION>
;; <P>
;;     <INPUT TYPE='Button' VALUE='Draw' onClick='drawOnCanvas()'>
;; </P>
;; <P>
;;     <IMG SRC='http://www.homeandlearn.co.uk/JS/images/london.jpg' ID='london_eye'>
;; </P>
;; </P>
