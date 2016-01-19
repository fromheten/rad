(ns rad.frontend.fx
  (:require [fx-clj.core :as fx]
            [clojure.core.async :as a :refer [<! >! go-loop chan]]))

(def print-chan (chan))
(def input-chan (chan 1000))

(defn create-view []
  (let [text-area (fx/text-area
                   {:on-key-pressed (fn [event]
                                      (a/put! input-chan
                                              (.getText event)))})
        pane (fx/pane
              text-area)]
    (go-loop [] ;; print loop
      (fx/pset! text-area (<! print-chan))
      (recur))
    pane))

(defn init-fx! []
  (fx/sandbox #'create-view)
  {:print-chan print-chan
   :in-chan input-chan
   :point-chan (chan)})
