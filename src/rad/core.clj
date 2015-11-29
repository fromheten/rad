(ns rad.core
  (:gen-class))                         ;:gen-class here means this
                                        ;namespace will be compiled
                                        ;into a .class file if the aot
                                        ;task is run


(defn -main [& args]                    ;This -main function will
                                        ;become the main method if we
                                        ;set up this namespace to aot,
                                        ;and point jar -m at it
  (println "Varmt välkommen till rad"))
