(ns rad.core
  (:gen-class))
(require '[rad.terminal])

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

(defn -main []
  (println (buffer->string sample-buffer)))
