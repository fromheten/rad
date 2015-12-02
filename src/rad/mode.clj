(ns rad.mode
  (:require [rad.buffer]
            [rad.point]))

(def current-mode (atom :insert))
(defn change-mode-to!
  [mode] (reset! current-mode mode))

;;; Insert mode

;; FIXME don't put hard coded mode-switching key, and don't make it escape
(defn insert-mode-handle-keypress [^String input]
  (do (println (str "Handling input: " input))
      (rad.buffer/insert-char! input @rad.point/point)
      (rad.point/move-point-forward!)))


;;; Command mode


;; common
(defn handle-keypress! [input]
  (condp = @current-mode
    :insert (insert-mode-handle-keypress input)
    ;; :command (command-mode-handle-keypress input)
    ))
