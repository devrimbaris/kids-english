(ns hello-world.core.utils
  (:require    [clojure.java.io :as io]
               [clj-http.client :as client]
               [clojure.pprint :as pp]
               [clojure.string :as stri]))

;;__ utility functions

(defn diff-with-f
  "Diff of two collections based on the condition function.
Condition function should take two arguments. The items in col-1 which satisfy the cond-f are removed iteratively.

Example: 
(def x [ 1 2 3 4 5 6 7 8 9])
(def s [3 4 5])
(diff-with-f x s #(= %1 %2)) ----> (1 2 6 7 8 9)

For equality, also this can be used with the above data;
(remove
 #(some (fn [a] (= a %1)) s)
 x)
"
 [col-1 col-2 cond-f]
 (loop [c1 col-1 c2 col-2]
   (if-let [word (first c2)]
     (recur 
      (remove #(cond-f % word) c1)
      (rest c2))
     c1)))

(defn replace-template [text m] 
      (clojure.string/replace text #"\{\w+\}" 
                              (comp m keyword clojure.string/join butlast rest)))


(defn merge-map-collections-on-key
  "Merges detail info from second map onto first using the key as the idenfier."
  [master detail k]
  
  (for [{word :word} master]
    (let [[ p1] (filter #(= (k %) word) master)
          [ p2] (filter #(= (k %) word) detail)]
      (merge p1 p2)
      ))
  
  )

(defn get-name-wtho-ext [file]
  (let [fullname (.getName file)]
    (first (stri/split fullname #"\."))))


(defn- pad-left [s c length]
  (stri/replace (format
                 (str "%" length "s" )
                 s) " " c))

(defn- pad-right [s p length]
  (stri/replace (format
                 (str "%-" length "s" )
                 s) " " p))


(defn get-mp3-url [word]
  (let [query-url (str "http://dictionary.cambridge.org/dictionary/american-english/" word)
        resp  (client/get query-url {:throw-exceptions false})]
    (if (= 200 (:status resp))
      (let [respbody (:body resp)
            mp3url (second (re-find #"data-src-mp3=\"(.*mp3)" respbody))]
        mp3url)
      nil)))



(defn create-cambridge-url [wordtext]
  "Creates audio sample url from Cambridge, http://dictionary.cambridge.org"
  (let [t1 (str (first wordtext))
        t2 (apply str (take 3 wordtext))
        t3 (apply str (take 5 wordtext))
        t4 (pad-right t3 "_" 5)
        ]
    (str "http://dictionary.cambridge.org/media/british/us_pron/" t1 "/" t2 "/" t4 "/" wordtext ".mp3")))

;;__ database
(defn- load-cards
  "Creates a vector of maps for the given set  directory. Finds list of
  files, removes directory entries, and returns a map for file, then
  assigns :card-id s incrementally."
  [& directories]
  (let [all-cards  
        (flatten  (for [directory directories]
                    (let [allInDir (.listFiles (io/file (str "resources/public/"  directory )))
                          onlyFiles (remove #(.isDirectory %) allInDir)
                          vectorOfMaps (for [file onlyFiles]
                                         {:card-id 1
                                          :category directory
                                          :word (get-name-wtho-ext file)

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

;    
(defn get-cards
  ([] (load-cards "body-parts" "colours" "family" "geometry" "nature" "opposites" "school" "weather" "clothes" "health"  "house" "kitchen" "verbs" "prepositions"))  
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

(defn get-mp3-filerows-data
"Loads all card information from images directory and using this information returns a vector of maps, with each map :word, :status :url.  The url is
  parsed from each word's webpage in Cambridge dictionary."
  []
  (when-let [cards (get-cards)]
    (for [c cards]
      (if-let [url (get-mp3-url (stri/lower-case (:word c)))]
        {:word (stri/lower-case (:word c)) :status "WORKING" :url url}
        {:word (stri/lower-case (:word c)) :status "DEAD" :url nil} ))))

(defn save-mp3-links
  "Saves mp3 links for files in text file later to be consumed by load-current-mp3-list function."
  []
  (spit "resources/public/cambridge_mp3.txt"
        (reduce str (for [{x :word y :status z :url}  (get-mp3-filerows-data)] (str x "|" y "|" z "\n" )))))


(defn load-current-mp3-list
  "Returns a vector of maps containing words, urls and statuses for cambridge mp3 urls by reading and parsing a words list file.
  FILE STRUCTURE:word|status|url
  word and url are self explaining, status is
        WORKING for words with working mp3 links
        DEAD for nonexistent mp3 links."
  []
  (let [filename "resources/public/cambridge_mp3.txt"
        rows (clojure.string/split (slurp filename) #"\n")
        splitted-rows (for [r rows] (clojure.string/split r #"\|"))]
    (reduce
     (fn [m [word status url]]
       (conj m {:word word :status (keyword status) :url url}))
     [] splitted-rows)))

(defn find-count-of-working-audio []
  (count (filter #(= ( :status %) :WORKING)  (load-current-mp3-list)) )
  )



(defn get-random-audio-question-and-options
  "Loads candidate words that has audio links, removes previously asked questions and adds the options,
returns a map."
  [previous-words]
  (let [all-cards (get-cards)
        all-rows (load-current-mp3-list)
        all-merged (merge-map-collections-on-key all-cards all-rows :word)
        cards-with-good-mp3url (filter #(= :WORKING (:status %)) all-merged)
        prev-filtered (diff-with-f cards-with-good-mp3url previous-words #(= (:word %1) %2 ))
        selected-card (rand-nth prev-filtered)
        options (->> all-cards
                     (remove #(= (:word selected-card) (:word %)))
                     (shuffle)
                     (take 4)
                     (cons selected-card)
                     (shuffle)
                     )]
    {:selected-card selected-card :options options}))

(get-random-audio-question-and-options [])

(defn add-mp3-links-to-cards [cards]
  (let [audio-urls (load-current-mp3-list)]
    (reduce
     #(conj %1 (assoc %2 :audio-url (:url  (filter (fn [x] (= x (:word ))) audio-urls))))
     []
     cards)))

(defn find-qu-text [card]
  (let [category (:category card)]
    (case category
      "colours" "What colour is it?"
      "family"  "Family members"
      "geometry" "What shape is it?"
      "weather"  "How is the weather like today?"
      "health" "How is he?"
      "verbs" "What is he doing?"
      "prepositions" "Where is the ball?"
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

(defn ordered-questions-map []
  (let [items (stri/split  (slurp "resources/public/siralamalar.txt" :encoding "UTF-8") #"\r\n")
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


;; ;;////////////////////////////////////
;; ;(selection-questions-map)

;; (format "%tF" (java.util.Date.))

;(ordered-questions-map)

;(ordered-generate-missing-and-options (:items  (last (ordered-questions-map))) 4 )


;; (for [f  (load-cards "body-parts")] (.getName f))

;; (doseq [f  (.listFiles (io/file "resources/public/body-parts"))] (println (stri/lower-case (get-name-wtho-ext f))))






;(map get-mp3-url (map stri/lower-case (map :word (take 3 (get-cards)))))
















