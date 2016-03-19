(ns deletion-commands
  "Adds commands for deletion of text"
  {:command-map '{\d {\d (fn [] (rad.buffer/delete-line! @rad.point/point))
                      \h (fn [] (do (rad.buffer/delete-char-backwards! @rad.point/point)
                                    (rad.point/move-point-backwards! 1)))}}})
