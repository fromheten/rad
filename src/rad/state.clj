(ns rad.state
  "Contains shared state for rad.")

(def loaded-packages (atom []))

(def config (atom {:should-exit? false}))
