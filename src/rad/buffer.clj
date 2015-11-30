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

(def point (atom [0 0])) ; move me to another ns

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
        first-half-of-line (.substring line 0 point-y)
        second-half-of-line (.substring line point-y (.length line))
        new-line (str first-half-of-line char second-half-of-line)]
    ;; (assoc buffer (point-y))
    (assoc buffer point-y new-line)))

(defn insert-char!
  "Inserts one-char input at @point, and moves @point forward"
  [^String input]
  (do (reset!
       rad.buffer/current-buffer
       (rad.buffer/insert-char-at-point @rad.buffer/current-buffer
                                        @rad.buffer/point
                                        input))))

#_(= ["Rad is meant" "tho be hacked"]
     (insert-char-at-point ["Rad is meant" "to be hacked"] [0 1] "h"))
