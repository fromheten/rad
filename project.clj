(defproject rad "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :jvm-opts ["-XstartOnFirstThread"]
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [clojure-lanterna "0.9.4"]
                 [org.eclipse.swt/org.eclipse.swt.cocoa.macosx.x86_64 "4.4"]]
  :repositories [["swt-repo" "https://raw.githubusercontent.com/maven-eclipse/swt-repo/master/"]]
  :main rad.core)
