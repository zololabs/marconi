(ns zolo.marconi.facebook.core
  (:use zolo.marconi.facebook.factory
        zolo.marconi.debug
        zolo.utils.clojure)
  (:require [zolo.marconi.test-state :as state]
            [zolo.utils.calendar :as zcal]))

(defn get-user [id]
  (state/get-from-state [:FACEBOOK :users id]))

(defn login-as [user]
  (print-debug "Logging in as:" (:name user) "-" (:id user))
  (state/assoc-in-state! [:FACEBOOK :current-user] user))

(defn current-user []
  (state/get-from-state [:FACEBOOK :current-user]))

(defn create-user [first-name last-name]
  (let [user (new-user first-name last-name)]
    (state/assoc-in-state! [:FACEBOOK :users (:id user)] user)))

(defn create-friend [first-name last-name]
  (-> (create-user first-name last-name)
      as-friend))

(defn create-friends [prefix n]
  (for [i (range 1 (inc n))]
    (create-friend (str "first-name-" prefix "-" i) (str "last-name-" prefix "-" i))))

(defn update-user [id attribs-map]
  (->> attribs-map
       (merge (state/get-from-state [:FACEBOOK :users id]))
       (state/assoc-in-state! [:FACEBOOK :users id])))

(defn update-friend [id attribs-map]
  (-> (update-user id attribs-map)
      as-friend))

(defn make-friend [{main-id :id :as main-user} {other-id :id :as other-user}]
  (print-debug "Making friend:" (:name (get-user main-id)) "<->" (:name (get-user other-id)))
  (state/append-in-state! [:FACEBOOK :friends (:id main-user)] (:id other-user))
  (state/append-in-state! [:FACEBOOK :friends (:id other-user)] (:id main-user)))

(defn unfriend [{main-id :id :as main-user} {other-id :id :as other-user}]
  (print-debug "Unfriending :" (:name (get-user main-id)) "<->" (:name (get-user other-id)))
  (state/remove-from-state! [:FACEBOOK :friends (:id main-user)] (:id other-user))
  (state/remove-from-state! [:FACEBOOK :friends (:id other-user)] (:id main-user)))

(defn fetch-friends [user]
  (let [results (->> (state/get-from-state [:FACEBOOK :friends (:id user)])
                     (map #(as-friend (state/get-from-state [:FACEBOOK :users %]))))]
    (print-debug "Fetching" (count results) "friends for" (:name user))
    results))

(defn send-message [from-user to-user thread-id message yyyy-mm-dd-HH-mm-string]
  (print-debug "Message on" yyyy-mm-dd-HH-mm-string "from" (:name from-user) "to" (:name to-user) ":" message)
  (let [msg (new-message from-user to-user thread-id message yyyy-mm-dd-HH-mm-string)]
    (state/append-in-state! [:FACEBOOK :messages (:id from-user)] msg)
    (state/append-in-state! [:FACEBOOK :messages (:id to-user)] msg)
    msg))

(defn remove-all-messages [user]
  (state/assoc-in-state! [:FACEBOOK :messages (:id user)] []))

(defn fetch-messages
  ([user]
     (fetch-messages user (zcal/to-seconds "1970-01-01")))
  ([user date-since-seconds]
     (print-debug "Fetching messages for" (:name user))
     (->> (state/get-from-state [:FACEBOOK :messages (:id user)])
          (filter (fn [m] (> (:created_time m) date-since-seconds))))))

(defn post-to-wall [from-user to-user post-message yyyy-mm-dd-string]
  (print-debug "Post on" yyyy-mm-dd-string "from" (:name from-user) "to" (:name to-user) ":" post-message)
  (let [post (new-post from-user to-user post-message yyyy-mm-dd-string)]
    (state/append-in-state! [:FACEBOOK :posts (:id to-user)] post)
    post))

(defn fetch-feeds [user]
  (print-debug "Fetching feeds for" (:name user))
  (state/get-from-state [:FACEBOOK :posts (:id user)]))

(defn extended-user-info [user]
  (select-keys user [:uid :first_name :last_name :username :sex :birthday_date :locale :current_location :email :pic_small :pic_big :profile_url]))

(defn login-creds [user]
  {:providerLoginInfo
   {:authResponse
    {:userID (:id user)
     :accessToken (:access-token user)
     :expiresIn "5855"}
    :status "connected"}})