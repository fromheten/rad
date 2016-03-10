(ns rad.buffer-test
  (:require [clojure.test :refer :all]
            [rad.buffer :refer :all]))

(deftest buffer-tests
  (testing "Deleting a char in a line"
    (is (= "ra"
           (delete-char-in-line "rad" 999)))
    (is (= "rd"
           (delete-char-in-line "rad" 1)))
    (is (= "0123"
           (delete-char-in-line "01234" 4))))

(testing "Deleting a char at point"
    (is (= ["Rad is meant" "o be hacked"]
           (delete-char-at-point ["Rad is meant" "to be hacked"] [0 1])))
    (is (= ["ra"]
           (delete-char-at-point ["rad"] [1337 0])))
    (is (= ["rad"]
           (delete-char-at-point ["rad"] [0 1337]))))

  (testing "deleting a char backwards"
    (is (= ["Rd"]
           (delete-char-backwards-from-point ["Rad"] [2 0])))
    (is (= ["Rad"]
           (delete-char-backwards-from-point ["Rad"] [0 0])))
    (is (= ["Rad" "is" "meant" "to" "HACK"]
           (delete-char-backwards-from-point ["Rad" "is" "meant" "to" "HACK"] [0 3]))))

  (testing "Inserting a character at point"
    (is (= ["Rad is meant" "hto be hacked"]
           (insert-char-at-point ["Rad is meant" "to be hacked"] [0 1] "h")))
    (is (= ["Rad" "rocks"]
           (insert-char-at-point ["Rad" "rcks"] [1 1] "o")))
    (is (= ["Ra" "d"]
           (insert-char-at-point ["Ra"] [0 6] "d")))
    (is (=  ["" "" "just some random" "" "stuff" "" "" "" "o" "" ""]
            (insert-char-at-point
             ["" "" "just some random" "" "stuff" "" "" "" "" "" ""]
             [588 8]
             "o"))))

  (testing "inserting a char in a line"
    (is (= "rad"
           (insert-char-in-line "ra" "d" 2)))
    (is (= "Rad Ain't eD"
           (insert-char-in-line "Rad Ain't e" "D" 854936)))
    (is (= "dar"
           (insert-char-in-line "ar" "d" 0)))
    (is (= "dar"
           (insert-char-in-line "ar" "d" -434))))

  (testing "Inserting a newline into a buffer"
    (is (= ["" ""] ;; Buffer with two empty rows
           (insert-new-line-at-line-number [""] 1)))
    (is (= ["Rad is meant"
            ""
            "to be hacked"]
           (insert-new-line-at-line-number ["Rad is meant"
                                            "to be hacked"] 1)))
    (is (= ["Rad" ""]
           (insert-new-line-at-line-number ["Rad"] 500)))))
