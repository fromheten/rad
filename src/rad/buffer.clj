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
        line-after-deletion (str
                             (.substring string 0 point-x)
                             (try (.substring string (inc point-x) (.length string))
                                  (catch java.lang.StringIndexOutOfBoundsException
                                      e
                                    "")))]
    (assoc buffer (second point) line-after-deletion)))

(defn delete-char!
  "Opposite of insert-char!"
  ([point] (reset!
            current-buffer
            (delete-char-at-point @current-buffer
                                  point))))

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
       current-buffer
       (insert-char-at-point @current-buffer
                             point
                             input))))

(defn insert-new-line-at-line-number
  "Returns a buffer with a blank line at position `line-nr'"
  [buffer line-nr]
  (if (> line-nr (count buffer))
    (recur buffer (dec line-nr))
    (let [lines-above-line-number (subvec buffer 0 line-nr)
          lines-below-line-number (subvec buffer line-nr (count buffer))]
      (into (into lines-above-line-number [""]) lines-below-line-number))))

(defn insert-new-line-at-line-number!
  "Inserts a new line into the current buffer"
  ([line-nr] (swap! current-buffer insert-new-line-at-line-number line-nr)))

(defn insert-new-line-below-point!
  [point] (insert-new-line-at-line-number! (inc (second point))))
