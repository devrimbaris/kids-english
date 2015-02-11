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

(def canvas-x-size 550)
(def canvas-y-size 420)

(def jstemplate "
function drawImage(){
var canvas=document.getElementById('canvas'); 
var ctx=canvas.getContext('2d'); 
img=new Image();   
img.onload=function(){
{slicejs} } 
img.src='{imgURL}';}")

(defn calculate-image-sizing [query-url canvas-x canvas-y]
  (with-open [in (io/input-stream query-url)]
    (let [img  (ImageIO/read in)
          width (.getWidth img)
          height (.getHeight img)
          scale_x (/ canvas-x width)
          scale_y (/ canvas-y height)
          ]
      [width height scale_x scale_y  ])
    )
  )

(defn slice-image [imgsize_x imgsize_y count_x count_y scale_x scale_y]
  (let [x-box (quot imgsize_x count_x)
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
                                  :size-y (- (:y1 m) (:y0 m)) ))
        filtered clips]
    filtered))

(defn get-slice-js
  ([imgURL]
   (let [[imgX imgY scale_x scale_y] (calculate-image-sizing imgURL)
         slices (slice-image imgX imgY 1 1 0.25 0.25)]
     (get-slice-js imgURL slices [])))
  ([imgURL slices shown-slice-ids]
   (let [filtered (remove 
                   #(some (fn [a] (= a (:id %))) shown-slice-ids)
                   slices)
         draw-js (stri/join
                  (for [clip filtered] (str "ctx.drawImage(img,"
                                            (:x0 clip) ","
                                            (:y0 clip) ","
                                            (:size-x clip) ","
                                            (:size-y clip) ","
                                            (:x0-scl clip) ","
                                            (:y0-scl clip) ","
                                            (:size-x-scl clip) ","
                                            (:size-y-scl clip) 
                                            ");\n")))]
     (utils/replace-template jstemplate {:imgURL imgURL :slicejs draw-js }))))

(defn generate-random-imgUrl []
  (let [rows (stri/split
              (:body  (client/get  "https://raw.githubusercontent.com/devrimbaris/hello-world/master/resources/public/bulmaca.txt")) #"\r\n")]
    (rand-nth rows)))


(defn generate-puzzle-data []
  (let [imgUrl (generate-random-imgUrl)
        [imageX imageY scale_x scale_y] (calculate-image-sizing imgUrl canvas-x-size canvas-y-size)
        slices (slice-image imageX imageY 10 10 scale_x scale_y)
        count-slices (count slices)]
    {:imgURL imgUrl
     :clip-count count-slices
     :slices slices
     :shown-ids (range 0 count-slices)
     }))


(defn increase-progress [{imgURL :imgURL  clip-count :clip-count slices :slices shown-ids :shown-ids :as all}]
  (if (> (count shown-ids) 0)
    (let [r (rand-nth shown-ids)
          new-shown-ids (remove #(= r %) shown-ids)
          ]
      {:imgURL imgURL :clip-count clip-count :slices slices :shown-ids new-shown-ids})
    all)
  )


;; (generate-puzzle-data)

;; (deneme (generate-puzzle-data) [])



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


