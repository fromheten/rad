(ns rad.mode-test
  (:require [clojure.test :refer :all]
            [rad.mode :refer :all]))

(deftest mode-tests
  (testing "function that can find the meaning of keys, mapped to values"
    (= (get-value-for-key :d)
       '(rad.buffer/delete-char-in-current-buffer!)))

  (testing "")

  (testing "function that can search and build a list to evaluate, containing a fair share of state"
    (= (do
         (reset! command-build-state '())
         (build-command 3)
         (build-command :d) ;Means delete
         (build-command :l)) ;means forward. hjkl is good for movement, all on home row
       '(rad.buffer/delete-char-in-current-buffer! @point 3))) ; This is the list the function build. Notice how it's very (eval)-able?
  )
