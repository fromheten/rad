(ns rad.mode
  (:require [rad.buffer]
            [rad.point]))

(def current-mode (atom :insert))
(defn change-mode-to!
  [mode] (reset! current-mode mode))

;;; Insert mode
(defn insert-mode-handle-keypress [input]
  (if (keyword? input)
    (if (= :backspace input)
      (rad.buffer/delete-char! @rad.point/point))
    (do (rad.buffer/insert-char! input @rad.point/point)
        (rad.point/move-point-forward!))))

;;; Command mode


;; common
(defn handle-keypress! [input]
  (condp = @current-mode
    :insert (insert-mode-handle-keypress input)
    ;; :command (command-mode-handle-keypress input)
    ))
