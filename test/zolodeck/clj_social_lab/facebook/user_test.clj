(ns zolodeck.clj-social-lab.facebook.user-test
  (:use [clojure.test :only [run-tests deftest is are testing]]
        zolodeck.clj-social-lab.facebook
        zolodeck.utils.system)
  (:require [zolodeck.clj-social-lab.facebook.user :as user]))

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


;; (deftest test-delete-all
;;   (init-facebook-lab)
;;   (user/delete-all)
;;   (user/create "Jack")
;;   (user/create "Jill"))

(def app-id (system-env "APP_ID"))
(def app-secret (system-env "APP_SECRET"))

(deftest ^:integration test-facebook-friends-integration
  (in-facebook-lab 
   app-id app-secret
   (let [jack (user/create "Jack")
         jill (user/create "Jill")]
     (login-as jack)
     (user/make-friend jill))))

(deftest test-facebook-wall-posts-integration)

(deftest test-facebook-messages-integration)

