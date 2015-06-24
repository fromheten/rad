(ns rad.buffer
  "Functions for dealing with rad buffers.")

(def current-buffer (atom [[{:character :r} {:character :a}]
                          [{:character :d} {:character :!}]
                          ]))

(defn make-character
  "Takes an alphanumeric and return a character object"
  [alphanumeric]
  {:character (keyword (str alphanumeric))})

(defn insert-char-in-line
  "Returns a new line with column 'point' replaces with new alphanumeric"
  [line column alphanumeric]
  (assoc line column alphanumeric))

;; fixme rename to in
(defn insert-char-in-buffer
  "Returns buffer with char inserted at point"
  [buffer point alphanumeric]
  (assoc buffer (second point) (insert-char-in-line
                                (buffer (first point)) ;x
                                (second point)         ;y

                                alphanumeric)))

(defn insert-char!
  "Inserts a character into the current buffer at point"
  [point alphanumeric]
  (swap! current-buffer
         (fn [_] (insert-char-in-buffer @current-buffer point (make-character alphanumeric)))))

(defn buffer?
  "Returns true if buffer is a proper buffer, else false"
  [buffer]
  ;; TODO FIXME
  (vector? buffer))

(defn line->string [line]
  (apply str                  ;; '(a b) -> "ab"
         (map (fn [character] ;; Returns '(a b)
                (name (:character character)))
              line)))

(defn buffer->string
  "Makes a string of all lines in a buffer, separated by newlines"
  [buffer]
  {:pre  [buffer?]
   :post [string?]}
  (do
    (println buffer)
    (clojure.string/join "\n" (map line->string buffer))))
