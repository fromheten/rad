(ns rad.package
  (:require [clojure.core.async :as a :refer [go chan >! <!]]
            [clojure.tools.namespace.file]))

;; A rad packet is a clojure namespace. It is in a file.
;; All packages in ~/.rad/packages will be loaded on init

;; Loading a package means:
;; 1. load-file
;; 2. save the ns form into `active-packages'

;; Packages can specify a :command-map.
;; It works with command-mode.
;; Command mode should do lookups in active-packages
;; Last package loaded, first priority for command selection.
;; I think clojure.core/merge can be useful for that.

;; TODO
;; * Make function is-package? that takes a list, and checks if it's a valid package (starts with ns, then name, then hash-map, which in turn contains whatever fields are required)
;; * Have command-mode search thru all the active packages when matching


;; playing
(let [directory (clojure.java.io/file (str (System/getProperty "user.home")
                                           "/.rad/packages"))
      files (file-seq directory)
      file-paths (map #(.getAbsolutePath %) files)]
  file-paths)

;; done playing. Lie! Never done playing :D

(defn load-package!
  "Loads a ns at file-path, and return its ns declaration"
  [file-path]
  {:pre [string?]}
  (load-file file-path)
  (clojure.tools.namespace.file/read-file-ns-decl file-path))

(def active-packages (atom []))
(def load-package-channel (chan 1000))
(def package-loading-loop
  (a/go-loop []
    (swap! active-packages conj (load-package! (<! load-package-channel)))
    (recur)))

#_(a/put! load-package-channel "/Users/martin/.rad/packages/new-kind-of-package/new-kind-of-package.clj")
#_(new-kind-of-package/say-hi "RAD")

(defn unload-namespace!
  "Unloads every var in a namespace"
  [ns]
  (doseq [[sym var] (ns-map ns)]
    (if (and (var? var)
             (= (.. var -ns -name) ns))
      (ns-unmap ns sym))))

#_(unload-namespace! 'new-kind-of-package)

(defn merge-package-command-maps
  "Returns a map containing all command mode key maps definined by a list of packages"
  [loaded-packages]
  (let [get-command-map-from-ns (fn [ns] (-> (nth ns 3) ;; ns metadata ;; using nth here will be buggy
                                             :command-map))
        list-of-all-command-maps (map get-command-map-from-ns loaded-packages)]
    (into {} list-of-all-command-maps)))
