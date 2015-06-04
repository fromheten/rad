;; Swt front end
;; This namespace describes functions for displaying rad buffers in SWT
(ns rad.swt
  (:import (org.eclipse.swt.widgets Display Shell)
           (org.eclipse.swt.layout GridLayout)
           (org.eclipse.swt.events ShellAdapter)))

(defn create-shell [display shell]
  (let [layout (GridLayout.)]
    (doto shell
      (.setText "rad")
      (.setLayout layout)
      (.addShellListener
       (proxy [ShellAdapter] []
         (shellClosed [evt]
           (System/exit 0)))))))

(defn swt-loop [display shell]
  (loop []
    (if (.isDisposed shell)
      (.dispose display)
      (do
        (if (not (.readAndDispatch display))
          (.sleep display))
        (recur)))))

(defn begin []
  (let [display (Display.)
        shell (Shell. display)]
    (create-shell display shell)
    (.setSize shell 700 700)
    (.open shell)
    (swt-loop display shell)))
