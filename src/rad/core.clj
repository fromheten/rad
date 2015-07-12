(ns rad.core
  (:gen-class)
  (:require [rad.terminal :as terminal]
            [rad.swt]
            [rad.buffer :as buffer]))

;; point is x & y position of the point (or cursor)
(def point (atom [0 0]))

;; FIXME understand newlines
(defn move-point-forward
  "Returns point 'steps' steps forward until it hits a newline.
  Then go to next line (not implemented)"
  [point steps] (assoc point (first point) (+ (first point) steps)))

(defn sync-frontend-cursor-to-point-atom!
  "Updates the cursor in whatever front end is active"
  []
  (terminal/move-cursor-in-terminal! @point))

(defn move-point-forward!
  "Moves point 'steps' steps, and also updates the UI cursor"
  [steps] (do
            (reset! point (move-point-forward @point 1))
            (sync-frontend-cursor-to-point-atom!)))

(defn handle-keypress!
  "Takes a key press, and delegates it into the proper action

  Todos: 
  * Make it front-end agnostic - now it depends on many things from the terminal front end"
  [key] (do (println (str "keypress: " key))
            (if (= :enter key) ;; replace with fn frontend/normalize-key
              (buffer/insert-char! @point \newline)
              
              (buffer/insert-char! @point key))
            (move-point-forward! 1)
            (terminal/render-buffer! @buffer/current-buffer terminal/scr)))

(defn -main []
  (do
    (println "Welcome to rad")
;    (rad.swt/start-gui-threaded))

    (terminal/init-terminal! terminal/scr)
    (terminal/get-keypress-keepalive-loop terminal/scr handle-keypress!)
    ))
