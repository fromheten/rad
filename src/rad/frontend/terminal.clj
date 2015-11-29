(ns rad.frontend.terminal)

(require '[lanterna.screen :as s])
(require '[clojure.core.async :as a])

(defonce things-to-print-chan (a/chan))
(defonce input-chars-chan (a/chan))
(defonce scr (s/get-screen))

(defn init-terminal! [scr]
  (s/start scr)
  (s/redraw scr))

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
        (println keypress) ; TODO Make this more amazing
        ))))

(defonce printing-thread
  (a/go
    (while true
      (let [buffer (a/<! things-to-print-chan)]
        (render-buffer! buffer scr)))))

(init-terminal! scr)

(comment
  (render-buffer! example-buffer scr)
  (a/go (a/>! things-to-print-chan ["example" "buffer"]))
  )
