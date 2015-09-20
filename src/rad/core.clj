(ns rad.core
  (:gen-class)
  (:require [clojure.core.async :as async :refer [chan go >! <!]]
            [rad.terminal :as terminal]
            [rad.swt]
            [rad.buffer :as buffer]))

(defn leader-key?
  "Checks if a key is the leader key (ctrl for most front-ends)"
  [key]
  (= key "\\" ))

(defn leader-key
  "This function dispatches whatever is written after the leader is hit"
  [])

(defn handle-keypress!
  "Takes a key press, and delegates it into the proper action"
  [key] (do (println (str "keypress: " key))
            (if (leader-key? key)
              (leader-key key)
              ;; might be an alt-leader too
              (buffer/insert-char ["point"] key))
            (terminal/render-buffer! buffer/sample-buffer terminal/scr)))

(def text-to-display-chan (chan))
(def input-char-chan (chan))
(go (>! text-to-display-chan "Rad is meant to be hacked"))
(defn -main []
  (do (println "Welcome to rad")
      (rad.swt/begin input-char-chan text-to-display-chan))
  ;;  (do (terminal/init-terminal! terminal/scr)
  ;;      (terminal/get-keypress-keepalive-loop terminal/scr handle-keypress!))
  )
