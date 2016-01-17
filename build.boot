#!/usr/bin/env boot

(set-env!
 :source-paths #{"src" "test"}
 :repositories {"central" "http://repo1.maven.org/maven2/"
                "maven-eclipse-repo" "http://maven-eclipse.github.io/maven"
                "clojars.org" "http://clojars.org/repo"}
 :dependencies '[[org.clojure/clojure "1.8.0-RC1"]
                 [clojure-lanterna "0.9.4"]

                 [org.eclipse.swt/org.eclipse.swt.win32.win32.x86 "4.5"]
                 [org.eclipse.swt/org.eclipse.swt.win32.win32.x86_64 "4.5"]
                 [org.eclipse.swt/org.eclipse.swt.gtk.linux.x86 "4.5"]
                 [org.eclipse.swt/org.eclipse.swt.gtk.linux.x86_64 "4.5"]
                 [org.eclipse.swt/org.eclipse.swt.cocoa.macosx.x86_64 "4.5"]

                 [org.clojure/tools.namespace "0.2.11"]
                 [org.clojure/core.async "0.2.374"]
                 [adzerk/boot-test "1.0.5" :scope "test"]])
(require '[adzerk.boot-test :refer :all])

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
