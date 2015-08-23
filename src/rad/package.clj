(ns rad.package)
(use '[clojure.java.io])

(def global-package-list
  "For now this is just a global atom. In the future there will be one for each window"
  (atom {}))

(defn package?
  "Poor mans type... Returns true if package is a valid rad package.
Check if package contains every key in `required keys'"
  [package]
  (let [required-keys [:author :doc-string :name]]
    (every? true?
            (map (fn [required-key] (contains? package required-key)) required-keys))))

(defn load-package!
  "Takes a package and registers it with the running rad session"
  [name-keyword package]
  {:pre [package?]}
  (swap! global-package-list assoc name-keyword package))

(defn unload-package! [name]
  (swap! global-package-list dissoc name))

(defn defpackage
  "Defines a package and adds it into the running rad session.
  Secretly converts from a fancy defn-style macro to a simple hash-map"
  [name doc-string package-map]
  (println (str "Loading package: " name))
  (let [name-keyword (keyword name)
        merged-package-map (merge package-map {:name (str name)
                                               :doc-string doc-string})]
    (load-package!
     name-keyword
     merged-package-map)
    (println (str "Current packages: " (keys @global-package-list)))))

(defn load-user-packages! []
  (let [walk (fn [dirpath pattern]
               (doall (filter #(re-matches pattern (.getName %))
                              (file-seq (file dirpath)))))
        plugin-files (walk
                      (str (System/getenv "HOME") "/.rad/packages/")
                      #".*\.clj")
        file-path (fn [file] (-> file .getAbsolutePath))
        all-plugins (map file-path plugin-files)]
    (map load-file all-plugins)))
(load-user-packages!)

(defn get-input-fns-from-package-list
  "Given a package-list, return a vector of every input-fn in the package-list"
  [package-list]
  (let [extract-input-fn (fn [package]
                           (get package :input-fn))]
    (map extract-input-fn (vals package-list))))

(defn run-thru-list-of-fns [input list-of-fns]
  (let [list-of-fns (filter fn? list-of-fns)]
    (doseq [f list-of-fns]
     (f input))))
