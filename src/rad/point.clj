(ns rad.point
  (:require [clojure.core.async :as a :refer [chan]]
            [rad.state]))

(defn move-point-forward
  "Returns a point where the x position is incremented `steps' steps"
  [point steps buffer]
  (let [point-y (second point)
        line (nth buffer point-y)
        line-length (count line)]
    (loop [steps-left steps
           point-x (first point)]
      (if (zero? steps-left)
        [point-x point-y]
        ;; check for IndexOutOfBoundException
        (if (< point-x line-length)
          (recur (dec steps-left)
                 (inc point-x))
          [point-x point-y])))))

(defn move-point-forward!
  "Moves point steps forward, defaults to 1"
  ([] (move-point-forward! 1))
  ([steps] (swap! rad.state/point move-point-forward steps @rad.state/current-buffer)))

(defn move-point-backwards
  "Returns a point where the x position is decremented `steps' steps"
  [point steps]
  (let [point-x (first point)
        new-point-x (- point-x steps)
        point-y (second point)]
    (cond
      (neg? new-point-x) [0 point-y]
      :else [new-point-x point-y])))

(defn move-point-backwards!
  ([] (move-point-backwards! 1))
  ([steps] (swap! rad.state/point move-point-backwards steps)))

(defn move-point-up
  "Returns a point where the y position is decremented `steps' steps"
  [point steps buffer]
  (let [new-point-y (- (second point) steps)]
    (if (neg? new-point-y)
      [(first point) 0]
      [(first point) new-point-y])))

(defn move-point-up!
  "Moves point `steps' steps up. Defaults to 1 step."
  ([] (move-point-up! 1))
  ([steps] (swap! rad.state/point move-point-up steps @rad.state/current-buffer)))

(defn move-point-down
  "Returns a point where the y position is decremented `steps' steps"
  [point steps buffer]
  (let [point-y (second point)
        target-line (+ point-y steps)]
    (cond
      (>= target-line (count buffer)) [(first point) (dec (count buffer))]
      :else [(first point) target-line])))

(defn move-point-down!
  ([] (move-point-down! 1))
  ([steps] (swap! rad.state/point move-point-down steps @rad.state/current-buffer)))

(defn move-point-to-beginning-of-line
  [point]
  [0 (second point)])

(defn move-point-to-beginning-of-line! []
  (swap! rad.state/point move-point-to-beginning-of-line))
