(ns rad.core
  (:gen-class)
  (:require [rad.terminal :as terminal]
            [rad.swt]
            [rad.buffer :as buffer]
            [rad.mode]
            [rad.point]
            :reload-all))

(defn alphanumeric?
  "Returns true if char is either a letter or a number"
  [char]
  (if (char? char)
    (not (nil? (re-matches #"^[0-9a-zA-Z ]+$" (str char))))
    false))

(defn insert-mode-handle-keypress!
  "Takes a key press, and delegates it into the proper action

  Todos:
  * Make it front-end agnostic - now it depends on many things from the terminal front end"
  [key] (do
          (condp = key
            :enter (do
                     (buffer/insert-char! @rad.point/point \newline)
                     ;; move point to one line down x=0, y=y+1
                     (reset! rad.point/point [0 (+ 1 (second @rad.point/point))])
                     (rad.point/sync-frontend-cursor-to-point-atom!))
            :backspace (do
                         (buffer/delete-char-backwards! @rad.point/point)
                         (rad.point/move-point-backwards! 1))

            ;; else
            (do  (buffer/insert-char! @rad.point/point key)
                 (rad.point/move-point-forward! 1)))

          (println (str "keypress: " key ", point: " @rad.point/point))
          (terminal/render-buffer! @buffer/current-buffer terminal/scr)))

(defn handle-keypress! [key]
  (if (= :command @rad.mode/current-mode)
    (rad.mode/command-mode-handle-key! key)
    (insert-mode-handle-keypress! key)))

(defn -main []
  (do
    (println "Welcome to rad")
;    (rad.swt/start-gui-threaded))

    (terminal/init-terminal! terminal/scr)
    (terminal/get-keypress-keepalive-loop terminal/scr handle-keypress!)))
