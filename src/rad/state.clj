(ns rad.state
  "Contains shared state for rad."
  (:require [clojure.core.async :refer [chan]]))

(def loaded-packages (atom []))

(def shutdown-chan (chan))
