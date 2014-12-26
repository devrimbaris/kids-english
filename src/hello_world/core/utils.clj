(ns hello-world.core.utils
  (:require [clojure.string :as stri]))

;;__ utility functions
(defn find-cards-with-id [id cards] (filter #(= id (:card-id %)) cards))

(defn find-all-values-in-map-with-key [keyname cards]
  (reduce #(conj %1 (get %2 keyname))
          []
          cards))

(defn convert-to-collection [x] (if (or  (coll? x) (seq? x)) (flatten x) (list x) ))

(defn remove-cards-with-id [cards & ids]
  (let [id-list (convert-to-collection ids)]
    (print ids)
    (reduce #(remove (fn [x] (= (:card-id x) %2)) %1) cards id-list)))

;;TODO option kisimlari tum card listesinden gelmeli
(defn get-card-and-options [cards options-count]
  (if (nil? cards) []
      (let [selected-card (rand-nth cards)
            x (- (count (get-cards)) options-count)
            options (-> (get-cards)
                        (shuffle)
                        (remove-cards-with-id (:card-id selected-card))
                        (nthrest x)
                        (conj selected-card)
                        )
            ]
        [selected-card options])))

;;__ database
(defn get-cards
  ([] [{:card-id 1 :category "body" :word "head" :img-file "head.jpg"}
       {:card-id 2 :category "body" :word "shoulders" :img-file "shoulders.jpg"}
       {:card-id 3 :category "body" :word "knees" :img-file "knees.jpg"}
       {:card-id 4 :category "body" :word "toes" :img-file "toes.jpg"}
       {:card-id 5 :category "colours" :word "red" :img-file "red.jpg"}
       {:card-id 6 :category "colours" :word "green" :img-file "green.jpg"}
       {:card-id 7 :category "colours" :word "blue" :img-file "blue.jpg"}
       {:card-id 8 :category "adj" :word "wet" :img-file "wet.jpg"}
       {:card-id 9 :category "adj" :word "dry" :img-file "dry.jpg"}])
  ([exclude-list] (remove-cards-with-id exclude-list (get-cards))))
