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

;; Save the 'display' object, so that it is accessible from the repl & more
(def display-ref (ref nil))

(defn begin []
  (let [display (Display.)]

    ;; We will want to modify the SWT display object later, so we save it. It's Java
    (dosync
     (ref-set display-ref display))

    (let [shell (Shell. display)]
      (create-shell display shell)
      (.setSize shell 700 700)
      (.open shell)
      (swt-loop display shell))))

;; Credit for this code got to Kevin Albrecht. Thank you! (or tack så mycket!)
(defn start-gui-threaded []
  "Launch the GUI in a new thread"
  (let [thread (Thread. begin)]
    (.start thread)
    thread))

;; This macro executes its body in the thread above. It simplifies REPL debugging.
(defmacro exec [& body]
  "Run `body` inside the thread of display-ref"
  `(.syncExec (deref display-ref)
              (reify Runnable
                (run [this]
                  ~@body))))

(defmacro pexec [& body]
  "Print the result of `body` inside the thread of display-ref"
  `(.syncExec (deref display-ref)
              (reify Runnable
                (run [this]
                  (println ~@body)))))
