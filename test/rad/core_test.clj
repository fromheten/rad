(ns rad.core-test
  (:require [clojure.test :refer :all]
            [rad.core :refer :all]))

(def test-buffer (atom [[\r \a \d]
                        [\i \s]
                        [\m \e \a \n \t]
                        [\t \o]
                        [\b \e]
                        [\h \a \c \k \e \d]]))

(deftest insertion-tests
  (testing "Determining whether input is meant to go into buffer, or key-commands"
    (is (=
         true
         (alphanumeric? \a)))

    (is (=
         false
         (alphanumeric? :down)))
    (is (=
         false
         (alphanumeric? "h")))))

(deftest point-tests
  (testing "Moving the point"
    (is (=
         [3 0]
         (move-point-forward @test-buffer [2 0] 1)))
    (is (=
         [3 0]
         (move-point-forward @test-buffer [3 0] 1))))

  (testing "Moving point to the edges"
    (is (=
         [2 4]
         (move-point-forward @test-buffer [2 4] 5))))

  (testing "Moving point backwards"
    (= [1 0]
       (move-point-backwards @test-buffer [3 0] 2))))
