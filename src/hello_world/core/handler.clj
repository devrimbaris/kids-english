(ns hello-world.core.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [hiccup.core :refer [html]]))


(defn get-samplecards [] [{:card-id 1 :category "body" :word "head" :img-file "head.jpg"}
                          {:card-id 2 :category "body" :word "shoulders" :img-file "shoulders.jpg"}
                          {:card-id 3 :category "body" :word "knees" :img-file "knees.jpg"}
                          {:card-id 4 :category "body" :word "toes" :img-file "toes.jpg"}])


(defroutes app-routes
  (GET "/" [] (print-question-page (get-samplecards)))
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))



;;html page generators

(defn print-question-page [cards]
  (html [:table [:tr [:td "card-id"] [:td "1"]]] ))





;;utility functions
(defn find-cards [id cards] (filter #(= id (:card-id %)) cards))

(defn find-all-values-in-map-with-key [keyname cards]
  (reduce #(conj %1 (get %2 keyname))
          []
          cards ))

(defn get-random-card [cards]
  (let [i (rand-int (count cards))]
    (nth cards i)))




;;testing functions
(find-all-values-in-map-with-key :img-file samplecards)

(find-card 2 ( get-samplecards))






















