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


;; cakar bu dikkat et (reduce #(lazy-cat %1 %2) [] (split-at 3 [1 2 3 4 5 6 7 8 9]))

;; (def m [1 2 3 4 5 6 7 ])



;; (defn tuples [s n]
;;   (loop [accu [] coll s c (count m)]
;;     (if (and  (some? (seq coll) ) ( > c 2))
;;       (recur (conj accu (take n coll))  (rest coll) (dec c))
;;       accu)))



;; (defn d [m]
;;   (let [tuples 
;;         (for [x (reduce #(conj %1 (partition %2 1 m)) []  (range 1 8))] (drop-last x))
;;         ]
;;     tuples))


;; (defn comb2 [m]
;;   (loop [accu [] [j k] (split-at 1 m)]
;;     (if (empty? k)
;;       accu
;;       (recur  (reduce conj accu  (for [f j r k] [f r]))
;;               (split-at 1 k)))))

;; (comb2 m)


;; (for [x1 (vector (first m)) x2 (drop 1 m) x3 (drop 2 m)]  [x1 x2 x3])

;; (for [x1 [1 2 3 4 5 6]  x2 [3 4 5] x3 [4 5 6]] [x1 x2 x3])

;; (split-at 1 m )

;; (tuples m 2)

;; (d m)

;; (seq [])




;; (partition 5 1 m)


;; ( print)  (ajskdjalskjdlaj)




