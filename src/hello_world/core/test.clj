;;dogru cevapsa yeni soru
;;yanlis cevapsa alloptions kullanarak tekrar ayni cardlardla soruyu sorma
;;lein ring server-headless
(ns hello-world.core.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [hello-world.core.utils :as utils]
            [clojure.string :as stri]
            [ring.util.response :as resp]
            [ring.middleware.session :as session]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [hiccup.core :refer [html]]))


;; (print-question-page (get-samplecards))

;; (find-all-values-in-map-with-key :img-file (get-samplecards))

;; (find-cards-with-id 2 ( get-samplecards))

;; (print-question-page (get-samplecards))

;; (lazy-cat (split-at 3 [1 2 3 4 5 6 7 8 9]))

;; (split-at 8 [1 2 3 4 5 6 7 8 9])

;; (reduce #(lazy-cat %1 %2) [] (split-at 3 [1 2 3 4 5 6 7 8 9]))

;; (rand-nth (get-samplecards))

;; (stri/split "1-2-3" #"-")


