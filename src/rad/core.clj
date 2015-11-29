(ns rad.core
  (:gen-class))

(require '[clojure.core.async :as a])
(require '[rad.frontend.terminal :as term])

(def io-c (term/init-terminal! term/scr))

;; Whenever some input comes, handle that
(a/go
  (while true
    ;; (rad.mode/handle-keypress! (:in-chan io-c))
    (println (:in-chan io-c))  ;; Time to write rad.mode...
    ))

(defn -main [& args]
  (println "Varmt välkommen till rad"))

;; interesting snippet https://github.com/clojure-emacs/cider#using-embedded-nrepl-server
