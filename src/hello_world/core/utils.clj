(ns hello-world.core.utils
  (:require    [clojure.java.io :as io]
               [clojure.pprint :as pp]
               [clojure.string :as stri]))

;;__ utility functions
(defn get-name-wtho-ext [file]
  (let [fullname (.getName file)]
    (first (stri/split fullname #"\."))))

(defn- pad-with [s p]
  (stri/replace (format "%-5s" s) " " p))

(defn create-cambridge-url [wordtext]
  "Creates audio sample url from Cambridge, http://dictionary.cambridge.org"
  (let [t1 (str (first wordtext))
        t2 (apply str (take 3 wordtext))
        t3 (apply str (take 5 wordtext))
        t4 (pad-with t3 "_")
        ]
    (str "http://dictionary.cambridge.org/media/british/us_pron/" t1 "/" t2 "/" t4 "/" wordtext ".mp3")))

;;__ database
(defn- load-cards
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

;  ([] (load-cards "body-parts" "colours" "family" "geometry" "nature" "opposites" "school" "weather" "clothes" "health" )) 
(defn get-cards
  ([] (load-cards  "family")) 
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

(defn find-qu-text [card]
  (let [category (:category card)]
    (case category
      "colours" "What colour is it?"
      "family"  "Family members"
      "geometry" "What shape is it?"
      "weather"  "How is the weather like today?"
      "health" "How is he?"
      "What is it?")))

(defn ordered-generate-missing-and-options [ordered-list options-count]
  "Sample call:(ordered-generate-missing-and-options [1 2 3 4 5 6 7 8] 5)"
  (let [random-cut (take 3 (drop (rand-int 100) (cycle ordered-list)))
        [f1 _ f2] random-cut
        missing (second random-cut)
        all-remaining-options (clojure.set/difference (set (shuffle  ordered-list)) (set random-cut))
        options (->> all-remaining-options
                     (shuffle)
                     (take (dec options-count))
                     (cons missing)
                     (shuffle)
                     )]
    {:missing  missing :f1  f1 :f2 f2 :options (shuffle options)}))

(defn- ordered-questions-map []
  (let [items (stri/split  (slurp "resources/public/siralamalar.txt") #"\r\n")
        parsed-items (for [i items] (stri/split i #":"))
        maps (reduce #(cons {:category (first %2) :items (vec (rest %2))}  %1) [] parsed-items)]
    maps))

(defn get-random-ordered-question
  ([selection]
   (let [all-ordereds (ordered-questions-map)
         my (first (filter #(= (:category %) selection) all-ordereds))
         ordered-list (:items my)]
     (ordered-generate-missing-and-options ordered-list 4)))
  
  ([selection previous-list]
   (loop [m (get-random-ordered-question selection) ]
     (let [ x (:missing m)]
       (if-not (some #(= x %) previous-list)
         m
         (recur (get-random-ordered-question selection) ))))))


(get-random-ordered-question "Aylar-Türkçe" [ "Mart"]
                             )


;;////////////////////////////////////
;(ordered-questions-map)

;(get-random-ordered-question "Aylar-Türkçe")


;; (format "%tF" (java.util.Date.))

;(ordered-questions-map)

;(ordered-generate-missing-and-options (:items  (last (ordered-questions-map))) 4 )


;; (for [f  (load-cards "body-parts")] (.getName f))

;; (doseq [f  (.listFiles (io/file "resources/public/body-parts"))] (println (stri/lower-case (get-name-wtho-ext f))))














