(ns rad.core-test
  (:require [clojure.test :refer :all]
            [rad.core :refer :all]))

(deftest point-tests
  (testing "Moving the point 5 steps"
    (is (=
         [5 0]
         (move-point-forward [0 0] 5)))))
