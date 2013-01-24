(ns zolodeck.clj-social-lab.facebook.core
  (:use zolodeck.clj-social-lab.facebook.factory
        zolodeck.utils.debug
        zolodeck.utils.clojure))

(def ^:dynamic TEST-STATE)

(defn assoc-in-state! [key-seq value]
  (swap! TEST-STATE assoc-in key-seq value)
  value)

(defn update-in-state! [key-seq update-fn value]
  (swap! TEST-STATE #(update-in %1 key-seq update-fn value))
  value)

(defn append-in-state! [key-seq value]
  (update-in-state! key-seq conj value))

(defn get-from-state [key-seq]
  (get-in @TEST-STATE key-seq))

(defn get-user [id]
  (get-from-state [:users id]))

(defn login-as [user]
  (print-vals "Logging in as:" (:name user) "-" (:id user))
  (assoc-in-state! [:current-user] user))

(defn create-user [first-name last-name]
  (let [user (new-user first-name last-name)]
    (assoc-in-state! [:users (:id user)] user)))

(defn create-friend [first-name last-name]
  (-> (create-user first-name last-name)
      as-friend))

(defn update-user [id attribs-map]
  (->> attribs-map
       (merge (get-from-state [:users id]))
       (assoc-in-state! [:users id])))

(defn make-friend [{main-id :id :as main-user} {other-id :id :as other-user}]
  (print-vals "Making friend:" (:name (get-user main-id)) "<->" (:name (get-user other-id)))
  (append-in-state! [:friends (:id main-user)] (:id other-user))
  (append-in-state! [:friends (:id other-user)] (:id main-user)))

(defn fetch-friends [user]
  (print-vals "Fetching friends for" (:name user))
  (->> (get-from-state [:friends (:id user)])
       (map #(as-friend (get-from-state [:users %])))))

(defn send-message [from-user to-user thread-id message yyyy-mm-dd-string]
  (print-vals "Message on" yyyy-mm-dd-string "from" (:name from-user) "to" (:name to-user) ":" message)
  (let [msg (new-message from-user to-user thread-id message yyyy-mm-dd-string)]
    (append-in-state! [:messages (:id from-user)] msg)
    (append-in-state! [:messages (:id to-user)] msg)))

(defn fetch-messages [user]
  (print-vals "Fetching messages for" (:name user))
  (get-from-state [:messages (:id user)]))

(defn extended-user-info [user]
  (select-keys user [:uid :first_name :last_name :username :sex :birthday_date :locale :current_location :email :pic_small :pic_big :profile_url]))

(defn login-creds [user]
  {:providerLoginInfo
   {:authResponse
    {:userID (:id user)
     :accessToken (:access-token user)
     :expiresIn "5855"}
    :status "connected"}})

(defn dump-test-state []
  (print-vals "TEST-STATE:" @TEST-STATE))

(defmacro in-facebook-lab [& body]
  `(binding [TEST-STATE (atom {})]
     (do 
       ~@body)))