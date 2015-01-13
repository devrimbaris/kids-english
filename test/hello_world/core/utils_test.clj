(ns hello-world.core.utils-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [hello-world.core.utils :as hw-utils]
            [hello-world.core.handler :as hw-handler]))

(deftest test-convert-to-list
  (testing "convertion to list if not"
    (let [x (hw-utils/convert-to-collection 1 )]
      (is (= '(1)  x))))

  (testing "not-found route"
    (let [response (hw-handler/app (mock/request :get "/invalid"))]
      (is (= (:status response) 404)))))

(deftest test-ordered-question-generators
  (testing "generate missing and options"
    (let [x 0 y 0]
      (is (= x y))
      ))
  )


