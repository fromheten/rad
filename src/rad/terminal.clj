;; # rad.terminal
;; A frontend for rad that displays the editor into a terminal.
;; Uses the lanterna library, which is kinda like curses for JVM.

(ns rad.terminal)
(require '[lanterna.screen :as s])

(def scr (s/get-screen :text))
(s/start scr)

(s/put-string scr 10 10 "hello")
(s/redraw scr)


(defn get-keypress-keepalive-loop
  "Keeps the terminal alive by blockingly waiting for a keypress, and then delegating it"
  [scr callback]
  (callback (s/get-key-blocking scr))
  (get-keypress-keepalive-loop scr callback))

(s/stop scr)
