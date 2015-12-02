(ns rad.buffer
  (:require [clojure.core.async :as a :refer [chan go >!]]))

(def current-buffer (atom ["Rad is meant"
                           "to be hacked"]))
(def buffer-updates-channel
  (let [channel (chan)]
    (add-watch current-buffer :some-key
               (fn [key atom old-state new-state]
                 (go (>! channel new-state))))
    channel))

(defn delete-char-at-point
  "Returns buffer without the char at point"
  [buffer point]
  (let [string (nth buffer (second point))
        point-x (first point)
        line-after-deletion (str (.substring string 0 point-x)
                                 (.substring string (inc point-x) (.length string)))]
    (assoc buffer (second point) line-after-deletion)))

#_(delete-char-at-point @current-buffer [0 1])

(defn insert-char-at-point
  [buffer point char]
  (let [point-x (first point)
        point-y (second point)

        line (buffer point-y)
        first-half-of-line (.substring line 0 point-x)
        second-half-of-line (.substring line point-x (.length line))

        ;; on next line something fishy is happening
        new-line (str first-half-of-line char second-half-of-line)]
    ;; (assoc buffer (point-y))
    (assoc buffer point-y new-line)))

(defn insert-char!
  "Inserts one-char input at point"
  [^String input point]
  (do (reset!
       rad.buffer/current-buffer
       (insert-char-at-point @current-buffer
                             point
                             input))))


#_(= ["Rad is meant" "tho be hacked"]
     (insert-char-at-point ["Rad is meant" "to be hacked"] [0 1] "h"))
