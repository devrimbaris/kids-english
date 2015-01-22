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

(defn get-image-size [query-url]
  (with-open [in (io/input-stream query-url)]
    (let [img  (ImageIO/read in)]
      [ (.getWidth img) (.getHeight img)])
    )
  )

(get-image-size "http://upload.wikimedia.org/wikipedia/commons/3/3d/Uranus2.jpg")

(defn slice-image [imgsize_x imgsize_y count_x count_y ]
  (let [
        x-box (quot imgsize_x count_x)
        y-box (quot imgsize_y count_y)
        x-cords (range 0 imgsize_x x-box )
        y-cords (range 0 imgsize_y y-box)
        cartesians
        (for [x x-cords y y-cords] {:x0 x :y0 y :x1 (min imgsize_x (+ x x-box)) :y1 (min imgsize_y (+ y y-box))})
        c-withid (map #(assoc %1 :id %2) cartesians (range))
        clips (for [m c-withid] (dissoc (assoc m
                                          :size-x (- (:x1 m) (:x0 m))
                                          :size-y (- (:y1 m) (:y0 m)) )   :x1 :y1))]
    clips
    ))

(defn get-slice-js [imgUrl]
  (let [[imgX imgY] (get-image-size imgUrl)
        slices (slice-image imgX imgY 10 10)]
    (stri/join (for [clip slices] (str "canvas_context.drawImage(img,"
                                       (:x0 clip) ","
                                       (:y0 clip) ","
                                       (:size-x clip) ","
                                       (:size-y clip) ","
                                       (:x0 clip) ","
                                       (:y0 clip) ","
                                       (:size-x clip) ","
                                       (:size-y clip) 
                                       ");"))))
  )

(defn deneme []
  (let [imgUrl "http://upload.wikimedia.org/wikipedia/commons/3/3d/Uranus2.jpg"]
    (html [:script (str "function drawOnCanvas() {

    var canvas = document.getElementById(\"canvas_1\");""

    if (canvas.getContext) {

        var canvas_context = canvas.getContext(\"2d\");""
        var img = document.getElementById(\"london_eye\");"
        (get-slice-js imgUrl)

        "} }")]
          [:section  {:style "border-style: solid; border-width: 2px; width: 600px;"}
           [:canvas {:width 600 :height 400 :ID "canvas_1"} "Canvas tag not supported"]]
          [:p [:input {:type "Button" :value "Draw" :onClick "drawOnCanvas()"}]]
          [:p [:img {:src imgUrl :ID "london_eye" :width 600 :height 400}]]
          ))


  )



(deneme)



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
