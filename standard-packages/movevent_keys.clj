(ns movement-keys
  "adds commands for moving the point"
  {:command-map '{\h rad.point/move-point-backwards!
                  \j rad.point/move-point-down!
                  \k rad.point/move-point-up!
                  \l rad.point/move-point-forward!}})
