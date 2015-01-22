(ns hello-world.core.bulmaca
  (:require [hello-world.core.utils :as utils]
            [clojure.string :as stri]
            [clj-http.client :as client]
            [clojure.java.io :as io]
            [hiccup.page :refer [html5]]
            [hiccup.core :refer [html]]))


(import 'java.io.File)
(import 'java.io.FileInputStream)
(import 'javax.imageio.ImageIO)


(defn get-mp3-url []
  (let [query-url "http://upload.wikimedia.org/wikipedia/commons/3/3d/Uranus2.jpg"]
    (with-open [in (io/input-stream query-url)]
      (let [img  (ImageIO/read in)]
        [ (.getWidth img) (.getHeight img)])
      )
    ))

(defn slice-image [imgsize_x imgsize_y count_x count_y]
  (let [x-box (quot imgsize_x count_x)
        y-box (quot imgsize_y count_y)
        x-cords (range 0 imgsize_x x-box )
        y-cords (range 0 imgsize_y y-box)
        cartesians (for [x x-cords y y-cords] {:x0 x :y0 y :x1 (+ x x-box) :y1 (+ y y-box)})
        slices (reduce #(conj %1
                              (assoc %2
                                :x1 (min (:x1 %2) imgsize_x)
                                :y1 (min (:y1 %2) imgsize_y)
                                )) [] cartesians)]

    (reduce #(conj %1 ))
    ))

(slice-image 35 32 3 2)


;; (defn deneme []
;;   (html [:script "function drawOnCanvas() {

;;     var canvas = document.getElementById(\"canvas_1\");""

;;     if (canvas.getContext) {

;;         var canvas_context = canvas.getContext(\"2d\");""
;;         var img = document.getElementById(\"london_eye\");""

;;         canvas_context.drawImage(img, 50, 50, 50, 50,  0, 0, 50, 50);
;;         canvas_context.drawImage(img, 200, 0, 180, 300, 200, 20, 150, 300);
;;         canvas_context.drawImage(img, 350, 0, 180, 300, 380, 20, 150, 300);
;;     }
;;     }"]
;;         [:section  {:style "border-style: solid; border-width: 2px; width: 600px;"}
;;          [:canvas {:width 600 :height 400 :ID "canvas_1"} "Canvas tag not supported"]]
;;         [:p [:input {:type "Button" :value "Draw" :onClick "drawOnCanvas()"}]]
;;         [:p [:img {:src "http://www.homeandlearn.co.uk/JS/images/london.jpg" :ID "london_eye"}]]
;;         )


;;   )





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
