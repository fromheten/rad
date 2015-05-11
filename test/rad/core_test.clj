(ns rad.core-test
  (:require [clojure.test :refer :all]
            [rad.core :refer :all]))

(deftest print-buffer
  (testing "printing a line"
    (is (=
         "cd"
         (line->string (second sample-buffer)))))

  (testing "printing a whole buffer"
    (is (=
         "ab\ncd"
         (buffer->string sample-buffer)))))
