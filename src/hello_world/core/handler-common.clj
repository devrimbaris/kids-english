(ns hello-world.core.handler-common
  (:require
   [clojure.string :as stri]
   [noir.session :as nses]))

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
