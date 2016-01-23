(ns rad.frontend.fx
  (:require [fx-clj.core :as fx]
            [clojure.core.async :as a :refer [<! >! go-loop chan]])
  (:import (javafx.scene.input KeyCode)
           (javafx.stage Modality)))

(def print-chan (chan))
(def input-chan (chan 1000))

(defn fx-hiccup
  "Converts a Rad buffer into fx hiccup syntax"
  [buffer]
  (into [:text-flow] (mapcat (fn [line]
                               [[:text line] [:text "\n"]])
                             buffer)))

(defn fx-keydown->rad-char
  "Takes a map that looks like a JavaFX KeyPress object.
  Returns char or keyword that rad.mode understands"
  [key-press-map]
  (cond
    (= "\t" (:text key-press-map)) :tab
    (not (empty? (:text key-press-map))) (.charAt (:text key-press-map) 0)
    (not (empty? (:code key-press-map))) (-> (:code key-press-map)
                                             clojure.string/lower-case
                                             keyword)
    :else key-press-map))

(defn buffer->widgets
  "Convert a buffer into JavaFX objects"
  [buffer]
  (fx/compile-fx (fx-hiccup buffer)))

(defn app-window! [refresh-fn & config-map]
  (fx/run<!!
   (let [scene (fx/scene (refresh-fn))
         stage (fx/stage)]
     (fx/pset! scene
               {:on-key-pressed
                (fn [event]
                  (if (= KeyCode/F5 (.getCode event))
                    (fx/pset! scene {:root (refresh-fn)})
                    (a/put! input-chan
                            (fx-keydown->rad-char
                             {:text (.getText event)
                              :character (.getCharacter event)
                              :code (.toString (.getCode event))
                              :alt-down? (.isAltDown event)
                              :is-control-down (.isControlDown event)
                              :is-meta-down (.isMetaDown event)
                              :is-shift-down (.isShiftDown event)
                              :is-shortcut-down (.isShortcutDown event)}))))})
     (.setScene stage scene)
     (.initModality stage Modality/NONE)
     (fx/pset! stage {:title "rad"})
     (.setHeight stage 300)
     (.setWidth stage 533)

     (.show stage)

     ;; Render any buffers coming into print-chan
     (a/go-loop []
       (fx/pset! scene {:root (buffer->widgets (a/<! print-chan))})
       (recur))

     stage)))

(defn init-fx! []
  (app-window! #(buffer->widgets ["Rad is meant" "to be hacked"]))
  {:print-chan print-chan
   :in-chan input-chan
   :point-chan (chan)})
