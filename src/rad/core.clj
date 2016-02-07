(ns rad.core
  (:gen-class))

(require '[clojure.core.async :as a :refer [go go-loop chan >! <!]])
(require '[rad.frontend.fx :as fx])
(require '[rad.mode :as mode])
(require '[rad.point :as point])
(require '[rad.buffer :as buffer])
(require '[rad.package :as package])
(require '[rad.state])

(defn -main [& args]
  (println "Varmt välkommen till rad")

  (def io-c (rad.frontend.fx/init-fx!))
  (def in-c (:in-chan io-c))
  (def out-c (:print-chan io-c))
  (def point-c (:point-chan io-c))

  (go (package/load-all-packages-in-dir! (str (System/getProperty "user.home")
                                           "/.rad/packages")))

  (go-loop []
    (a/>! out-c (<! rad.buffer/buffer-updates-channel))
    (recur))
  (go-loop []
    (a/>! point-c (<! rad.point/point-update-channel))
    (recur))
  (go-loop []
    (rad.mode/handle-keypress! (<! in-c))
    (recur))

  (loop []
    (if-not (:should-exit? @rad.state/config)
      (recur)
      (println "Exiting Rad..."))))
