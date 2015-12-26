(ns rad.package
  (:require [rad.state]
            [clojure.tools.namespace.file]))

(defn load-package!
  "Loads a package (which is a ns with some special metadata) into Rad"
  [package]
  (swap! rad.state/loaded-packages conj package))

(defn get-key-map-from-package [package]
  (:command-map (eval (nth package 3))))

(defn merge-package-command-maps [packages-list]
  (into {} (map get-key-map-from-package packages-list)))

(defn get-in-evaled                     ;utility
  "Returns the evaled result of clojure.core/get-in"
  [m ks]
  (eval (get-in m ks)))

(defn load-package-from-file! [file-path]
  (load-file file-path)                 ;Load into the Lisp enviroment
  (load-package!                        ;Load into the Rad enviroment
   (clojure.tools.namespace.file/read-file-ns-decl file-path)))
