(ns hello-world.core.handler-audio
  (:require
   [hello-world.core.utils :as utils]
   [clojure.string :as stri]))

(defn load-current-mp3-list
  "Returns a vector of maps containing words, urls and statuses for cambridge mp3 urls by parsing a words list file.
  FILE STRUCTURE:word|status|url
  word and url are self explaining, status is
        WORKING for words with working mp3 links
        DEAD for nonexistent mp3 links."
  []
  (let [filename "resources/public/cambridge_mp3.txt"
        rows (clojure.string/split (slurp filename) #"\r\n")
        splitted-rows (for [r rows] (clojure.string/split r #"\|"))]
    (reduce
     (fn [m [word status url]]
       (conj m {:word word :status status :url url}))
     [] splitted-rows)))

(defn get-mp3-filerows-data
"Loads all card information from images directory and using this information returns a vector of maps, with each map :word, :status :url.  The url is
  parsed from each word's webpage in Cambridge dictionary."
  []
  (when-let [cards  (take 3 (utils/get-cards))]
    (for [c cards]
      (if-let [url (utils/get-mp3-url (stri/lower-case (:word c)))]
        {:word (stri/lower-case (:word c)) :status :WORKING :url url}
        {:word (stri/lower-case (:word c)) :status :DEAD :url nil} ))))

(defn save-mp3-links []
  (spit "resources/public/cambridge_mp3.txt"
        (reduce str (for [{x :word y :status z :url}  (get-mp3-filerows-data)] (str x "|" y "|" z "\n" )))))












