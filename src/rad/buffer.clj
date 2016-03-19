(ns rad.buffer
  (:require [clojure.core.async :as a :refer [chan go >!]]))

(def current-buffer (atom ["Rad is meant"
                           "to be hacked"]))
(def buffer-updates-channel
  (let [channel (chan)]
    (add-watch current-buffer :_
               (fn [_ _ _ new-state]
                 (go (>! channel new-state))))
    channel))

(defn delete-char-in-line
  "Returns line without the char at point"
  [line point-x]
  (try
    (str (subs line 0 point-x) (subs line (inc point-x)))
    (catch Exception e
      (delete-char-in-line line (dec (count line))))))

(defn delete-char-at-point
  "Returns buffer without the char at point"
  [buffer point]
  (if (not (> (second point) (count buffer)))
    (let [line (nth buffer (second point))
          point-x (first point)]
      (assoc buffer (second point)
             (delete-char-in-line line point-x)))
    buffer))

(defn delete-char!
  "Opposite of insert-char!"
  ([point] (reset!
            current-buffer
            (delete-char-at-point @current-buffer
                                  point))))

(defn delete-char-backwards-from-point
  ([buffer point]
   (if (-> (first point) zero?)
     buffer
     (delete-char-at-point buffer [(dec (first point)) (second point)]))))

(defn delete-char-backwards!
  "What your backspace key does"
  ([point] (swap! current-buffer
                  #(delete-char-backwards-from-point %
                                                     point))))

(defn delete-line
  "Returns buffer without the line at position line-nr"
  [buffer line-nr]
  (try (into (subvec buffer 0 line-nr)
             (subvec buffer (inc line-nr)))
       (catch Exception e buffer)))

(defn delete-line!
  "Deletes a line in the current buffer."
  ([point] (swap! current-buffer delete-line (second point))))

(defn insert-char-in-line
  "Returns a string with `char' inserted at `point-y'"
  [^String line char point-x]
  (cond
    (neg? point-x) (recur line char 0)
    (> point-x (count line)) (recur line char (count line))
    :else  (let [first-half-of-line (.substring line 0 point-x)
                 second-half-of-line (.substring line point-x (.length line))]
             (str first-half-of-line char second-half-of-line))))

(defn insert-char-at-point
  "returns a buffer with `char' at `point'"
  [buffer point ^String character]
  (let [point-x (first point)
        point-y (second point)]
    (cond
      (> point-y (count buffer)) (conj buffer character)
      :else (assoc buffer
                   point-y
                   (insert-char-in-line
                    (buffer point-y)
                    character
                    point-x)))))

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
