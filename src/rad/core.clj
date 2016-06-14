(ns rad.core
  (:gen-class)
  (:require [clojure.core.async :as a :refer [go go-loop chan >! <!]]
            [clojure.stacktrace]
            [rad.frontend.fx :as fx]
            [rad.mode :as mode]
            [rad.point :as point]
            [rad.buffer :as buffer]
            [rad.package :as package]
            [rad.state]))

(Thread/setDefaultUncaughtExceptionHandler
 (reify Thread$UncaughtExceptionHandler
   (uncaughtException [_ thread throwable]
     (println "\nOh no!\n")
     (println (clojure.stacktrace/print-stack-trace throwable))
     (println "\n さようなら \n")
     (System/exit 1))))

(defn -main [& args]
  (println "Varmt välkommen till rad")

  (def io-c (rad.frontend.fx/init-fx!))
  (def in-c (:in-chan io-c))
  (def out-c (:print-chan io-c))
  (def point-c (:point-chan io-c))

  (go (package/load-all-packages-in-dir! "standard-packages")
      (package/load-all-packages-in-dir! (str (System/getProperty "user.home")
                                              "/.rad/packages")))

  (go-loop []
    (a/>! out-c (<! rad.state/buffer-updates-channel))
    (recur))
  (go-loop []
    (a/>! point-c (<! rad.state/point-update-channel))
    (recur))
  (go-loop []
    (rad.mode/handle-keypress! (<! in-c))
    (recur))

  (a/<!! rad.state/shutdown-chan) ; this is how rad is told to exit
  (println "Exiting Rad..."))
