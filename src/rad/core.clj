(ns rad.core
  (:gen-class)
  (:require [rad.terminal :as terminal]
            [rad.swt]
            [rad.buffer :as buffer]))

;; point is x & y position of the point (or cursor)
(def point (atom [0 0]))
(declare sync-frontend-cursor-to-point-atom! move-point-backwards! move-point-backwards)

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

(defn sync-frontend-cursor-to-point-atom!
  "Updates the cursor in whatever front end is active"
  []
  (terminal/move-cursor-in-terminal! @point))

(defn move-point-forward!
  "Moves point 'steps' steps, and also updates the UI cursor"
  [steps] (do
            (reset! point (move-point-forward @rad.buffer/current-buffer @point 1))
            (sync-frontend-cursor-to-point-atom!)))

(defn alphanumeric?
  "Returns true if char is either a letter or a number"
  [char]
  (if (char? char)
    (not (nil? (re-matches #"^[0-9a-zA-Z ]+$" (str char))))
    false))

(defn handle-keypress!
  "Takes a key press, and delegates it into the proper action

  Todos:
  * Make it front-end agnostic - now it depends on many things from the terminal front end"
  [key] (do
          (condp = key
            :enter (do
                     (buffer/insert-char! @point \newline)
                     ;; move point to one line down x=0, y=y+1
                     (reset! point [0 (+ 1 (second @point))])
                     (sync-frontend-cursor-to-point-atom!))
            :backspace (do
                         (buffer/delete-char-backwards! @point)
                         (move-point-backwards! 1))

            ;; else
            (do  (buffer/insert-char! @point key)
                 (move-point-forward! 1)))

          (println (str "keypress: " key ", point: " @point))
          (terminal/render-buffer! @buffer/current-buffer terminal/scr)))

(defn -main []
  (do
    (println "Welcome to rad")
;    (rad.swt/start-gui-threaded))

    (terminal/init-terminal! terminal/scr)
    (terminal/get-keypress-keepalive-loop terminal/scr handle-keypress!)))
