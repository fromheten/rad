;; Swt front end
;; This namespace describes functions for displaying rad buffers in SWT
(ns rad.swt
  (:import [org.eclipse.swt SWT]
           [org.eclipse.swt.widgets Display Shell Text Listener]
           [org.eclipse.swt.layout GridLayout]
           [org.eclipse.swt.events ShellAdapter]))

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
        shell (Shell. display)
        text-area (Text. shell 2) ;; 2 Means multi li
        ]
    (create-shell display shell)
    ;;(.setBounds text-area 10 10 200 200)
    (.setSize shell 700 700)
    (.setBounds text-area (.getClientArea shell))
    (.setText text-area "Hello Jesper! \nThank you for helping me with Java :D")

    ;; listen to input
    (.addListener text-area (. SWT KeyDown)
                  (proxy [Listener] []   ;; extend Java class Listener
                    (handleEvent [event] ;; with method 'handleEvent'
                      (println "HELLO!!!!"))))

    (.open shell)
    (swt-loop display shell)))
