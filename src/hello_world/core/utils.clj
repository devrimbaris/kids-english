(ns hello-world.core.utils
  (:require    [clojure.java.io :as io]
               [clojure.pprint :as pp]
               [clojure.string :as stri]))

;;__ utility functions
(defn get-name-wtho-ext [file]
  (let [fullname (.getName file)]
    (first (stri/split fullname #"\."))
    )
  )
(defn- pad-with [s p]
  (stri/replace (format "%-5s" s) " " p)

  )

(defn create-cambridge-url [wordtext]
  "Creates audio sample url from Cambridge, http://dictionary.cambridge.org"
  (let [t1 (str (first wordtext))
        t2 (apply str (take 3 wordtext))
        t3 (apply str (take 5 wordtext))
        t4 (pad-with t3 "_")
        ]
    (str "http://dictionary.cambridge.org/media/british/us_pron/" t1 "/" t2 "/" t4 "/" wordtext ".mp3")))

;;__ database
(defn load-cards
  "Creates a vector of maps for the given set  directory. Finds list of
  files, removes directory entries, and returns a map for file, then
  assigns :card-id s incrementally."
  [& directories]
  (let [ all-cards  
        (flatten  (for [directory directories]
                    (let [allInDir (.listFiles (io/file (str "resources/public/"  directory )))
                          onlyFiles (remove #(.isDirectory %) allInDir)
                          vectorOfMaps (for [file onlyFiles]
                                         {:card-id 1
                                          :category directory
                                          :word (stri/upper-case (get-name-wtho-ext file))
                                          :au-file (create-cambridge-url (get-name-wtho-ext file)  )
                                          :img-file (str directory "/" (.getName file))})]
                      vectorOfMaps)))]
    (map #(assoc %1 :card-id (inc %2)) all-cards (range))))


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


(defn get-cards
  ([] (load-cards "body-parts" "colours"))
  ([exclude-list] (remove-cards-with-id exclude-list (get-cards))))

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


;; (load-cards "body-parts")


;; (for [f  (load-cards "body-parts")] (.getName f))


;; (doseq [f  (.listFiles (io/file "resources/public/body-parts"))] (println (stri/lower-case (get-name-wtho-ext f))))











