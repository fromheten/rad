(ns rad.frontend.fx-test
  (:require [rad.frontend.fx :refer :all]
            [clojure.test :refer :all])
  (:import (javafx.scene.input KeyCode)))

(deftest rendering-buffers
  ;; In the future, add tests for multiple points.
  (testing "rendering a single char"
    (is (= (hiccup-char "r")
           [:text-flow [:text "r"]]))
    (is (= (hiccup-char "a" true)
           [:text-flow.point [:text.point "a"]])))
  (testing "rendering a single line with point"
    (is (= (hiccup-line "rad" 1)
           [:flow-pane
            [:text-flow [:text "r"]]
            [:text-flow.point [:text.point "a"]]
            [:text-flow [:text "d"]]])))
  (testing "convert a rad buffer to hiccup with background"
    (is (= (fx-hiccup ["Rad" "hack"] [1 1])
           [:v-box
            [:flow-pane
             [:text-flow [:text "R"]]
             [:text-flow [:text "a"]]
             [:text-flow [:text "d"]]]
            [:flow-pane
             [:text-flow [:text "h"]]
             [:text-flow.point [:text.point "a"]]
             [:text-flow [:text "c"]]
             [:text-flow [:text "k"]]]]))
    (testing "showing point even when it is outside of the text"
      (is (= (fx-hiccup ["rad"] [3 0])
             [:v-box [:flow-pane
                      [:text-flow [:text "r"]]
                      [:text-flow [:text "a"]]
                      [:text-flow [:text "d"]]
                      [:text-flow.point [:text.point " "]]]]))))
  (testing "rendering empty lines"
    (is (= (hiccup-char "")
           [:text-flow [:text ""]]))
    (is (= (hiccup-line "")
           [:flow-pane [:text-flow [:text " "]]]))
    (is (= (fx-hiccup ["Rad " "" "" "rocks!"] [0 1]) ;; Just like CIDER!
           [:v-box
            [:flow-pane
             [:text-flow [:text "R"]]
             [:text-flow [:text "a"]]
             [:text-flow [:text "d"]]
             [:text-flow [:text " "]]]
            [:flow-pane
             [:text-flow.point [:text.point " "]]]
            [:flow-pane
             [:text-flow [:text " "]]]
            [:flow-pane
             [:text-flow [:text "r"]]
             [:text-flow [:text "o"]]
             [:text-flow [:text "c"]]
             [:text-flow [:text "k"]]
             [:text-flow [:text "s"]]
             [:text-flow [:text "!"]]]]))))

(deftest input
  (testing "converting JavaFX input object into a rad char"
    (is (= \a
           (fx-keydown->rad-char {:text "a"
                                  :to-string "KeyEvent [source = javafx.scene.Scene@4c32ba1c, target = javafx.scene.Scene@4c32ba1c, eventType = KEY_PRESSED, consumed = false, character = , text = A, code = A, shiftDown]"
                                  :character ""
                                  :alt-down? false
                                  :control-down? false
                                  :is-meta-down false
                                  :shift-down? false
                                  :shortcut-down? false})))
    (is (= \A
           (fx-keydown->rad-char {:text "A"
                                  :to-string "KeyEvent [source = javafx.scene.Scene@4c32ba1c, target = javafx.scene.Scene@4c32ba1c, eventType = KEY_PRESSED, consumed = false, character = , text = A, code = A, shiftDown]"
                                  :character ""
                                  :alt-down? false
                                  :control-down? false
                                  :is-meta-down false
                                  :shift-down? true
                                  :shortcut-down? false})))
    (is (= :tab
           (fx-keydown->rad-char {:text "\t"
                                  :to-string "KeyEvent [source = javafx.scene.Scene@b1a43d2, target = javafx.scene.Scene@b1a43d2, eventType = KEY_PRESSED, consumed = false, character = , text = \t, code = TAB]"
                                  :character ""
                                  :alt-down? false
                                  :control-down? true
                                  :is-meta-down false
                                  :shift-down? true
                                  :shortcut-down? true})))
    (is (= \space
           (fx-keydown->rad-char {:text " "
                                  :to-string "KeyEvent [source = javafx.scene.Scene@b1a43d2, target = javafx.scene.Scene@b1a43d2, eventType = KEY_PRESSED, consumed = false, character = , text =  , code = SPACE]"
                                  :character ""
                                  :alt-down? true
                                  :control-down? false
                                  :meta-down? false
                                  :shift-down? false
                                  :shortcut-down? false})))
    (is (= :back_space
           (fx-keydown->rad-char {:text ""
                                  :to-string "KeyEvent [source = javafx.scene.Scene@1864ff48, target = javafx.scene.Scene@1864ff48, eventType = KEY_PRESSED, consumed = false, character = , text = , code = BACK_SPACE]"
                                  :character ""
                                  :code "BACK_SPACE"
                                  :alt-down? false
                                  :control-down? false
                                  :meta-down? false
                                  :shift-down? true
                                  :shortcut-down? true})))
    (is (= :enter
           (fx-keydown->rad-char {:text ""
                                  :to-string "KeyEvent [source = javafx.scene.Scene@67951658, , code = ENTER]"
                                  :character ""
                                  :code "ENTER"
                                  :alt-down? false
                                  :control-down? false
                                  :meta-down? false
                                  :shift-down? false
                                  :shortcut-down? false})))
    (is (= :shift
           (fx-keydown->rad-char {:text ""
                                  :to-string "KeyEvent [source = javafx.scene.Scene@1b4e4710, target = javafx.scene.Scene@1b4e4710, eventType = KEY_PRESSED, consumed = false, character = , text = , code = SHIFT, shiftDown]"
                                  :character ""
                                  :code "SHIFT"
                                  :alt-down? false
                                  :control-down? false
                                  :meta-down? false
                                  :shift-down? true
                                  :shortcut-down? false})))))
