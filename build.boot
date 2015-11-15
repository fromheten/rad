#!/usr/bin/env boot

(set-env!
 :source-paths #{"src"}
 :dependencies '[[org.clojure/clojure "1.8.0-RC1"]])

(deftask build
  "Builds an uberjar of this project that can be run with java -jar"
  []
  (comp
   (aot :namespace '#{core})
   (pom :project 'rad
        :version "0.0.0")
   (uber)
   (jar :main 'core)))

(defn -main [& args]
  (require 'core)
  (apply (resolve 'core/-main) args))
