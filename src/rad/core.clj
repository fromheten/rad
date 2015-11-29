(ns rad.core
  (:gen-class))                         ;:gen-class here means this
                                        ;namespace will be compiled
                                        ;into a .class file if the aot
                                        ;task is run

(require '[rad.frontend.terminal :as term])
(defn init-terminal-frontend! []
  (term/init-terminal! term/scr)
  (term/render-buffer! ["Rad is meant" "to be hacked"] term/scr))

(defn -main [& args]                    ;This -main function will
                                        ;become the main method if we
                                        ;set up this namespace to aot,
                                        ;and point jar -m at it
  (println "Varmt välkommen till rad")
  (init-terminal-frontend!))
