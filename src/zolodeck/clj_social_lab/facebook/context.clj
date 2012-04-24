(ns zolodeck.clj-social-lab.facebook.context
  (:use 
   zolodeck.clj-social-lab.utils.debug
   zolodeck.clj-social-lab.utils.core))

(def ^:dynamic APP-ID)
(def ^:dynamic APP-ACCESS-TOKEN)

(def ^:dynamic CURRENT-USER (atom nil))

(defn current-user [] @CURRENT-USER)

(def DEFAULT-PERMISSIONS "email,friends_about_me,friends_birthday,friends_relationship_details,friends_location,friends_likes,friends_website,read_mailbox,offline_access")



