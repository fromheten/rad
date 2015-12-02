(ns rad.point
  (:require [clojure.core.async :as a :refer [chan]]))

(def point (atom [0 0]))
(def point-update-channel
  (let [channel (chan)]
    (add-watch point :some-key-whatever
               (fn [key atom old-state new-state]
                 (println (str "new point:" new-state))
                 (a/put! channel new-state)))
    channel))

(defn move-point-forward
  "Returns a point where the x-axis is incremented `steps' steps"
  [steps point]
  (let [point-x (first point)
        point-y (second point)]
    [(+ steps point-x)
     point-y]))

#_(move-point-forward 5 @point)

(defn move-point-forward!
  "Moves point steps forward, defaults to 1"
  ([] (move-point-forward! 1))
  ([steps] (reset! point (move-point-forward steps @point))))
