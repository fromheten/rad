(ns rad.frontend.fx
  (:require [fx-clj.core :as fx]
            [clojure.core.async :as a :refer [chan go go-loop alts! >! <!]]
            [rad.state])
  (:import (javafx.stage Modality)))

(def print-chan (chan))
(def input-chan (chan 1000))
(def point-chan (chan 1000))

(defn hiccup-char
  [^String character & point?]
  (if (first point?)
    [:text-flow.point [:text.point character]]
    [:text-flow [:text character]]))

(defn hiccup-line
  "Makes rad-friendly fx hiccup syntax from a string.
  Adding the .point class where appropriate"
  [^String line & point-x]
  (if (zero? (count line))              ; empty line
    (if point-x
      [:flow-pane (hiccup-char " " true)]
      [:flow-pane (hiccup-char " ")])

    (loop [hiccup [:flow-pane]
           chars-left (count line)
           index 0]
      (if (zero? chars-left)
        (if (and (not (nil? point-x))
                 (or
                  (= (first point-x) index)
                  (> (first point-x) (count line))))
          (conj hiccup (hiccup-char " " true))
          hiccup)
        (recur (into hiccup [(hiccup-char
                              (str (.charAt line index))
                              (= index (first point-x)))])
               (dec chars-left)
               (inc index))))))

(defn fx-hiccup
  "Converts a Rad buffer into fx hiccup syntax"
  [buffer & point]
  (let [point (first point)] ; not yet ready for multiple points
    (->> buffer
         (map-indexed (fn [line-number line]
                        (if (= line-number (second point))
                          [(hiccup-line line (first point))]
                          [(hiccup-line line)])))
         (apply concat ,)
         (into [:v-box] ,))))

(defn fx-keydown->rad-char
  "Takes a map that looks like a JavaFX KeyPress object.
  Returns char or keyword that rad.mode understands"
  [key-press-map]
  (cond
    (= "\t" (:text key-press-map)) :tab
    (= "ENTER" (:code key-press-map)) :enter
    (not (empty? (:text key-press-map))) (.charAt (:text key-press-map) 0)
    (not (empty? (:code key-press-map))) (-> (:code key-press-map)
                                             clojure.string/lower-case
                                             keyword)
    :else key-press-map))

(defn buffer->widgets
  "Convert a buffer into JavaFX objects"
  [buffer point]
  (fx/compile-fx (fx-hiccup buffer point)))

(defn app-window!
  "Initiates all the Java shit for creating a JavaFX window.
  Major problem: can not run twice, becuase two windows will share state"
  []
  (fx/run<!!
   (let [scene (fx/scene (buffer->widgets [""] [0 0]))
         stage (fx/stage)]
     (fx/pset! scene
               {:on-key-pressed
                (fn [event]
                  (a/put! input-chan
                          (fx-keydown->rad-char
                           {:text (.getText event)
                            :character (.getCharacter event)
                            :code (.toString (.getCode event))
                            :alt-down? (.isAltDown event)
                            :control-down? (.isControlDown event)
                            :meta-down? (.isMetaDown event)
                            :shift-down? (.isShiftDown event)
                            :shortcut-down? (.isShortcutDown event)})))})
     (fx/pset! stage
               {:on-close-request
                (fn [_] (a/put! rad.state/shutdown-chan :exit))})

     (fx/set-global-css! [[:* {:-fx-font-family "monaco, Consolas, 'Lucida Console', monospace"}]
                          [:.point {:-fx-background-color "limegreen"}]])

     (.setScene stage scene)
     (.initModality stage Modality/NONE)
     (fx/pset! stage {:title "rad"})
     (.setHeight stage 300)
     (.setWidth stage 533)

     (.show stage)

     ;; Render any buffers coming into print-chan
     (let [render! (fn [scene buffer point]
                     (fx/pset! scene {:root (buffer->widgets buffer point)}))
           welcome-message ["Rad is meant"
                            "to be hacked"]]

       (a/go-loop [last-buf welcome-message
                   last-point [0 0]]
         (render! scene last-buf last-point)
         (let [[v ch] (a/alts! [print-chan point-chan])]
           (if (= ch point-chan)
             (recur last-buf v)
             (recur v last-point)))))
     stage)))

(defn init-fx! []
  (app-window!)
  {:print-chan print-chan
   :in-chan input-chan
   :point-chan point-chan})
