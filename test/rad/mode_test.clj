(ns rad.mode-test
  (:require [clojure.test :refer :all]
            [rad.mode :refer :all]))

(deftest mode-tests
  (testing "function that can find the meaning of keys, mapped to values"
    (fn? (get-value-for-key \d))))
