;; Swt front end
;; This namespace describes functions for displaying rad buffers in SWT
(ns rad.swt
  (:require [clojure.core.async :as async :refer [<!!]])
  (:import [org.eclipse.swt SWT]
           [org.eclipse.swt.widgets Display Shell Listener]
           [org.eclipse.swt.custom StyledText]
           [org.eclipse.swt.layout GridLayout FillLayout]
           [org.eclipse.swt.graphics Font]
           [org.eclipse.swt.events ShellAdapter]))

(defn create-shell [display shell]
  (let [layout (FillLayout.)]
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

(defn begin [input-chan text-to-display-chan]
  (let [display (Display.)
        shell (Shell. display)
        text-area (org.eclipse.swt.custom.StyledText. shell (. SWT BORDER))]
    (create-shell display shell)
    ;;(.setBounds text-area 10 10 200 200)
    (.setSize shell 700 700)
    (.setBounds text-area (.getClientArea shell))
    (.setFont text-area (Font. display "Courier" 14 (. SWT NORMAL)))
    (.setText text-area (str (<!! text-to-display-chan)))

    ;; listen to input
    (.addListener text-area (. SWT KeyDown)
                  (proxy [Listener] []   ;; extend Java class Listener
                    (handleEvent [event] ;; with method 'handleEvent'
                      (println (str "keydown: " (.keyCode event))))))
    (.open shell)
    (swt-loop display shell)))
