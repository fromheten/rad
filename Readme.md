# Rad

## How to run
You need to have [`boot`](https://github.com/boot-clj/boot#install) installed.
You also need to have the JVM installed. I have tested with version `8`.

`$ boot build` will create a `.jar` file in the `target/` directory.

### Load the app into a repl
To start a repl with the app loaded, run `boot repl`. It also opens a nrepl, so you can connect another editor, such as Cursive or CIDER (you can also just `M-x cider-jack-in`, if you are a CIDER user)

## Concepts
### Buffer
Text in Rad is represented as a buffer. A buffer is a list of lines, and lines are string.

Here is an example of a rad buffer

``` Clojure
["Rad is meant"
"to be hacked!"]
```

This will render into

```
Rad is meant
to be hacked!
```

Functions manipulating buffers are mostly in `rad.buffer`. Go hack there :).

### Front-ends
It's really easy to make front-ends for rad. You can do it in 5 minutes, I'm sure.

Front-ends communicate with Rad thru channels, from `clojure.core.async`.
If you are unfamiliar with `core.async`, do this [repl-based core.async-tutorial](https://github.com/clojure/core.async/blob/master/examples/walkthrough.clj).

A front-end is nothing more than *a function returning a hash-map with 3 channels.*
Here is a (silent and useless) rad front-end:

``` Clojure
(require '[clojure.core.async :as async)
(defn worthless-frontend
  []
  {:print-chan (async/chan)
    :in-chan (async/chan)
    :point-chan (async/chan)})
```
* Rad will send whatever text to print thru `:print-chan`.

* The *point* (a vector of 2 numbers, describing [x y] of the cursor) is sent to `:point-chan` whenever it's updates, so you can put it in the right position in your front-end.

* Send any keystrokes into `:in-chan`, preferably as `char` (like `\d`, `\a`, etc).

### Modes
Rad (currently) has 2 modes. They are defined in `rad.mode`.

* Insert mode
* Command mode

*Insert mode* is like any text editor - type something, and it shows up on the screen.

*Command mode* is kind of like `vim`. I'll document this better later...

### Packages
Rad packages are crazy simple. They have exactly the same form as a Clojure `ns` macro.
Only difference is: a rad package requires a `docstring` and an `attr-map`.

Rad packages are a vector where the first element is a ns declaration. Think a normal clojure file, wrapped in `[]`.

All packages in `~/.rad/packages` will be loaded upon startup.

All standard packages that are distributed along with Rad are located in `../standard-packages/`.

To load a package from file, use `rad.package/load-package-from-file!`.

To load a package from a Clojure list (beginning with the symbol `ns`), use `rad.package/load-package!`.

#### Example package - test-package
This is the package I used for testing. It's very small but shows off what capabilities lie in Rad packages.

``` clojure
(ns replace-buffer-with-happy-message
  "Replaces the current buffers content with an uplifting message.
  Just for fun."
  {:author {:name "Martin Josefsson"
            :url "http://www.martinjosefsson.com"
            :email "hello@martinjosefsson.com"}
   :command-map '{\!
                  {\h
                   {\a
                    {\p
                     {\p
                      {\y
                       (fn [] (reset! rad.state/current-buffer
                                      (replace-buffer-with-happy-message/message!)))}}}}}}})

(println "Happy Hacking :)")
(defn message! []
  (let [msg "Happy Hacking :)"]
    (println (str "Happy Hacking :)" msg))
    [msg]))
```

When `replace-buffer-with-happy-message` is loaded (with `rad.package/load-package-from-file!`), it will print "Happy Hacking :)" to STDOUT.

It also adds the command-mode command `happy`, which uses vars defined in `rad.buffer` as well as in the package.

Add the replace-buffer-with-happy-message package to `~/.rad/packages`, restart Rad, enter command mode and type `happy` and you will see it in action.

## Why?
### A hackable enviroment
[Bret Victor explains it: youtube link](https://youtu.be/klTjiXjqHrQ)

Computer programs are not what you type into your source files.

We who create in the medium of the computer write code in a file.
Then we feed it to a compiler. Then we run the program. Then we observe it.

This works, and you get a working program out in the end.

Like the painter instantly sees the result of their stroke on the canvas,
and the musician instantly hears the sound of the plucked string.
We, who create for the medium of the computer, also have that opportunity.

*There is another way*

Programs can be created interactively. With Rad I hope to shorten the path between
thought and program execution.

#### Interactive development, for all languages
In Rad, the command `e e` stands for "Evaluate Expression", evaluating what is under the point.

``` Javascript
con▮sole.log("Hello Rad!");
```
`▮` means point. The command `e e` here will evaluate the expression under the point.
It will run the function `console.log` in a Javascript repl under the hood.

The same of course is even simpler for lisps.

``` Clojure
(map ▮inc [1 2 3)
```
The `e e` command will here evaluate within the map, and you will see [2 3 4].

I've yet to figure out how to get this functionality in non-interpreted languages, but I'm sure it's doable.

#### Keyboard-driven design
Put your fingers on the home row. If you don't touch, type, [learn it in 20 minutes](https://www.typingclub.com/typing-qwerty-en/keys-jf.html).

You now have 47 inputs, at your fingertips (on my keyboard). Combine with modifier keys, and that number is multiplied.

When using a mouse, you have two possible inputs (right and left click).

### Dissatisfaction with current programmers enviroments

#### Vim
* Not easy to extend
* Not a good enviroment to build apps
* You have to remember what mode you are in

#### Emacs
* Gap buffers instead of line-based one (like rad, vim & atom)
(This is the reason why showing line numbers is slow in Emacs)
* Blocking all the time, nothing is async
* Emacs pinky. Your pinky finger is always resting on `Ctrl` or `Meta`. That leaves a 10-fingered person with only 9 fingers available.
* My coworkers hate pairing with me

#### Atom
* You end up using the mouse a lot
* Not a lisp (still an amazing editor though)
* Slow as hell
* JS has no `core.async`

## License

All contributions given to this project, I (Martin Josefsson) own. By contributing, you agree to this.

All Rad source code is licensed under GPLv3. You can read the full text in Licence-gpl3.txt.

My hope is that Rad will be hacked upon, and I hope that this license can aid in that.

Your own code that interacts with Rad can have whatever license you wish to, of course.
