;; # rad.terminal
;; A rad frontend that displays the editor into a terminal.
;; Uses the lanterna library, which is kinda like curses for JVM.

(ns rad.terminal
  "A rad frontend, usable via a terminal"
  (:require [lanterna.screen :as s]
            [rad.buffer :as buffer]))
(require '[lanterna.screen :as s])

;;(def scr (s/get-screen :text))
(def scr (s/get-screen)) ;; just for now

(defn init-terminal!
  [scr]
  (s/start scr)

  (s/put-string scr 10 10 "hello")
  (s/redraw scr))

(defn render-buffer!
  "Renders a buffer to the terminal. This happens a lot."
  [buffer scr]
  (let [string (buffer/buffer->string buffer)]
    (do (s/put-string scr 1 0 string)
        (s/redraw scr))))

(render-buffer! buffer/sample-buffer scr)

(defn get-keypress-keepalive-loop
  "Keeps the terminal alive by blockingly waiting for a keypress, and then delegating it"
  [scr callback]
  (callback (s/get-key-blocking scr))
  (s/redraw scr)
  (recur scr callback))
