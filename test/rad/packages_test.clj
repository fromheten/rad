(ns rad.package-test
  (:require [clojure.test :refer :all]
            [rad.package :refer :all]
            [rad.state]))
(def example-packages ['(ns new-kind-of-package
                          "A prototype for how a package can look"

                          {:author {:name "Martin Josefsson"
                                    :url "http://www.martinjosefsson.com"
                                    :email "hello@martinjosefsson.com"}
                           :people [{:name "Jesper Oskarsson"
                                     :url "http://github.com/redien/reuse-lang"
                                     :email "jesosk@gmail.com"}]
                           :version "1.0.0"
                           :repository "https://github.com/fromheten/rad/"
                           :keywords #{:test :package}
                           :homepage "http://www.example.org/"
                           :bugs "http://www.example.org/bug-reports"
                           :license "GPLv3"
                           :bin {:ls "/bin/ls"}
                           :dependencies [{:foo-package "2.3.4"}] ;; These packages will be loaded first
                           :rad-version "0.4.3"

                           :command-map '{\s (fn [] (clojure.core/println "hello"))
                                          \r (fn [] "it works!")}})
                       '(ns another-example-package
                          "Another example"
                          {:command-map '{\e (fn [] "2: pretty neat")
                                         \r (fn [] "2: it still works")}})])
(deftest package-loading
  (testing "Loading a package."
    ;; The format we save this list in is just a vector
    (is (= (do (reset! rad.state/loaded-packages [])
               (load-package! (first example-packages))
               (load-package! (second example-packages))
               @rad.state/loaded-packages)
           example-packages)))
  (testing "getting the command-mode key-map for a package."
    (= '(\s \r)
       (keys (get-key-map-from-package (first example-packages)))))

  (testing "Merging all command maps in a collection of packages."
    (let [merged-command-maps (merge-package-command-maps example-packages)]
      (is (map? merged-command-maps))
      (is (char? (first (keys merged-command-maps))))
      (is (= #{\e \r \s}
             (set (keys merged-command-maps))))
      (is (= "hello"                    ;for my understanding
             ((fn [] "hello"))))
      (is (= "2: pretty neat"
             ((eval (get-in merged-command-maps [\e])))
             ((get-in-evaled merged-command-maps [\e]))))))

  (testing "Loading a package from a file, and running a function from it."
    (testing "Loading a package from a file"
      (reset! rad.state/loaded-packages [])
      (load-package-from-file! "./test/test-package.clj")
      (is (= 1
             (count @rad.state/loaded-packages))))
    (testing "Evaliating a function from its key-map"
      (reset! rad.state/loaded-packages [])
      (load-package-from-file! "./test/test-package.clj")
      (is (= "Running a function from a package on disk"
             ((get-in-evaled (merge-package-command-maps @rad.state/loaded-packages) [\s]))))
      (is (= "hello tester"
             ((get-in-evaled (merge-package-command-maps @rad.state/loaded-packages) [\g]))))))

  (reset! rad.state/loaded-packages []))
