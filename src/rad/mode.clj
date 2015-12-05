(ns rad.mode
  (:require [rad.buffer]
            [rad.point]))

(def current-mode (atom :insert))
(defn change-mode-to!
  [mode]
  (reset! current-mode mode))

;;; Insert mode
(defn insert-mode-handle-keypress! [input]
  (if (keyword? input)
    (condp = input
      :backspace (rad.buffer/delete-char! @rad.point/point)
      :tab (change-mode-to! :command))
    (do (rad.buffer/insert-char! input @rad.point/point)
        (rad.point/move-point-forward!))))

;;; Command mode
(def keystroke-accumulator (atom []))
(def key-map {\d {\w #(println "delete word")
                  \h #(println "delete backwards")
                  \l #(println "delete character")}
              \e {\e #(println "eval expression")}
              :tab #(change-mode-to! :insert)})


(defn command-mode-handle-keypress!
  "Builds a command in keystroke-accumulator, and if it points to a fn, eval it.
  Exploits the fact that (get-in) takes a vector as an argument, so builds the
  query (keystroke-accumulator) in the same format. Homoiconicity is the shit."
  [input-char]
  (let [key-map-node-or-leaf (swap! keystroke-accumulator conj input-char)
        fn-or-map (get-in key-map @keystroke-accumulator)]
    (cond
      (fn? fn-or-map) (do (fn-or-map)
                          (reset! keystroke-accumulator []))
      (nil? fn-or-map) (reset! keystroke-accumulator [])
      :else fn-or-map)))

;; common
(defn handle-keypress! [input]
  (condp = @current-mode
    :insert (insert-mode-handle-keypress! input)
    :command (command-mode-handle-keypress! input)))
