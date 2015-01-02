(ns hello-world.core.ordered-handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [hello-world.core.utils :as utils]
            [hello-world.core.views :as views]
            [clojure.string :as stri]
            [ring.util.response :as resp]
            [ring.middleware.session.memory :refer [memory-store]]
            [ring.middleware.params :as prms]
            [ring.middleware.stacktrace :as stack]
            [noir.session :as nses]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults api-defaults]]
            [hiccup.core :refer [html]]))

;;__ program logic

;;__ routings

(defn do-start-ordered [selection]
  (nses/clear!)

  )



(def  ordered-routes
  (routes
   (context "/ordered" []
            
            (GET "/start-ordered" [selection] (do-start-ordered selection))

            (GET "/" [] "oodddoo")
    
            (GET "/deneme" [id] (str id)))))


