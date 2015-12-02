(ns rad.core
  (:gen-class))

(require '[clojure.core.async :as a :refer [go chan >! <!]])
(require '[rad.frontend.terminal :as term])
(require '[rad.mode :as mode])
(require '[rad.point :as point])
(require '[rad.buffer :as buffer])

(def io-c (term/init-terminal! term/scr))
(def in-c (:in-chan io-c))
(def out-c (:print-chan io-c))
(def point-c (:point-chan io-c))

(go
  (while true
    (a/>! out-c (<! rad.buffer/buffer-updates-channel))))
(go
  (while true
    (a/>! point-c (<! rad.point/point-update-channel))))
(go
  (while true
    (rad.mode/handle-keypress! (<! in-c))))

(defn -main [& args]
  (println "Varmt välkommen till rad"))

#_[@rad.buffer/current-buffer
 @rad.point/point]
#_(do
    (reset! rad.point/point [0 0])
    (reset! rad.buffer/current-buffer ["Rad is meant" "to be hacked!"]))

;; interesting snippet https://github.com/clojure-emacs/cider#using-embedded-nrepl-server
