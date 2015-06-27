# rad

`rad` is a programming enviroment and a text editor. You could say that it is
a bunch of functions to manipulate a data structure I call 'buffer'.
If you know what a rad buffer is, you know everything there is to know about `rad`.

* A `rad` buffer is a list of lines.

* Lines are vector of characters.

* Characters are hash-maps describing a character.

A buffer can look like this:

```
[       ;; A buffer is a list of line
  [     ;; A line is a list of chars
    \r  ;; A char
    \a
  ]
  [
    \d
    \!
  ]
])
```
When rendering this, it will look like this
```
ra
d!
```
And that is pretty radical, if you ask me.

## Usage

FIXME

## Why?

I was a `vim` person for the longest time. Then I got sucked into Lisp and `emacs`.
I was also jealous of the swiftness of Sublime Text and the many nifty features
of various IDEs.

`rad` is not much more than a few loose ideas at the moment - and it might never
become anything worthwhile. My hope though, is to make a text editor that is
line-based (like `vim`). Therefore, the data structure of a buffer in `rad`
is line-based, unlike `emacs` buffers (where line numbers (`linum-mode`) are expensive).

It is also written in [Clojure](http://clojure.org). I hope to make it
better performing than `emacs`, thanks to Clojures `core.async` and easy
to use `go`-blocks.

Last but not least, I hope to let it feel modern. You should be able
to use `rad` with you keyboard only, but it should have 'normal' keyboard
shortcuts (ctrl-c for copy to system clipboard, ctrl-z for undo, etc).

That is still far in the future, though.

## What's going on now?

For the todo tasks, status report, and other notes, check out the file `todo.org`.
You are more than welcome to edit that file and send a pull request!

## License

Copyright © 2015 Martin Josefsson <hello@martinjosefsson.com>

Distributed under the GNU GPL

## Attributions
* [clojure-lanterna](http://sjl.bitbucket.org/clojure-lanterna/) by [Steve Losh](http://stevelosh.com). Thank you!!!

* [lanterna](https://code.google.com/p/lanterna/)
