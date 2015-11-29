(ns rad.frontend.terminal)

(require '[lanterna.screen :as s])
(require '[clojure.core.async :as a])

(def print-chan (a/chan))
(def in-chan (a/chan))
(defonce scr (s/get-screen))

(def example-buffer ["Rad is meant" "to be hacked"])

(defn render-buffer!
  "Renders a buffer to a screen. Happens a lot, don't bloat"
  [buffer scr]
  (do
    (s/clear scr)
    (loop [amount-of-lines-left-to-print (dec (count buffer))
           index 0]
      (s/put-string scr 0 index (buffer index))
      (if (> amount-of-lines-left-to-print 0)
        (recur (dec amount-of-lines-left-to-print)
               (inc index))))
    (s/redraw scr)))

(def input-thread
  (a/go
    (while true
      (let [keypress (s/get-key-blocking scr)]
        (a/>! in-chan keypress)))))

(defonce printing-thread
  (a/go
    (while true
      (let [buffer (a/<! print-chan)]
        (render-buffer! buffer scr)))))

(defn init-terminal! [scr]
  (s/start scr)
  (s/redraw scr)
  (render-buffer! ["Rad is meant" "to be hacked"] scr)
  {:print-chan print-chan
   :in-chan in-chan})

(comment
  (render-buffer! example-buffer scr)
  (a/go (a/>! print-chan ["example" "buffer"]))
  )
