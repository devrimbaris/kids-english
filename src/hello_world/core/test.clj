;;dogru cevapsa yeni soru
;;yanlis cevapsa alloptions kullanarak tekrar ayni cardlardla soruyu sorma
;;lein ring server-headless
(ns hello-world.core.handler
  (:require [hello-world.core.utils :as utils]
            [clojure.string :as stri]))


;; (print-question-page (get-samplecards))

;; (find-all-values-in-map-with-key :img-file (get-samplecards))

;; (find-cards-with-id 2 ( get-samplecards))

;; (print-question-page (get-samplecards))

;; (lazy-cat (split-at 3 [1 2 3 4 5 6 7 8 9]))

;; (split-at 8 [1 2 3 4 5 6 7 8 9])

;; (reduce #(lazy-cat %1 %2) [] (split-at 3 [1 2 3 4 5 6 7 8 9]))









