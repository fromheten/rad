(ns rad.package-test
  (:require [clojure.test :refer :all]
            [rad.package :refer :all]
            [rad.mode]))

(def example-package '(ns new-kind-of-package
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

                        :command-map {\s #("this package works!")}
                        :indent-buffer (fn [buffer] buffer) ;;Shitty indentation
                        }))
(def another-example-package '(ns another-new-kind-of-package
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
                        :rad-version "0.0.0"

                        :command-map {\l #("this package works as well!")}
                        :indent-buffer (fn [buffer] buffer) ;;Shitty indentation
                        }))

(def mock-active-packages (atom [example-package another-example-package]))

(deftest testing-tests
  (testing "That the test system is set up"
    (is (= 1 1))))

(deftest loading-packages
  (testing "command mode uses commands from packages key maps"
    (let [merged-command-maps (merge-package-command-maps @mock-active-packages)]

      (is (map? merged-command-maps))
      (is (char? (first (keys merged-command-maps))))
      #_(is (= {\s #("this package works as well!") ; = does not work for comparing functions)
                \l #("this package works!")}        ; so don't eval this. It's only documentation.
               merge-package-command-maps)))))

#_(merge-package-command-maps @mock-active-packages)
