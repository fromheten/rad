(ns deletion-commands
  "Adds commands for deleting lines, characters etc"
  {:command-map '{\d {\d (fn [] (rad.buffer/delete-line! @rad.state/point))
                      \h (fn [] (rad.buffer/delete-char-backwards! @rad.state/point))}}})
