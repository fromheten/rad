(ns rad.buffer-test
  (:require [clojure.test :refer :all]
            [rad.buffer :refer :all]))

(deftest buffer-tests
  (testing "Deleting a char at point"
    (is (= ["Rad is meant" "o be hacked"]
           (delete-char-at-point ["Rad is meant" "to be hacked"] [0 1]))))

  (testing "Inserting a character at point"
    (is (= ["Rad is meant" "hto be hacked"]
           (insert-char-at-point ["Rad is meant" "to be hacked"] [0 1] "h"))))

  (testing "Inserting a newline into a buffer"
    (is (= ["" ""] ;; Buffer with two empty rows
           (insert-new-line-at-line-number [""] 1)))
    (is (= ["Rad is meant"
            ""
            "to be hacked"]
           (insert-new-line-at-line-number ["Rad is meant"
                                            "to be hacked"] 1))))
  (testing "Inserting a new line way below the length of the buffer"
    (is (= ["Rad" ""]
           (insert-new-line-at-line-number ["Rad"] 500)))))
