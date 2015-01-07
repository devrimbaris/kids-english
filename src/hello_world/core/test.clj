(ns hello-world.core.test
  (:require [hello-world.core.utils :as utils]
            [clojure.string :as stri]
            [clojure.walk :as w]))


;; (print-question-page (get-samplecards))

;; (find-all-values-in-map-with-key :img-file (get-samplecards))

;; (find-cards-with-id 2 ( get-samplecards))

;; (print-question-page (get-samplecards))

;; (lazy-cat (split-at 3 [1 2 3 4 5 6 7 8 9]))

;; (split-at 8 [1 2 3 4 5 6 7 8 9])

;; (reduce #(lazy-cat %1 %2) [] (split-at 3 [1 2 3 4 5 6 7 8 9]))

