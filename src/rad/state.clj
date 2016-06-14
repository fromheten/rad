(ns rad.state
  "Contains shared state for rad."
  (:require [clojure.core.async :as a :refer [chan go >!]]))

(def loaded-packages (atom []))

(def shutdown-chan (chan))

(def current-buffer (atom ["Rad is meant"
                           "to be hacked"]))
(def buffer-updates-channel
  (let [channel (chan)]
    (add-watch current-buffer :_
               (fn [_ _ _ new-state]
                 (go (>! channel new-state))))
    channel))

(def point (atom [0 0]))
(def point-update-channel
  (let [channel (chan)]
    (add-watch point :_
               (fn [_ _ _ new-state]
                 (a/put! channel new-state)))
    channel))
