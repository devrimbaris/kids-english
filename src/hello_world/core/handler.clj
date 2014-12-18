(ns hello-world.core.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [hiccup.core :refer [html]]))


(def samplecards [{:card-id 1 :word "head" :img-file "head.jpg"}
                  {:card-id 2 :word "shoulders" :img-file "shoulders.jpg"}
                  {:card-id 3 :word "knees" :img-file "knees.jpg"}
                  {:card-id 4 :word "toes" :img-file "toes.jpg"}])


(defroutes app-routes
  (GET "/" [] (str  "Hello " (:name sampledata)))
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))





(defn print-all-cards [cards]
  (html [:table [:tr [:td "deneme"]]] ))


;;finder functions
(defn find-cards [id cards] (filter #(= id (:card-id %)) cards))

(defn find-all-values-in-map-with-key [keyname cards]
  (reduce #(conj %1 (get %2 keyname))
          []
          cards ))

(defn get-random-card [cards]
  (let [i (rand-int 0 (count cards))]))




;;testing functions
(find-all-values-in-map-with-key :img-file samplecards)

(find-card 2 samplecards)






















