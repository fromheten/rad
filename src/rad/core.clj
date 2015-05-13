(ns rad.core
  (:gen-class))
(require '[rad.terminal :as terminal])

(def sample-buffer
  [ ;; a vector of lines
   [ ;; a line
    {:character :a
     :color :red}
    {:character :b
     :color :blue}]
   [ ;; another line
    {:character :c
     :font :serif}
    {:character :d
     :font :serif
     :color :green}]
   ])

(defn buffer? [buffer]
  "Returns true if buffer is a proper buffer, else false"
  ;; TODO FIXME
  true)

(defn line->string [line]
  (apply str                  ;; '(a b) -> "ab"
         (map (fn [character] ;; Returns '(a b)
                (name (:character character)))
              line)))

(defn buffer->string [buffer]
  "Makes a string of all lines in a buffer, separated by newlines"
  {:pre  [buffer?]
   :post [string?]}
  (clojure.string/join "\n" (map line->string buffer)))

(defn handle-keypress
  "Takes a key press, and delegates it into the proper action"
  [key]
  (println key))

(defn -main []
  (println "Welcome to rad")
  (terminal/get-keypress-keepalive-loop terminal/scr handle-keypress))
