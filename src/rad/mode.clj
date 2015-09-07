(ns rad.mode
  "State and functions for entering/leaving modes such as 'insert mode' and 'command mode'."
  (:require [rad.buffer]
            [rad.terminal]
            [rad.point]))

;; A rad command is a function. If it returns another function, it will not evaluate. If you give it something which is not fn?, it will evaluate.
;; To make commands which take arguments - they return a function that takes a function. This creates a chain, and that chain is the arguments.

(def current-mode (atom :command))           ; nil deafult to insert mode ; FIXME
(defn change-mode!
  ([] (change-mode! nil))
  ([mode]  (reset! current-mode mode)))

(defn rep
  "Runs f n times"
  [n f & acc]
  (if (< 0 n)
    (recur (dec n) f (conj acc (f)))
    acc))

(defn r []                              ;make me a macro
  (fn [n]
    (fn [f]
      (rep n f))))

(defn command-mode-key-map [] {\r r
                               \d rad.buffer/delete-char-at-current-point!
                               :escape change-mode!})

(defn get-value-for-key
  "Translates from input key to what function/argument type should be put in the command
  Numbers get returned as they are, when keys which have a value in the command-mode-key-map will get their value returned"
  [key]
  (if (number? key)
    key
    (get (command-mode-key-map) key)))

(def current-command-state (atom nil))
(defn command-mode-handle-key! [key]
  (println key)
  (let [command (get-value-for-key key)]
    (if (fn? @current-command-state)
      (if (nil? command)
        (reset! current-command-state nil)
        (reset! current-command-state (@current-command-state command)))
      (reset! current-command-state (command))))
  (println (str "keypress: " key))
  (rad.terminal/render-buffer! @rad.buffer/current-buffer rad.terminal/scr))

(defn insert-mode-handle-keypress!
  "Takes a key press, and delegates it into the proper action

  Todos:
  * Make it front-end agnostic - now it depends on many things from the terminal front end"
  [key] (do
          (condp = key
            :escape (change-mode! :command)
            :enter (do
                     (rad.buffer/insert-char! @rad.point/point \newline)
                     ;; move point to one line down x=0, y=y+1
                     (reset! rad.point/point [0 (+ 1 (second @rad.point/point))])
                     (rad.point/sync-frontend-cursor-to-point-atom!))
            :backspace (do
                         (rad.buffer/delete-char-backwards! @rad.point/point)
                         (rad.point/move-point-backwards! 1))

            ;; else
            (do  (rad.buffer/insert-char! @rad.point/point key)
                 (rad.point/move-point-forward! 1)))

          (println (str "keypress: " key ", point: " @rad.point/point))
          (rad.terminal/render-buffer! @rad.buffer/current-buffer rad.terminal/scr)))

;; (deref current-command-state)
;; (handle-key! :r)
;; (handle-key! 3)
;; (handle-key! :esc)


;; (handle-key! :d)
;; (reset! rad.buffer/current-buffer [[\r \a \d \. \i \s \. \a \n \. \e \d \i \t \o \r]
;;                                    [\s \e \c \o \n \d \. \l \i \n \e]])
;; @rad.core/point
