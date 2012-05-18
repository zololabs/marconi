(ns zolodeck.clj-social-lab.facebook.user-test
  (:use [clojure.test :only [run-tests deftest is are testing]]
        zolodeck.clj-social-lab.facebook.core
        zolodeck.utils.system
        zolodeck.utils.debug))

(def app-id (system-env "SOCIAL_LAB_TEST_APP_ID"))
(def app-secret (system-env "SOCIAL_LAB_TEST_APP_SECRET"))

(deftest test-basic-fb-functionality
  (in-facebook-lab app-id app-secret
    (let [jack (create-user "Jack")
          jill (create-user "Jill")]
      (login-as jack)
      (make-friend jack jill)
      (send-message jack jill "Hey Jill, I am going to fetch" "2012-05-01")
      (send-message jill jack "Sure I will come" "2012-05-02")
      (send-message jack jill "Great see you soon" "2012-05-03")
      (print-vals "Jack's messages:" (fetch-messages jack))
      (is (= 3 (count (fetch-messages jack)))))))

