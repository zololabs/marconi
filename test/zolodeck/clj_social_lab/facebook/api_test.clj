(ns zolodeck.clj-social-lab.facebook.api-test
  (:use [clojure.test :only [run-tests deftest is are testing]]
        zolodeck.clj-social-lab.facebook.api
        zolodeck.utils.system
        zolodeck.utils.debug))

(deftest temp-test
  (is (= 1 1)))