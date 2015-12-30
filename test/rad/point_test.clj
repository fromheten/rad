(ns rad.point-test
  (:require [clojure.test :refer :all]
            [rad.point :refer :all]))

;; I should reverse the order of the arguments to these functions.
;; Because they deal with single items, point should be the first arg

(deftest point-tests
  (testing "Moving point in forward"
    (is (= [5 0]
           (move-point-forward [0 0] 5))))

  (testing "Moving point backwards"
    (is (= [4 0]
           (move-point-backwards [5 0] 1)))
    (is (= [0 2]
           (move-point-backwards [4 2] 58))))

  (testing "Moving point down in a buffer"
    (is (= [0 1]
           (move-point-down [0 0] 1))))

  (testing "whether something is a negative number"
    (is (negative? -5))
    (is (not (negative? 6)))))
