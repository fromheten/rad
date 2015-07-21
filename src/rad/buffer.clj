(ns rad.buffer
  "Functions for dealing with rad buffers.")

(declare buffer->list-of-strings)

(def current-buffer (atom [[\r \a]
                           [\d \!]]))

(defn make-character
  "Takes an alphanumeric and return a character object"
  [alphanumeric]
  alphanumeric)

(defn delete-char-in-line
  "Returns a line with whatever is at position point-x removed"
  [line point-x]
  (let [each-char-except-for-point (subvec line 0 point-x)
        second-half-of-line (subvec line (+ 1 point-x))]

    ;; put htese those in one vector
    (into each-char-except-for-point second-half-of-line)))

(defn delete-char-in-buffer
  "Deletes the character at `point' in `buffer'"
  [buffer point]
  (let [point-x (first point)
        point-y (second point)

        all-lines-above-current (subvec buffer 0 point-y)
        current-line (buffer point-y)
        current-line-without-deleted-char (delete-char-in-line current-line point-x)
        all-lines-below-current (subvec buffer (inc point-y) (count buffer))]

    ;; Join lines above current, with the current (modified) line, with all lines below
    (into
     (into all-lines-above-current
           (vector current-line-without-deleted-char))
     all-lines-below-current)))

(defn delete-char-backwards
  "Deletes a char in a buffer one column before point-x"
  [buffer point]
  (delete-char-in-buffer buffer [(dec (first point)) (second point)]))

(defn delete-char-backwards!
  "Deletes a char backwards from @point, and saves it to @current-buffer"
  [point]
  (reset! current-buffer (delete-char-backwards @current-buffer point)))

(defn insert-char-in-line
  "Returns a new line with column 'point' replaces with new alphanumeric"
  [line column alphanumeric]
  (assoc line column alphanumeric))

(defn insert-char-in-buffer
  "Inserts char at point in buffer.
  If char is a \newline, it will insert a new line in the buffer after point"
  [buffer point alphanumeric]
  (condp = alphanumeric

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

    \backspace (delete-char-backwards @current-buffer point)

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
