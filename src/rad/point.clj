(ns rad.point
  (:require [rad.terminal]))

;; point is x & y position of the point (or cursor)
(def point (atom [0 0]))
(declare move-point-backwards! move-point-backwards)

(defn sync-frontend-cursor-to-point-atom!
  "Updates the cursor in whatever front end is active"
  []
  (rad.terminal/move-cursor-in-terminal! @rad.point/point))

(defn move-point-backwards
  "Returns point 'steps' steps backwards until it hits a beginning-of-line"
  [buffer point steps]
  (let [can-move-backwards? (fn [point-x]
                              (> point-x 0))]
    (loop [line (buffer (second point))
           point-x (first point)
           steps steps]
      (if (and
           (> steps 0)
           (can-move-backwards? point-x))

        (recur line (dec point-x) (dec steps))
        [point-x (second point)]))))

(defn move-point-backwards!
  "Moves point 'steps' steps backwards, and syncs the UI cursor"
  [steps] (do
            (reset! point (move-point-backwards @rad.buffer/current-buffer @point steps))
            (sync-frontend-cursor-to-point-atom!)))

(defn move-point-forward
  "Returns point 'steps' steps forward until it hits a newline."
  [buffer point steps]
  (let [point-x (first point)
        point-y (second point)
        line (buffer point-y)
        can-move-forward? (fn [line p-x]
                            (not (>= p-x (count line))))]

    (if (and
         (> steps 0)
         (can-move-forward? line point-x))
      (recur
       buffer
       [(inc point-x) point-y] ;; one step forward
       (- steps 1))
      point)))


(defn move-point-forward!
  "Moves point 'steps' steps, and also updates the UI cursor"
  [steps] (do
            (reset! point (move-point-forward @rad.buffer/current-buffer @point 1))
            (sync-frontend-cursor-to-point-atom!)))
