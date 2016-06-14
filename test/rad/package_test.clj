(ns rad.package-test
  (:require [clojure.test :refer :all]
            [rad.package :refer :all]
            [rad.state]))

(def example-packages ['[(ns new-kind-of-package
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
                            :command-map '{\s (fn [] (new-kind-of-package/rad-name "tester"))
                                           \r {\a (fn [] (println "it works!"))}}})
                         (defn rad-name [name] (str "hello " name))]

                       '[(ns another-example-package
                           "Another example"
                           {:command-map '{\e (fn [] "2: pretty neat")
                                           \r (fn [] "2: it still works")}})]])

(deftest package?-predicate
  (testing "Valid packages are seen as such"
    (is (package? '[(ns test-package
                      "A test package, to be loaded from the file system"
                      {:command-map '{\s (fn [] "Running a function from a package on disk")
                                      \g (fn [] (say-hi "tester"))}})
                    (defn say-hi [name]
                      (str "hello " name))]))
    (is (not (package? '(ns test-package "string" {:map (fn [] "func")}))))))

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
    (is (map? (merge-package-command-maps example-packages)))
    (is (map char? (keys (merge-package-command-maps example-packages))))
    (is (= #{\e \r \s}
           (set (keys (merge-package-command-maps example-packages)))))
    (is (= "hello" ((fn [] "hello"))))
    (is (= "2: pretty neat"
           ((eval (get-in (merge-package-command-maps example-packages) [\e])))
           ((get-in-evaled (merge-package-command-maps example-packages) [\e])))))

  (testing "Loading a package from a file, and running a function from it."
    (testing "Loading a package from a file"
      (is (package? (get-package-from-file "./test_packages/test_package.clj"))))
    (testing "Evaliating a function from its key-map"
      (reset! rad.state/loaded-packages [])
      (load-package! (get-package-from-file "./test_packages/test_package.clj"))
      (is (= "Running a function from a package on disk"
             ((get-in-evaled
               (merge-package-command-maps @rad.state/loaded-packages)
               [\s]))))))
  (testing "getting a list of packages from directory"
    (is (map package? (get-all-packages-in-dir "./test_packages/"))))

  (testing "Loading all packages in a directory"
    (is (= (do (reset! rad.state/loaded-packages [])
               (load-all-packages-in-dir! "./test_packages/")
               (count @rad.state/loaded-packages))
           (count (clojure-files-in-dir "./test_packages/")))))

  (testing "Loading package with command that uses fn in rad.package"
    (is (= (do (reset! rad.state/loaded-packages [])
               (load-package!
                '[(ns package-that-uses-rad.package-ns
                    "Let's see if my implementation is worth anything..."
                    {:command-map '{\p (fn [] (do (require 'rad.package)
                                                  (rad.package/get-in-evaled
                                                   {:it "works"} [:it])))}})])
               ((get-in-evaled
                 (merge-package-command-maps @rad.state/loaded-packages) [\p])))
           "works")))
  (reset! rad.state/loaded-packages []))
