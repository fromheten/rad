(ns rad.buffer-test
  (:require [clojure.test :refer :all]
            [rad.buffer :refer :all]))

(def example-buffer (atom [[\r \a]
                           [\d \!]]))
(def long-example-buffer (atom [[\r \a \d]
                                [\i \s]
                                [\m \e \a \n \t]
                                [\t \o]
                                [\b \e]
                                [\h \a \c \k \e \d]
                                ]))

(deftest inserting-into-buffers-tests
  (testing "inserting a character at 2nd position in a line"
    (is (=
         (insert-char-in-line
          [\r \x]
          ([0 1] 1)
          \a)

         [\r \a])))

  (testing "inserting Henji character at [_ 1]"
    (is (=
         (insert-char-in-line
          [\r \a]
          ([0 1] 1)
          \行
          )
         [\r \行])))

  (testing "inserting a character at 1x1 in the a buffer"
    (is (=
         (insert-char-in-buffer
          @example-buffer
          [1 1]
          \?)

         [[\r \a]
          [\d \?]])))

  (testing "inserting a character at the end of a line in a buffer"
    (is (=
         (insert-char-in-buffer @example-buffer [2 1] \?)

         [[\r \a]
          [\d \! \?]]
         )))
  (testing "inserting newlines"
    (is (=
         (insert-char-in-buffer @example-buffer [1 0] \newline)

         [[\r]
          [\a]
          [\d \!]]
         ))
    (is (=
         (insert-char-in-buffer @example-buffer [1 1] \newline)

         [[\r \a] [\d] [\!]]
         ))
    (is (=
         (insert-char-in-buffer @example-buffer [0 0] \newline)

         [[] [\r \a] [\d \!]]))
    (is (=
         (insert-char-in-buffer @example-buffer [2 1] \newline)
         [[\r \a] [\d \!] []]))))

(deftest deleting-chars-in-buffers
  (testing "deleting a char from a line"
    (is (=
         (delete-char-in-line [\r \a \d] 1)
         [\r \d])))
  (testing "deleting a char from a buffer"
    (is (=
         [[\r \a] [\!]]
         (delete-char-in-buffer @example-buffer [0 1]))))
  (testing "deleting a char backwards (as with the backspace key)"
    (is (=
         [[\r \a] [\!]]
         (delete-char-backwards @example-buffer [1 1])))))
()
(deftest print-buffer-tests
  (testing "printing a line"
    (is (=
         "d!"
         (line->string (second @example-buffer)))))

  (testing "printing a whole buffer"
    (is (=
         "ra\nd!"
         (buffer->string @example-buffer))))

  (testing "printing a buffer like this [\"first line\" \"second line\"]"
    ;; maybe this should be the default behaviour?
    (is (=
         ["ra" "d!"]
         (buffer->list-of-strings @example-buffer)))))
