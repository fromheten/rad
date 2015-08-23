# Rad packages

The rad enviroment is like a function.
It is a function of user intent.
It outputs to a computer screen.

A package in its most basic form can do one or both of two things.

* Act upon input events
* Filters buffer on output

## Input events

When a user inputs, that input will be passed to every function in a list.
You can add things to that list.
While your package is loaded, all input will be given to your `:input-fn`.

Here is an example of a input fn that will flash an imaginary light with the inputted key as morse

```
(defn blink-morse-light
  [input-key] ;; First argument is whatever key the user pressed
  "Blinks an imaginary lamp with the morse code of the inputted character"
  (if (alphanumeric? input-key)
    (-> input-key
        make-morse-code
        imaginary-light/flash!)))
```

You can put your dirty stinky side-effect infected code on input events.

## Output filters (only half-way implemented as of now)

Before displaying the buffer, it is passed thru all functions in the output-filters list.
These filters are functions that take (amongst other things) the current buffer.
They return a buffer.

Here is an example of a output-filter.
It will add [mätaldöts](https://en.wikipedia.org/w/index.php?title=Metal_umlaut) to all wowels

```
(defn metalize-buffer
  "Takes a buffer, and replaces every a,o,u,e or y with their umlaut versions.
  Vërÿ mëtäl"
  [buffer]
  {:pre [rad.buffer/buffer?]
   :post [rad.buffer/buffer?]}
  (let [vowels-to-metalize-map {\a \ä, \o \ö, \u \ü \e \ë, \y \ÿ
                                \A \Ä, \O \Ö, \U \Ü \E \Ë, \Y \Ÿ}
        vowel? (fn [char] (contains? vowels-to-metalize-map char))
        add-umlaut-to-char (fn [char] (get vowels-to-metalize-map char))
        metalize-char (fn [char] (if (vowel? char)
                                    (add-umlaut-to-char char)
                                    char))
        metalize-line (fn [line] (into [] (map metalize-char line)))]

    (into [] (map metalize-line buffer))))
```
As you can see, it only takes a buffer and returns one.

## `defpackage`

To create a plugin, create a `.clj` file. Let it begin with it's own namespace.
You can have whatever code you wish in there.
The only thing you need is a call to `defpackage`.

Here is an example where we put the two above functions into a package

```
(defpackage 'morse-and-metaldots
  "Blinks a light with the input as morse code on keypress, and adds metaldots to the buffer"
  {:author {:name "Martin Josefsson"
            :email "rad@example.org"
            :web "http://www.example.org/morse-and-metal"}
   :code {:web "http://www.gitlab.com/example/metal-morse"}}
  {:input-fn blink-morse-light
   :output-fn metalize-buffer
   :commands []})
```
Any defpackage that is placed in `~/.rad/packages` will be run on startup.

## Good job
You now know how to extend rad. Pretty radical
