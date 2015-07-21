(defproject rad "0.0.2"
  :description "A radically simple hackers enviroment"
  :url "https://github.com/fromheten/rad"
  :license {:name "GNU GPL"
            :url "https://gnu.org/licenses/gpl.html"}
  :plugins [[jonase/eastwood "0.2.1"]]
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [clojure-lanterna "0.9.4"]
                 [org.eclipse/swt-cocoa-macosx-x86_64 "3.5.2"]]
  :main rad.core)
