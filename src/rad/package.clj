(ns rad.package
  (:require [rad.state]
            [clojure.java.io :as io]))

(defn package?
  "Package data structure predicate"
  [package]
  (and (vector? package)
       (list? (first package))
       (= 'ns (first (first package)))
       (map? (nth (first package) 3))))

(defn load-package!
  "Loads a package into Rad"
  [package]
  {:pre [(package? package)]}
  ;; Hack because Clojure namespaces are not pure data
  (binding [*ns* (first (first package))]
    (in-ns (first (first package))) ;; without AOT can't work
    (run! eval package))
  (swap! rad.state/loaded-packages conj package))

(defn get-key-map-from-package [package]
  (:command-map (eval (nth (first package) 3))))

(defn merge-package-command-maps [packages-list]
  (into {} (map get-key-map-from-package packages-list)))

(defn get-in-evaled
  "Returns the evaled result of clojure.core/get-in"
  [m ks]
  (eval (get-in m ks)))

(defn get-package-from-file [file-path]
  {:pre [(string? file-path)]
   :post [(package? %)]}
  (second (read-string (str "'[" (slurp file-path) "]"))))

(defn clojure-files-in-dir [path]
  (->> (file-seq (clojure.java.io/file path))
       (map #(.getAbsolutePath %) ,)
       (filter #(re-find #".clj$" %) ,)))

(defn get-all-packages-in-dir [path]
  {:pre [(string? path)]
   :post [(fn [pkgs] (map package? pkgs))]}
  (map get-package-from-file (clojure-files-in-dir path)))

(defn load-all-packages-in-dir! [path]
  {:pre [(string? path)]}
  (run! load-package! (get-all-packages-in-dir path))
  @rad.state/loaded-packages)
