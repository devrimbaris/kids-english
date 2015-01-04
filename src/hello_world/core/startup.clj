;;lein ring server-headless
(ns hello-world.core.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.adapter.jetty :as jty]
            [hello-world.core.views :as views]
            [hello-world.core.utils :as utils]
            [hello-world.core.handler :as hndlr]
            [clojure.string :as stri]
            [ring.util.response :as resp]
            [ring.middleware.session :as sess]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults api-defaults]]))

(defonce server (jty/run-jetty #'hndlr/app {:port 30000 :join? false}))
