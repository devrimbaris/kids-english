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

(defn slice-image [imgsize_x imgsize_y count_x count_y scale_x scale_y]
  (let [
        x-box (quot imgsize_x count_x)
        y-box (quot imgsize_y count_y)
        x-cords (range 0 imgsize_x x-box )
        y-cords (range 0 imgsize_y y-box)
        cartesians
        (for [x x-cords y y-cords] {:x0 x :y0 y :x1 (min imgsize_x (+ x x-box)) :y1 (min imgsize_y (+ y y-box))})
        scaled (for [m cartesians] (assoc m
                                     :x0-scl (* (:x0 m) scale_x)
                                     :x1-scl (* (:x1 m) scale_x)
                                     :y0-scl (* (:y0 m) scale_y)
                                     :y1-scl (* (:y1 m) scale_y)
                                     )
                    ) 
        c-withid (map #(assoc %1 :id %2) scaled (range))
        clips (for [m c-withid] (assoc m
                                  :size-x (- (:x1 m) (:x0 m))
                                  :size-x-scl (- (:x1-scl m) (:x0-scl m))
                                  :size-y-scl (- (:y1-scl m) (:y0-scl m))
                                  :size-y (- (:y1 m) (:y0 m)) ))]
    clips
    ))

(slice-image 35 32 3 2 0.5 0.5)

(defn get-slice-js [imgUrl]
  (let [[imgX imgY] (get-image-size imgUrl)
        slices (slice-image imgX imgY 10 10 0.1 0.1)]
    (stri/join (for [clip slices] (str "canvas_context.drawImage(img,"
                                       (:x0 clip) ","
                                       (:y0 clip) ","
                                       (:size-x clip) ","
                                       (:size-y clip) ","
                                       (:x0-scl clip) ","
                                       (:y0-scl clip) ","
                                       (:size-x-scl clip) ","
                                       (:size-y-scl clip) 
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
          [:p [:img {:src imgUrl :ID "london_eye" }]]
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
