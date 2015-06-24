(ns rad.buffer-test
  (:require [clojure.test :refer :all]
            [rad.buffer :refer :all]))

(def example-buffer (atom [[{:character :r} {:character :a}]
                           [{:character :d} {:character :!}]]))

(deftest inserting-into-buffers-tests
  (testing "inserting a character at 2nd position in a line"
    (is (=
         (insert-char-in-line
          [{:character :r} {:character :x}]
          ([0 1] 1)
          {:character :a})

         [{:character :r} {:character :a}])))

  (testing "inserting a character at 1x1 in the a buffer"
    (is (=
         (insert-char-in-buffer
          @example-buffer
          [1 1]
          {:character :?})

         [[{:character :r} {:character :a}]
          [{:character :d} {:character :?}]]))))

(deftest print-buffer-tests
  (testing "printing a line"
    (is (=
         "d!"
         (line->string (second @example-buffer)))))

  (testing "printing a whole buffer"
    (is (=
         "ra\nd!"
         (buffer->string @example-buffer)))))
