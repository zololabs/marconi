clj-social-lab
================
clj-social-lab is a clojure library that will help you automate integration tests with social networks. 

To start with, this library only integrates with facebook.

Currently you can ,

* <b>Create Facebook Test Users</b>
* <b>Delete Facebook Test Users</b>
* <b>Delete All Facebook Test Users</b>
* <b>Make Facebook Test Users friends</b>

This library also comes with some testing utilities that make it easy for you to write integration level tests.

For example, this is an integration tests that tests friends-list functionality using clj-social-lab,

```clj
(deftest ^:integration test-friends-list
  (in-facebook-lab FB-APP-ID FB-APP-SECRET
   (let [jack (test-user/create "jack")
         jill (test-user/create "jill")
         mary (test-user/create "mary")]
     (login-as jack)
     (test-user/make-friend jill)
     (test-user/make-friend mary)

     (let [friends-of-jack (gateway/friends-list (test-user/access-token))]
       (is (= 2 (count friends-of-jack)))
       (is (some #(= "jill" (:first_name %)) friends-of-jack))
       (is (some #(= "mary" (:first_name %)) friends-of-jack))))))
```
<b>in-facebook-lab :</b> This macros takes care of deleting all test users that are created as part of this test. So you do not end up with too many facebook test users. In fact, facebook has an hard limit of 500 test users per application.
       
This is just the start. I will be adding more functionalities when need arises in my project or when there is enough demand for a functionality.

Installation
------------

Add clj-social-lab to your leiningen project file, as a dev-dependency::

```clj
[zolodeck/clj-social-lab "1.0.0-SNAPSHOT"]
```