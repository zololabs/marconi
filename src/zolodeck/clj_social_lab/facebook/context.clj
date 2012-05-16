(ns zolodeck.clj-social-lab.facebook.context
  (:use zolodeck.utils.debug
        zolodeck.utils.calendar))

(def ^:dynamic APP-ID)
(def ^:dynamic APP-ACCESS-TOKEN)

(def ^:dynamic CURRENT-USER (atom nil))

(def ^:dynamic ALL-USERS (atom {}))

(def ^:dynamic MESSAGES (atom []))

(defn current-user [] @CURRENT-USER)

(def DEFAULT-PERMISSIONS "email,friends_about_me,friends_birthday,friends_relationship_details,friends_location,friends_likes,friends_website,read_mailbox,offline_access")

(defn reset-users! []
  (reset! ALL-USERS {}))

(defn add-user! [user]
  (swap! ALL-USERS merge {(:id user) user})
  user)

(defn remove-user! [user]
  (swap! ALL-USERS dissoc (:id user))
  user)

(defn all-users []
  (vals @ALL-USERS))

(defn add-message! [from to message yyyy-mm-dd]
  (let [message-map {:from from
                     :to to
                     :message message
                     :timestamp (date-string->instant :date yyyy-mm-dd)}]
    (swap! MESSAGES conj message-map)))

(defn clear-messages! []
  (print-vals "Clearing All Messages")
  (reset! MESSAGES []))

(defn messages []
  @MESSAGES)






