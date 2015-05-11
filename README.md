# rad

`rad` is a programming enviroment and a text editor. You could say that it is
a bunch of functions to manipulate a data structure I call 'buffer'.
If you know what a rad buffer is, you know everything there is to know about `rad`.

A `rad` buffer is a list of lines.
Lines are vector of characters.
Characters are hash-maps describing a character.

A buffer can look like this:

```
[                    ;; Buffers are vectors of lines
 [                   ;; a line
  {:char :r          ;; will show up like an 'a' colored red
   :color :red}
  {:char :a
   :color :blue}]    ;; end of line
 [                   ;; another line
  {:char :d
   :font :serif}     ;; A 'c' in serif font. Or whatever
  {:char :!
   :font :serif
   :color :green}]   ;; A green 'd' in serif font
 ])
```
When rendering this and stripping font and color data, it will look like this
```
ra
d!
```
And that is pretty radical, if you ask me.

## Usage

FIXME

## License

Copyright © 2015 Martin Josefsson <hello@martinjosefsson.com>

Distributed under the GNU GPL
