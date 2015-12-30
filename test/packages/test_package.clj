(ns test-package
  "A test package, to be loaded from the file system"
  {:command-map '{\s (fn [] "Running a function from a package on disk")
                  \g (fn [] (test-package/say-hi "tester"))}})

(defn say-hi [name]
  (str "hello " name))
