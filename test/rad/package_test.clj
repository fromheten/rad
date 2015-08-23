(ns rad.package-test
  (:require [clojure.test :refer :all]
            [rad.package :refer :all]))

(def test-package-list
  {:say-input-out-loud {:name "say-input-out-loud"
                        :doc-string "Whatever input is given, send it thru Mac OS X's text-to-speech"
                        :author {:name "Martin Josefsson"
                                 :email "rad-admin@martinjosefsson.com"
                                 :web "http://www.martinjosefsson.com/"}
                        :input-fn (fn [char] "it works")}})

(package? (test-package-list :say-input-out-loud))
