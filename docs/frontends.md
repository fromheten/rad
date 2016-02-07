# Rad front-ends
Rad is meant to be hacked! And front-ends are the premier way to hack Rad I/O.

## Conceptual overview - what is a rad frontend?
A rad frontend is a function, returning a hash-map of 3 channels.

The channels are:

* `:in-chan`

* `:print-chan`

* `:point-chan`

Whenever you want to send input to rad, send it to `:in-chan`.
When that channel receives a message, it is passed to `rad.mode/handle-keypress!`.
It will do it's thing, and might update the buffer and/or point.

When that happens, the new buffer or point will be put onto the `:print-chan` & `:point-chan`, respectively.

*That's all!* You do what you want with it - but the most obvious thing is to render the buffer & point to the user.

## Simple example - it's just channels!

``` clojure
(ns my-worthless-front-end
  (:require [clojure.core.async :as a])

(def print-chan (a/chan))
(def in-chan (a/chan))
(def point-chan (a/chan))

(defn start-render-loop! []
  (a/go-loop [last-buf ["init!!"]
              last-point [0 0]

    ;; Actually do some fancy rendering
    (println "buffer is: " last-buf)
    (println "point is: " last-point)

    (let [[v ch] (a/alts! [print-chan point-chan])]
      (if (= ch point-chan)
        (recur last-buf v)
        (recur v last-point)))))

;; Take input
;; Do this on any "keydown" events, if you want to
(a/go (a/>! in-chan :tab)
      (a/>! in-chan "r")
      (a/>! in-chan "a")
      (a/>! in-chan "d"))

;; A function, returning a hash-map of 3 channels.
(defn init-frontend []
  (start-render-loop!)
  {:print-chan print-chan
   :in-chan in-chan
   :point-chan point-chan})
```

## How to shut down rad from a front-end?

When you want to shut down rad, you set `(:should-exit @rad.state/config)` to true.

When Rad is done with whatever it is doing, it will shut down. That behaviour is defined in `rad.core/-main`.

``` clojure
(swap! rad.state/config update :should-exit? not)
```

## Roadmap

In the future rad frontends will not be defined as namespaces in the source tree of rad itself.

Instead front-ends will be just another field in a package declaration.

Right now it isn't because I wrote my first front-end before I wrote the package system.
