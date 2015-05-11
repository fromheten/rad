(ns rad.terminal-test
  (:require [clojure.test :refer :all]
            [rad.terminal :refer :all]))

(deftest greets
  (testing "greetings"
    (is (= "Hello Martin"
           (greeet "Martin")))))
