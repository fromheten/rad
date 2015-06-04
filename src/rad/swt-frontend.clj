;; Swt front end
;; This namespace describes functions for displaying rad buffers in SWT
(ns rad.swt-frontend
  (:import (org.eclipse.swt.widgets Display Shell)
           (org.eclipse.swt.layout GridLayout)
           (org.eclipse.swt.events ShellAdapter)))

(defn say-hi []
  (println "Hello"))
