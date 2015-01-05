(ns hello-world.core.handler-audio
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [hello-world.core.utils :as utils]
            [hello-world.core.handler-common :as hacommon]
            [clojure.string :as stri]
            [ring.util.response :as resp]
            [ring.middleware.session.memory :refer [memory-store]]
            [ring.middleware.params :as prms]
            [ring.middleware.stacktrace :as stack]
            [noir.session :as nses]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults api-defaults]]
            [hiccup.core :refer [html]]))






;; (select-word-to-display
;;  (let [three-choices [from-existing-list-of-audio  a-new-randomly-selected-image-file-and-checking-url hybrid-with-previous]])
;;  (do
;;    (get-current-mp3-list))
;;  )
;; ;; 

;; (get-current-mp3-list)
;; ;;file-name
;; ;;cambridge.txt
;; ;;file structure
;; ;;	word|status|url
;; ;; word and url are self explaining,
;; ;; status is
;; ;; 	WORKING for words with working mp3 links
;; ;; 	DEAD for nonexistent mp3 links
;; ;;
;; ;;check if file exists
;; ;;IF YES
;; ;;	return a vector of maps [{:word xxx :url yyy}]


;; (creation of-options-of_5_pictures_with_one_of_them_as_AUDIO
;;  (let [word (select-word-to-display)]))


(defn get-cambridge-list
  "Loads a vector of maps containing words, urls and statuses for cambridge mp3 urls.
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



(get-cambridge-list)

(clojure.string/split "asdasdas|vfvfvfvf|opopopopop" #"\|")













