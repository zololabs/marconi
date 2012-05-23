(ns zolodeck.clj-social-lab.facebook.core-test
  (:use [clojure.test :only [run-tests deftest is are testing]]
        zolodeck.clj-social-lab.facebook.core
        zolodeck.utils.system
        zolodeck.utils.debug)
  (:require [zolodeck.clj-social-lab.facebook.api :as api]))

(deftest test-basic-fb-functionality
  (in-facebook-lab 
    (let [jack (create-user "Jack" "Mulder")
          jill (create-user "Jill" "Sculley")
          joe (create-user "Joe" "Conrad")]

      (make-friend jack jill)
      (make-friend jack joe)
      (is (= 2 (count (fetch-friends jack))))
      
      (send-message jack jill "1" "Hey Jill, I am going to fetch" "2012-05-01")
      (send-message jill jack "1" "Sure I will come" "2012-05-02")
      (send-message jack jill "1" "Great see you soon" "2012-05-03")
      (is (= 3 (count (fetch-messages jack))))

      (let [an-fb-user (first (api/all-users))

            an-fb-friend (first (api/friends-list an-fb-user))
            a-dummy-friend (first (fetch-friends jack))

            an-fb-message (first (api/fetch-messages an-fb-user))
            a-dummy-message (first (fetch-messages jack))]
        
        (is (= (sort (keys an-fb-user)) (sort (keys jack))))
        (is (= (sort (keys an-fb-friend)) (sort (keys a-dummy-friend))))
        (is (= (sort (keys an-fb-message)) (sort (keys a-dummy-message))))))))


