#!/usr/bin/env boot

(set-env!
 :source-paths #{"src"}
 :dependencies '[[org.clojure/clojure "1.8.0-RC1"]
                 [clojure-lanterna "0.9.4"]
                 [org.clojure/core.async "0.2.374"]])

(deftask build
  "Builds an uberjar of this project that can be run with java -jar"
  []
  (comp
   (aot :namespace '#{rad.core})
   (pom :project 'rad
        :version "0.0.0")
   (uber)
   (jar :main 'rad.core)))

(defn -main [& args]
  (require 'core)
  (apply (resolve 'rad.core/-main) args))
