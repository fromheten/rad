;; # rad.terminal
;; A rad frontend that displays the editor into a terminal.
;; Uses the lanterna library, which is kinda like curses for JVM.

(ns rad.terminal
  "A rad frontend, usable via a terminal"
  (:require [lanterna.screen :as s]
            [rad.buffer :as buffer]))
(require '[lanterna.screen :as s])

(def scr (s/get-screen :text))
;;(def scr (s/get-screen)) ;; just for now

(defn init-terminal!
  [scr]
  (s/start scr)

  (s/redraw scr))

(defn move-cursor-in-terminal!
  [point] (s/move-cursor scr (first point) (second point)))

(defn render-buffer!
  "Renders a buffer to the terminal. This happens a lot"
  [buffer scr]
  (do
    (s/clear scr)
    (println buffer)

    (let [buffer-as-lines-of-strings (rad.buffer/buffer->list-of-strings buffer)]
      (loop [amount-of-lines-left-to-print (dec (count buffer-as-lines-of-strings))
             index 0]

        (s/put-string scr 0 index
                      (buffer-as-lines-of-strings index))

        (if (> amount-of-lines-left-to-print 0)
          (recur (dec amount-of-lines-left-to-print)
                 (inc index))
          "done")))

    (s/redraw scr)))

(render-buffer! @buffer/current-buffer scr)

(defn get-keypress-keepalive-loop
  "Keeps the terminal alive by blockingly waiting for a keypress, and then delegating it"
  [scr callback]
  (callback (s/get-key-blocking scr))
  (s/redraw scr)
  (recur scr callback))
