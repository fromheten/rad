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


(defn handle-keypress! [key]
  (if (= :command @rad.mode/current-mode)
    (rad.mode/command-mode-handle-key! key)
    (rad.mode/insert-mode-handle-keypress! key)))

(defn -main []
  (do
    (println "Welcome to rad")
;    (rad.swt/start-gui-threaded))

    (terminal/init-terminal! terminal/scr)
    (terminal/get-keypress-keepalive-loop terminal/scr handle-keypress!)))
