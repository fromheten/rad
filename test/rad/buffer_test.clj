(ns rad.buffer-test
  (:require [clojure.test :refer :all]
            [rad.buffer :refer :all]))

(def example-buffer (atom [[\r \a]
                           [\d \!]]))

(deftest inserting-into-buffers-tests
  (testing "inserting a character at 2nd position in a line"
    (is (=
         (insert-char-in-line
          [\r \x]
          ([0 1] 1)
          \a)

         [\r \a])))

  (testing "inserting Japanese character at [_ 1]"
    (is (=
         (insert-char-in-line
          [\r \a]
          ([0 1] 1)
          \行
          )
         [\r \行]
         )))

  (testing "inserting a character at 1x1 in the a buffer"
    (is (=
         (insert-char-in-buffer
          @example-buffer
          [1 1]
          \?)

         [[\r \a]
          [\d \?]]))))

(deftest print-buffer-tests
  (testing "printing a line"
    (is (=
         "d!"
         (line->string (second @example-buffer)))))

  (testing "printing a whole buffer"
    (is (=
         "ra\nd!"
         (buffer->string @example-buffer)))))
