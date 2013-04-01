(ns zolo.marconi.facebook.core-test
  (:use [clojure.test :only [run-tests deftest is are testing]]
        zolo.utils.system
        zolo.utils.debug)
  (:require [zolo.marconi.facebook.core :as fb-lab]
            [zolo.marconi.core :as marconi]))

(deftest test-basic-fb-functionality
  (marconi/in-lab 
   (let [jack (fb-lab/create-user "Jack" "Mulder")
         jill (fb-lab/create-user "Jill" "Sculley")
         joe (fb-lab/create-user "Joe" "Conrad")]

     (fb-lab/make-friend jack jill)
     (fb-lab/make-friend jack joe)
     (is (= 2 (count (fb-lab/fetch-friends jack))))

     (let [dummy-friend (first (sort-by :first_name (fb-lab/fetch-friends jack)))]
       (are [expected attrib] (= expected (dummy-friend attrib))
            "Jill" :first_name
            "Sculley" :last_name))

     (fb-lab/send-message jack jill "1" "Hey Jill, I am going to fetch" "2012-05-01")
     (fb-lab/send-message jill jack "1" "Sure I will come" "2012-05-02")
     (fb-lab/send-message jack jill "1" "Great see you soon" "2012-05-03")
     (is (= 3 (count (fb-lab/fetch-messages jack)))))))


