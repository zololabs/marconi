(ns zolo.marconi.facebook.core-test
  (:use [clojure.test :only [run-tests deftest is are testing]]
        zolo.marconi.facebook.core
        zolodeck.utils.system
        zolodeck.utils.debug))

(deftest test-basic-fb-functionality
  (in-facebook-lab 
   (let [jack (create-user "Jack" "Mulder")
         jill (create-user "Jill" "Sculley")
         joe (create-user "Joe" "Conrad")]

     (make-friend jack jill)
     (make-friend jack joe)
     (is (= 2 (count (fetch-friends jack))))

     (let [dummy-friend (first (sort-by :first_name (fetch-friends jack)))]
       (are [expected attrib] (= expected (dummy-friend attrib))
            "Jill" :first_name
            "Sculley" :last_name))

     (send-message jack jill "1" "Hey Jill, I am going to fetch" "2012-05-01")
     (send-message jill jack "1" "Sure I will come" "2012-05-02")
     (send-message jack jill "1" "Great see you soon" "2012-05-03")
     (is (= 3 (count (fetch-messages jack)))))))


