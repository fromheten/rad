(ns rad.buffer
  "Functions for dealing with rad buffers.")

(def current-buffer (atom [[\r \a]
                           [\d \!]]))


(defn make-character
  "Takes an alphanumeric and return a character object"
  [alphanumeric]
  alphanumeric)

(defn insert-char-in-line
  "Returns a new line with column 'point' replaces with new alphanumeric"
  [line column alphanumeric]
  (assoc line column alphanumeric))

(defn insert-char-in-buffer
  "Inserts char at point in buffer.
  If char is a \newline, it will insert a new line in the buffer after point"
  [buffer point alphanumeric]
  (condp = alphanumeric

    ;; In case of \newline, a buffer like this:
    ;; r|a ; (| means point, or [1 0])
    ;; d!
    ;;
    ;; turnes into:
    ;; r
    ;; a
    ;; d!
    \newline (let [point-x (first point)
                   point-y (second point)
                   current-line (buffer point-y)

                   every-line-above-current-line (subvec buffer 0 point-y)     ;; collection of lines
                   current-line-until-point (subvec current-line 0 point-x)    ;; one line
                   rest-of-current-line (subvec current-line point-x)          ;; one line
                   every-line-below-current-line (subvec buffer (+ 1 point-y)) ;; collection of lines

                   every-line-above-plus-current-until-point (conj every-line-above-current-line current-line-until-point)
                   above-current-plus-rest-of-current (conj every-line-above-plus-current-until-point rest-of-current-line)

                   ;; Here is the last bug. I'm adding a collection into another collection.
                   ;; What I want to do is to add every item of every-line-below-current-line into above-current-plus-rest-of-current
                   the-whole-new-line (into above-current-plus-rest-of-current every-line-below-current-line)]
               the-whole-new-line)

    ;; else
    (assoc buffer (second point) (insert-char-in-line
                                  (buffer (second point)) ;y - line
                                  (first point)           ;x - column

                                  alphanumeric))))

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
                character)
              line)))

(defn buffer->string
  "Makes a string of all lines in a buffer, separated by newlines"
  [buffer]
  {:pre  [buffer?]
   :post [string?]}
  (clojure.string/join "\n" (map line->string buffer)))

(defn buffer->list-of-strings
  "Takes a buffer, and returns a list of every line, represented as a string"
  [buffer]
  (into [] (map line->string buffer)))
