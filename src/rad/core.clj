(ns rad.core
  (:gen-class))

(require '[clojure.core.async :as a :refer [go chan >! <!]])
(require '[rad.frontend.terminal :as term])
(require '[rad.mode])

(def io-c (term/init-terminal! term/scr))
(def in-c (:in-chan io-c))
(def out-c (:print-chan io-c))

(go
  (while true
    (a/>! out-c (<! rad.buffer/buffer-updates-channel))))

(go
  (while true
    (rad.mode/handle-keypress! (<! in-c))))

(defn -main [& args]
  (println "Varmt välkommen till rad"))

;; interesting snippet https://github.com/clojure-emacs/cider#using-embedded-nrepl-server
