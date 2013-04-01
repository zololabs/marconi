(ns zolo.marconi.facebook.core
  (:use zolo.marconi.facebook.factory
        zolo.utils.debug
        zolo.utils.clojure)
  (:require [zolo.marconi.test-state :as state]))

(defn get-user [id]
  (state/get-from-state [:users id]))

(defn login-as [user]
  (print-vals "Logging in as:" (:name user) "-" (:id user))
  (state/assoc-in-state! [:current-user] user))

(defn current-user []
  (state/get-from-state [:current-user]))

(defn create-user [first-name last-name]
  (let [user (new-user first-name last-name)]
    (state/assoc-in-state! [:users (:id user)] user)))

(defn create-friend [first-name last-name]
  (-> (create-user first-name last-name)
      as-friend))

(defn create-friends [prefix n]
  (for [i (range 1 (inc n))]
    (create-friend (str "first-name-" prefix "-" i) (str "last-name-" prefix "-" i))))

(defn update-user [id attribs-map]
  (->> attribs-map
       (merge (state/get-from-state [:users id]))
       (state/assoc-in-state! [:users id])))

(defn update-friend [id attribs-map]
  (-> (update-user id attribs-map)
      as-friend))

(defn make-friend [{main-id :id :as main-user} {other-id :id :as other-user}]
  (print-vals "Making friend:" (:name (get-user main-id)) "<->" (:name (get-user other-id)))
  (state/append-in-state! [:friends (:id main-user)] (:id other-user))
  (state/append-in-state! [:friends (:id other-user)] (:id main-user)))

(defn unfriend [{main-id :id :as main-user} {other-id :id :as other-user}]
  (print-vals "Unfriending :" (:name (get-user main-id)) "<->" (:name (get-user other-id)))
  (state/remove-from-state! [:friends (:id main-user)] (:id other-user))
  (state/remove-from-state! [:friends (:id other-user)] (:id main-user)))

(defn fetch-friends [user]
  (print-vals "Fetching friends for" (:name user))
  (->> (state/get-from-state [:friends (:id user)])
       (map #(as-friend (state/get-from-state [:users %])))))

(defn send-message [from-user to-user thread-id message yyyy-mm-dd-string]
  (print-vals "Message on" yyyy-mm-dd-string "from" (:name from-user) "to" (:name to-user) ":" message)
  (let [msg (new-message from-user to-user thread-id message yyyy-mm-dd-string)]
    (state/append-in-state! [:messages (:id from-user)] msg)
    (state/append-in-state! [:messages (:id to-user)] msg)
    msg))

(defn remove-all-messages [user]
  (state/assoc-in-state! [:messages (:id user)] []))

(defn fetch-messages [user]
  (print-vals "Fetching messages for" (:name user))
  (state/get-from-state [:messages (:id user)]))

(defn post-to-wall [from-user to-user post-message yyyy-mm-dd-string]
  (print-vals "Post on" yyyy-mm-dd-string "from" (:name from-user) "to" (:name to-user) ":" post-message)
  (let [post (new-post from-user to-user post-message yyyy-mm-dd-string)]
    (state/append-in-state! [:posts (:id to-user)] post)
    post))

(defn fetch-feeds [user]
  (print-vals "Fetching feeds for" (:name user))
  (state/get-from-state [:posts (:id user)]))

(defn extended-user-info [user]
  (select-keys user [:uid :first_name :last_name :username :sex :birthday_date :locale :current_location :email :pic_small :pic_big :profile_url]))

(defn login-creds [user]
  {:providerLoginInfo
   {:authResponse
    {:userID (:id user)
     :accessToken (:access-token user)
     :expiresIn "5855"}
    :status "connected"}})

(defmacro in-facebook-lab [& body]
  `(binding [state/TEST-STATE (atom {})]
     (do 
       ~@body)))