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

(def app-id (system-env "SOCIAL_LAB_TEST_APP_ID"))
(def app-secret (system-env "SOCIAL_LAB_TEST_APP_SECRET"))

;;TOOD Need to add some asserts
(deftest ^:integration test-facebook-friends-integration
  (in-facebook-lab 
   app-id app-secret
   (let [jack (user/create "Jack")
         jill (user/create "Jill")]
     (login-as jack)
     (user/make-friend jill))))

(deftest ^:integration test-facebook-wall-posts-integration)

(deftest test-facebook-messages-integration
  (in-facebook-lab 
   app-id app-secret
   (let [jack (user/create "Jack")
         jill (user/create "Jill")]
     (login-as jack)
     (user/make-friend jill)
     (user/send-message jill "Hey Jill, I am going to fetch" "2012-05-01")
     (user/receive-message jill "Sure I will come"  "2012-05-01")
     (user/send-message jill "Great see you soon"  "2012-05-01")
     (is (= 3 (count (user/messages)))))))

