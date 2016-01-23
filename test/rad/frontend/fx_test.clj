(ns rad.frontend.fx-test
  (:require [rad.frontend.fx :refer :all]
            [clojure.test :refer :all])
  (:import (javafx.scene.input KeyCode)))

(deftest rendering-buffers
  (testing "converting a rad buffer to fx hiccup"
    (is (= (fx-hiccup ["Rad is meant" "to be hacked!"])
           [:text-flow
            [:text "Rad is meant"]
            [:text "\n"]
            [:text "to be hacked!"]
            [:text "\n"]]))))

(deftest input
  (testing "converting JavaFX input object into a rad char"
    (is (= \a
           (fx-keydown->rad-char {:text "a"
                                  :to-string "KeyEvent [source = javafx.scene.Scene@4c32ba1c, target = javafx.scene.Scene@4c32ba1c, eventType = KEY_PRESSED, consumed = false, character = , text = A, code = A, shiftDown]"
                                  :character ""
                                  :alt-down? false
                                  :is-control-down false
                                  :is-meta-down false
                                  :is-shift-down false
                                  :is-shortcut-down false})))
    (is (= \A
           (fx-keydown->rad-char {:text "A"
                                  :to-string "KeyEvent [source = javafx.scene.Scene@4c32ba1c, target = javafx.scene.Scene@4c32ba1c, eventType = KEY_PRESSED, consumed = false, character = , text = A, code = A, shiftDown]"
                                  :character ""
                                  :alt-down? false
                                  :is-control-down false
                                  :is-meta-down false
                                  :is-shift-down true
                                  :is-shortcut-down false})))
    (is (= :tab
           (fx-keydown->rad-char {:text "\t"
                                  :to-string "KeyEvent [source = javafx.scene.Scene@b1a43d2, target = javafx.scene.Scene@b1a43d2, eventType = KEY_PRESSED, consumed = false, character = , text = \t, code = TAB]"
                                  :character ""
                                  :alt-down? false
                                  :is-control-down true
                                  :is-meta-down false
                                  :is-shift-down true
                                  :is-shortcut-down true})))
    (is (= \space
           (fx-keydown->rad-char {:text " "
                                  :to-string "KeyEvent [source = javafx.scene.Scene@b1a43d2, target = javafx.scene.Scene@b1a43d2, eventType = KEY_PRESSED, consumed = false, character = , text =  , code = SPACE]"
                                  :character ""
                                  :alt-down? true
                                  :is-control-down false
                                  :is-meta-down false
                                  :is-shift-down false
                                  :is-shortcut-down false})))
    (is (= :back_space
           (fx-keydown->rad-char {:text ""
                                  :to-string "KeyEvent [source = javafx.scene.Scene@1864ff48, target = javafx.scene.Scene@1864ff48, eventType = KEY_PRESSED, consumed = false, character = , text = , code = BACK_SPACE]"
                                  :character ""
                                  :code "BACK_SPACE"
                                  :alt-down? false
                                  :is-control-down false
                                  :is-meta-down false
                                  :is-shift-down true
                                  :is-shortcut-down true})))
    (is (= :enter
           (fx-keydown->rad-char {:text ""
                                  :to-string "KeyEvent [source = javafx.scene.Scene@67951658, , code = ENTER]"
                                  :character ""
                                  :code "ENTER"
                                  :alt-down? false
                                  :is-control-down false
                                  :is-meta-down false
                                  :is-shift-down false
                                  :is-shortcut-down false})))
    (is (= :shift
           (fx-keydown->rad-char {:text ""
                                  :to-string "KeyEvent [source = javafx.scene.Scene@1b4e4710, target = javafx.scene.Scene@1b4e4710, eventType = KEY_PRESSED, consumed = false, character = , text = , code = SHIFT, shiftDown]"
                                  :character ""
                                  :code "SHIFT"
                                  :alt-down? false
                                  :is-control-down false
                                  :is-meta-down false
                                  :is-shift-down true
                                  :is-shortcut-down false})))))
