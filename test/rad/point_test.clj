(ns rad.point-test
  (:require [clojure.test :refer :all]
            [rad.point :refer :all]))

(def test-buffer ["rad" "to be hacked" "and that is how we like it"])

(deftest point-tests
  (testing "Moving point in forward"
    (is (= [3 0]
           (move-point-forward [0 0] 5 test-buffer))))
  (testing "Moving point backwards"
    (is (= [4 0]
           (move-point-backwards [5 0] 1)))
    (is (= [0 2]
           (move-point-backwards [4 2] 58))))
  (testing "Moving point down"
    (is (= [0 1]
           (move-point-down [0 0] 1))))

  (testing "Moving point up"
    (is (= [0 1]
           (move-point-up [0 3] 2 test-buffer))))

  (testing "Moving point to the beginning of a line"
    (is (= [0 0]
           (move-point-to-beginning-of-line [100 0])))
    (is (= [0 500]
           (move-point-to-beginning-of-line [500 500]))))
  (testing "whether something is a negative number"
    (is (negative? -5))
    (is (not (negative? 6)))))
