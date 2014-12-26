(ns hello-world.core.utils-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [hello-world.core.utils :refer :all ]
            [hello-world.core.handler :refer :all]))

(deftest test-convert-to-list
  (testing "convertion to list if not"
    (let [x (convert-to-collection 1 )]
      (is (= '(1)  x))))

  (testing "not-found route"
    (let [response (app (mock/request :get "/invalid"))]
      (is (= (:status response) 404)))))
