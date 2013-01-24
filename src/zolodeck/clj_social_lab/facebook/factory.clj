(ns zolodeck.clj-social-lab.facebook.factory
  (:use zolodeck.utils.clojure
        zolodeck.utils.calendar))

(defn new-user 
  ([first-name last-name]
     (let [id (str (random-integer))
           gender (select-randomly "female" "male")
           user {:gender gender
                 :last_name last-name
                 :link (str "http://www.facebook.com/profile.php?id=" id)
                 :email (str first-name "." last-name "@gmail.com")
                 :timezone (select-randomly -1 -2 -3 -4 -5 -6 -7 -8 -9 -10 -11)
                 :name (str first-name " " last-name)
                 :locale "en_US"
                 :updated_time "2012-05-21T04:50:43+0000"
                 :first_name first-name
                 :id id
                 :uid id
                 :access-token (str (random-guid))
                 :username id
                 :sex gender
                 :birthday_date "4/17/1976"
                 :current_location {:country "USA" :state "CA" :city "Foster City" :zip "94404"}
                 :pic_small (str "http://www.facebook.com/small" id ".png")
                 :pic_big (str "http://www.facebook.com/big " id ".png")
                 :profile_url (str "http://www.facebook.com/profile.php?id=" id)}]
       user))
  ([]
     (new-user (str (random-guid)) (str (random-guid)))))

(defn as-friend [user]
  (let [id (:id user)]
    {:gender (:gender user)
     :last_name (:last_name user)
     :link (str "http://www.facebook.com/profile.php?id=" id),
     :installed true,
     :locale "en_US",
     :first_name (:first_name user)
     :id id
     :username (str "username-" id)
     :birthday (:birthday_date user),
     :picture {:data {:url (str "http://profile.ak.fbcdn.net/static-ak/rsrc.php/v1/yo/r/" id ".gif")}}}))

(defn as-friends [users]
  (map as-friend users))

(defn new-message [from-user to-user thread-id message yyyy-mm-dd-string]
  {:attachment []
   :author_id (:id from-user)
   :body  message
   :created_time (.getTime (date-string->instant "yyyy-MM-dd" yyyy-mm-dd-string))
   :message_id (str (random-integer))
   :thread_id thread-id
   :subject "fake thread subject"
   :to [(:id to-user)]})

(defn sample-friends [n]
  (repeatedly n #(as-friend (new-user ))))