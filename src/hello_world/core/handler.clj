;;lein ring server-headless
(ns hello-world.core.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [clojure.string :as stri]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [hiccup.core :refer [html]]))

;;__ definitions
(defn get-samplecards [] [{:card-id 1 :category "body" :word "head" :img-file "head.jpg"}
                          {:card-id 2 :category "body" :word "shoulders" :img-file "shoulders.jpg"}
                          {:card-id 3 :category "body" :word "knees" :img-file "knees.jpg"}
                          {:card-id 4 :category "body" :word "toes" :img-file "toes.jpg"}
                          {:card-id 5 :category "colours" :word "red" :img-file "red.jpg"}
                          {:card-id 6 :category "colours" :word "green" :img-file "green.jpg"}
                          {:card-id 7 :category "colours" :word "blue" :img-file "blue.jpg"}
                          {:card-id 8 :category "adj" :word "wet" :img-file "wet.jpg"}
                          {:card-id 9 :category "adj" :word "dry" :img-file "dry.jpg"}
                          ])

;;__ utility functions
(defn find-cards-with-id [id cards] (filter #(= id (:card-id %)) cards))

(defn find-all-values-in-map-with-key [keyname cards]
  (reduce #(conj %1 (get %2 keyname))
          []
          cards ))


(defn get-card-and-options [cards options-count]
  (let [all (take options-count  (shuffle cards))
         card (rand-nth all)]
    [card all]))


;;__ html page generators
(defn print-question-page [cards]
  (let [[card all] (get-card-and-options (get-samplecards) 5)]
    (html [:html
           [:head [:title (str  "Question" " " (:word card))]]
           [:body
            [:img {:src (:img-file card)  :alt (:img-file card)}]
            [:form {:action "/check-answer" :method "get"}
             (for [x all]
               [:p  [:input
                     {:type "radio" :name "cevap" :value (str  (:card-id x) "-" (:card-id card))}
                     (:word x)]])
             [:input {:type "submit" :name "submit" :value "submit"}]]]])))

(defn check-answer [ answer]
  (let [[x1 x2]  (stri/split answer #"-")]
    (if  (= x1 x2) "AFERIN" "YURRU")))

;;__ routings
(defroutes app-routes
  (GET "/" [] (print-question-page (get-samplecards)))
  (GET "/check-answer" [cevap] (check-answer cevap))
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))




;;__ testing functions



(print-question-page (get-samplecards))

(find-all-values-in-map-with-key :img-file (get-samplecards))

(find-cards-with-id 2 ( get-samplecards))

(let [rc (get-random-card (get-samplecards))]
  (remove #(= (:card-id rc) (:card-id %)) (get-samplecards))
  )

(print-question-page (get-samplecards))

(lazy-cat (split-at 3 [1 2 3 4 5 6 7 8 9]))

(split-at 8 [1 2 3 4 5 6 7 8 9])

(reduce #(lazy-cat %1 %2) [] (split-at 3 [1 2 3 4 5 6 7 8 9]))

(rand-nth (get-samplecards))

















