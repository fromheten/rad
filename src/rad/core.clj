(ns rad.core
  (:gen-class))

(require '[clojure.core.async :as a :refer [go chan >! <!]])
(require '[rad.frontend.terminal :as term])

(require '[rad.mode])

(def io-c (term/init-terminal! term/scr))
(def in-c (:in-chan io-c))
(def out-c (:print-chan io-c))

(go (a/>! out-c [(str (+ 1 (rand)))]))

;; Whenever the current-buffer updates, show to the user
(go (while true
      (>! out-c
          (<! rad.buffer/buffer-updates-channel))))



(defn -main [& args]
  (println "Varmt välkommen till rad"))

;; interesting snippet https://github.com/clojure-emacs/cider#using-embedded-nrepl-server
