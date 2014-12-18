(ns hello-world.core.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [hiccup.core :refer [html]]))


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
(def options-count 4)

;;__ html page generators
(defn print-question-page [cards]
  (let [card (get-random-card cards)
        rest-of-cards (take options-count  (remove #(= (:card-id card) (:card-id %)) cards))
        rnd-insert-position (rnd-int options-count)
        

        ]
    (html [:html
           [:head [:title (str  "Question" " " (:card-id card))]]
           [:body
            [:img {:src (:img-file card)}]
            [:p (str (find-all-values-in-map-with-key :word rest-of-cards))]
            
            ]
           

           ])))





(print-question-page (get-samplecards))

;;__ routings
(defroutes app-routes
  (GET "/" [] (print-question-page (get-samplecards)))
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))

;;__ utility functions
(defn find-cards-with-id [id cards] (filter #(= id (:card-id %)) cards))

(defn find-all-values-in-map-with-key [keyname cards]
  (reduce #(conj %1 (get %2 keyname))
          []
          cards ))

(defn get-random-card [cards]
  (let [i (rand-int (count cards))]
    (nth cards i)))


;;__ testing functions
(find-all-values-in-map-with-key :img-file (get-samplecards))

(find-cards-with-id 2 ( get-samplecards))

(let [rc (get-random-card (get-samplecards))]
  (remove #(= (:card-id rc) (:card-id %)) (get-samplecards))
  )

(print-question-page (get-samplecards))

(lazy-cat (split-at 3 [1 2 3 4 5 6 7 8 9]))

(split-at 3 [1 2 3 4 5 6 7 8 9])

(reduce #(lazy-cat %1 %2) [] (split-at 3 [1 2 3 4 5 6 7 8 9]))




















