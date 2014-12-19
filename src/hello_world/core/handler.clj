;;dogru cevapsa yeni soru
;;yanlis cevapsa alloptions kullanarak tekrar ayni cardlardla soruyu sorma
;;lein ring server-headless
(ns hello-world.core.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [clojure.string :as stri]
            [ring.util.response :as resp]
            [ring.middleware.session :as session]
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
                          {:card-id 9 :category "adj" :word "dry" :img-file "dry.jpg"}])

;;__ utility functions
(defn find-cards-with-id [id cards] (filter #(= id (:card-id %)) cards))

(defn find-all-values-in-map-with-key [keyname cards]
  (reduce #(conj %1 (get %2 keyname))
          []
          cards))

(defn remove-cards-with-id [id-list cards] 
  (reduce #(remove (fn [x] (= (:card-id x) %2)) %1) cards id-list))

(defn get-card-and-options [cards options-count exclude-list]
  (let [remaining-cards (->> cards
                             (remove-cards-with-id exclude-list)
                             (shuffle)
                             (take options-count)) 
        card (rand-nth remaining-cards)]
    [card remaining-cards]))

(get-card-and-options (get-samplecards) 3 [])

;;__ html page generators
(defn print-question-form [cards exclude-list]
  (let [[card all] (get-card-and-options cards 5 [])]
    (html [:p 
           [:img {:src (:img-file card)  :alt (:word card)}] (:word card)]
          [:form {:action "/check-answer" :method "get"}
           (for [x all]
             [:p  [:input 
                   {:type "radio" :name "answer" :value (:card-id x)}
                   (:word x)]])
           [:input {:type "hidden" :name "correct-answer" :value (:card-id card)}]
           [:input {:type "hidden" :name "currentcards" :value (stri/join "-" (find-all-values-in-map-with-key :card-id cards))}]
           [:input {:type "hidden" :name "alloptions" :value (stri/join "-" (find-all-values-in-map-with-key :card-id all))}]
           [:input {:type "submit" :name "submit" :value "submit"}]])))

(defn print-question-page [cards]
  (html [:html
       [:head [:title "Word maze"]]
       [:body
        [:p 
         [:a {:href "/"} "Reload"]]
        (print-question-form cards [])]]))

;;__ program logic


;;__ routings

(defn set-session-var [session]
  (if (:my-var session)
    {:body "Session variable already set"}
    {:body "Nothing in session, setting the var" 
     :session (assoc session :my-var "foo")}))


(defroutes app-routes

  (GET "/" [] (print-question-page (get-samplecards)))
  (GET "/check-answer" [answer correct-answer currentcards alloptions]
       (if (= correct-answer answer)
         (print-question-form (remove-cards-with-id [correct-answer] [] )) 
         "YYYYY") )
  (route/resources "/")
  (route/not-found "Not Found"))


(def app
  (wrap-defaults app-routes site-defaults))

;;__ testing functions

;; (print-question-page (get-samplecards))

;; (find-all-values-in-map-with-key :img-file (get-samplecards))

;; (find-cards-with-id 2 ( get-samplecards))

;; (print-question-page (get-samplecards))

;; (lazy-cat (split-at 3 [1 2 3 4 5 6 7 8 9]))

;; (split-at 8 [1 2 3 4 5 6 7 8 9])

;; (reduce #(lazy-cat %1 %2) [] (split-at 3 [1 2 3 4 5 6 7 8 9]))

;; (rand-nth (get-samplecards))

;; (stri/split "1-2-3" #"-")
















