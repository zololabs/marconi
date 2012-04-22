(ns zolodeck.clj-facebook-lab.test.example-test
  (:use [clojure.test :only [run-tests deftest is are testing]]
        zolodeck.clj-facebook-lab.facebook)
  (:require [zolodeck.clj-facebook-lab.facebook.test-user :as test-user]))

  ;; init-facebook-lab
  ;; user1 = test-user/create
  ;; user2 = test-user/create
  ;; login-as user1
  ;; make-friends(user2)
  ;; post-in-wall(user2 "message")
  ;; login-as user2
  ;; friends(user2) . should have user1
  ;; news-feed(user2). should have user2 message
  ;; message(user1 , "hey")
  ;; login-as user1
  ;; read-messages(user1)
  ;; messages .should have user2 message
  ;; message(user2 "I am good how are you")
  ;; login-as user2 
  ;; messages = read-messages
  ;; messages should have user2 message
  ;; delete-all

(deftest test-facebook-friends-integration
  (init-facebook-lab "287471481334573" "48741a4a0358763b0649459772f53cc4")
  )

(deftest test-facebook-wall-posts-integration)

(deftest test-facebook-messages-integration)

