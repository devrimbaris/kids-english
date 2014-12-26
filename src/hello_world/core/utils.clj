(ns hello-world.core.utils
  (:require    [clojure.java.io :as io]
               [clojure.string :as stri]))

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
                        (shuffle)
                        )
            ]
        [selected-card options])))

;;__ database
(defn get-cards
  ([] (load-cards "body-parts"))
  ([exclude-list] (remove-cards-with-id exclude-list (get-cards))))



(defn load-cards
  "Creates a vector of maps for the given directory. FOr the list of files, first check"
  [directory]
  (map #(assoc %1 :card-id (inc %2))
       (remove nil?
               (for [file (.listFiles (io/file (str "resources/public/"  directory ))) ]
                 (if (.isFile file)
                   {:card-id 1 :category directory :word (.getName file) :img-file (str directory "/" (.getName file))}) ))
       (range)))






(load-cards "body-parts")


(for [f  (load-cards "body-parts")] (.getName f))


(for [f  (.listFiles (io/file "resources/public/body-parts"))] (.length f))


;;(remove #(not ( .isDirectory %)) (.listFiles (io/file "resources/public/body-parts")))
