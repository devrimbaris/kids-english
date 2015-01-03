(ns hello-world.core.handler-common
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [hello-world.core.utils :as utils]
            [hello-world.core.views :as views]
            [hello-world.core.ordered-handler :as ordered]
            [clojure.string :as stri]
            [ring.util.response :as resp]
            [ring.middleware.session.memory :refer [memory-store]]
            [ring.middleware.params :as prms]
            [ring.middleware.stacktrace :as stack]
            [noir.session :as nses]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults api-defaults]]
            [hiccup.core :refer [html]]))



;;__ routings
(defn increase-progress []
  (let [current-value (nses/get :c-progress)]
    (nses/put! :c-progress (inc current-value))
    (inc current-value)))



(defn record-wrong [wrong]
  (let [wrongs-list (nses/get :wrongs-list)
        edited (conj wrongs-list wrong)]
    (nses/put! :wrongs-list (distinct edited ))))

(defn get-progress []
  {:c-progress (nses/get :c-progress)
   :total-questions (nses/get :total-questions )
   :c-rights (- (nses/get :total-questions ) (count (nses/get :wrongs-list))) 
   :wrongs-list (nses/get :wrongs-list)
   :c-wrongs (count (nses/get :wrongs-list))})
