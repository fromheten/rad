(ns rad.point
  (:require [clojure.core.async :as a :refer [chan]]))

(def point (atom [0 0]))
(def point-update-channel
  (let [channel (chan)]
    (add-watch point :some-key-whatever
               (fn [key atom old-state new-state]
                 (a/put! channel new-state)))
    channel))

(defn move-point-forward
  "Returns a point where the x position is incremented `steps' steps"
  [point steps]
  (let [point-x (first point)
        point-y (second point)]
    [(+ steps point-x)
     point-y]))

(defn move-point-forward!
  "Moves point steps forward, defaults to 1"
  ([] (move-point-forward! 1))
  ([steps] (swap! point move-point-forward steps)))

(defn negative? [n] (< n 0))

(defn move-point-backwards
  "Returns a point where the x position is decremented `steps' steps"
  [point steps]
  (let [point-x (first point)
        new-point-x (- point-x steps)
        point-y (second point)]
    (if (negative? new-point-x)
      [0 point-y]
      [new-point-x point-y])))

(defn move-point-backwards!
  ([] (move-point-backwards! 1))
  ([steps] (swap! point move-point-backwards steps)))

(defn move-point-down
  "Returns a point where the y position is decremented `steps' steps"
  [point steps]
  (let [point-y (second point)]
    [(first point) (+ point-y steps)]))

(defn move-point-down!
  ([] (move-point-down! 1))
  ([steps] (swap! point move-point-down steps)))

(defn move-point-to-beginning-of-line
  [point]
  [0 (second point)])

(defn move-point-to-beginning-of-line! []
  (swap! point move-point-to-beginning-of-line))
