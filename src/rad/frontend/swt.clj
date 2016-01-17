(ns rad.frontend.swt
  (:require [clojure.core.async :as a :refer [chan]]))

(defonce print-chan (chan))
(defonce in-chan (chan))
(defonce point-sync-chan (chan))

(defn init-swt-frontend!
  "Initializes all things SWT and returns a Rad frontend"
  []
  {:print-chan print-chan
   :in-chan in-chan
   :point-chan point-sync-chan})
