(ns rad.mode
  (:require [rad.buffer]))

(def current-mode (atom :insert))
(defn change-mode-to!
  [mode] (reset! current-mode mode))

;;; Insert mode

;; FIXME don't put hard coded mode-switching key, and don't make it escape
(defn insert-mode-handle-keypress [^String input]
  (println (str "Handling input: " input))
  (reset!
   rad.buffer/current-buffer
   (rad.buffer/insert-char-at-point @rad.buffer/current-buffer
                                    @rad.buffer/point
                                    input)))


;;; Command mode


;; common
(defn handle-keypress! [input]
  (condp = @current-mode
    :insert (insert-mode-handle-keypress input)
    ;; :command (command-mode-handle-keypress input)
    ))
