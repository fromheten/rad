(ns rad.mode
  (:require [rad.buffer]
            [rad.point]
            [rad.package]
            [rad.state]
            [clojure.core.async :as a :refer [go go-loop chan timeout <! >!]]))

(def current-mode (atom :insert))
(defn change-mode-to!
  "Sets current-mode to a new mode. Defaults to insert mode"
  ([] (change-mode-to! :insert))
  ([mode] (println "changing mode: " (reset! current-mode mode))))

;;; Insert mode
(defn insert-mode-handle-keypress!
  "Inserts the character pressed, or changes mode"
  [input]
  (if (keyword? input)
    (condp = input
      :back_space (do (rad.buffer/delete-char-backwards! @rad.state/point)
                      (rad.point/move-point-backwards! 1))
      :tab (change-mode-to! :command)
      :enter (do (rad.buffer/insert-new-line-below-point! @rad.state/point)
                 (rad.point/move-point-to-beginning-of-line!)
                 (rad.point/move-point-down! 1))
      :up (rad.point/move-point-up!)
      :down (rad.point/move-point-down!)
      :left (rad.point/move-point-backwards!)
      :right (rad.point/move-point-forward!)

      (println (str "Can not handle input: " input)))
    (do (rad.buffer/insert-char! input @rad.state/point)
        (rad.point/move-point-forward!))))

;;; Command mode

;; Timeout code
;; This block is a little hairy, but here's the gist of it:
;; When in command mode, one second of inactivity puts you back in insert mode
;; Soon putting command mode in it's own ns would make sense

(def last-keypress-timestamp (atom (System/currentTimeMillis)))
(defn touch-timestamp! [timestamp-atom]
  (reset! timestamp-atom (System/currentTimeMillis)))

(def command-mode-keypresses-chan (chan))

(go-loop []
  (while (= :command @current-mode)
    (let [timeout-ch (timeout 1000)]
      ;; One of these channels will have a value first. The first one that
      ;; gets a value, will have it's callback evaluated.
      ;; This means, if you press a key before the timeout is done, the first
      ;; callback will be called. If you are inactive, rad will switch modes.
      ;; I'm unhappy about all the mutation going on here.
     (a/alt!
       command-mode-keypresses-chan ([result]
                                     (touch-timestamp! last-keypress-timestamp))
       timeout-ch ([result] (if (not (= :insert @current-mode))
                              (change-mode-to! :insert))))))
  (recur))
;; end timeout code

(def keystroke-accumulator (atom []))

(defn global-command-mode-key-map []
  (into {} [{\e {\e #(println "eval expression")}
             :tab #(change-mode-to! :insert)}
            (rad.package/merge-package-command-maps @rad.state/loaded-packages)]))

(defn command-mode-handle-keypress!
  "Builds a command in keystroke-accumulator, and if it points to a fn, eval it.
  Exploits the fact that (get-in) takes a vector as an argument, so builds the
  query (keystroke-accumulator) in the same format. Homoiconicity is the shit."
  [input-char]
  (let [key-map-node-or-leaf (swap! keystroke-accumulator conj input-char)
        fn-or-map (eval (get-in (global-command-mode-key-map) @keystroke-accumulator))]
    ;; go block for controlling mode timeout
    (go (touch-timestamp! last-keypress-timestamp)
        (>! command-mode-keypresses-chan input-char))
    ;; Traverse the key-map tree, and if arriving on a function, eval it
    (cond
      (fn? fn-or-map) (do (fn-or-map)
                          (reset! keystroke-accumulator [])
                          (change-mode-to! :insert))
      (nil? fn-or-map) (reset! keystroke-accumulator [])
      :else fn-or-map)))

;; common
(defn handle-keypress! [input]
  (condp = @current-mode
    :insert (insert-mode-handle-keypress! input)
    :command (command-mode-handle-keypress! input)))
