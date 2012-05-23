(ns zolodeck.clj-social-lab.facebook.factory
  (:use zolodeck.utils.clojure
        zolodeck.utils.calendar))

(defn as-friend [user]
  (let [id (:id user)]
    {:gender (:gender user)
     :last_name (:last-name user)
     :link (str "http://www.facebook.com/profile.php?id=" id),
     :installed true,
     :locale "en_US",
     :first_name (:first-name user)
     :id id
     :birthday "08/08/1980",
     :picture (str "http://profile.ak.fbcdn.net/static-ak/rsrc.php/v1/yo/r/" id ".gif")}))

(defn new-message [from-user to-user thread-id message yyyy-mm-dd-string]
  {:attachment []
   :author_id (parse-int (:id from-user))
   :body  message
   :created_time (.getTime (date-string->instant "yyyy-MM-dd" yyyy-mm-dd-string))
   :message_id (str (random-integer))
   :thread_id thread-id
   :viewer_id (:id to-user)})