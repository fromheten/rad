(ns rad.state
  "Contains shared mutable state for rad. Call it `app-db'")

(def loaded-packages (atom []))
