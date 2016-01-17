(ns rad.frontend.swt-test
  (:require [clojure.test :refer :all]
            [rad.frontend.swt :refer :all]
            [clojure.core.async :as a]))

(deftest initializing-swt
  (testing "getting a proper front-end"
    (let [swt-frontend (init-swt-frontend!)]
      (is (map? swt-frontend))
      (is (= 3
             (count (keys swt-frontend))))
      (is (= clojure.core.async.impl.channels.ManyToManyChannel
             (type (:in-chan swt-frontend))
             (type (:print-chan swt-frontend))
             (type (:point-chan swt-frontend)))))))
