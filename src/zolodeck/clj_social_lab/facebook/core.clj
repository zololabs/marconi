(ns zolodeck.clj-social-lab.facebook.core
  (:use zolodeck.utils.debug)
  (:require [zolodeck.clj-social-lab.facebook.url :as fb-url]
            [zolodeck.clj-social-lab.facebook.request :as fb-request]
            [clj-http.client :as http]
            [uri.core :as uri]))

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

(defn app-access-token [app-id app-secret]
  (print-vals "Getting App Access Token")
  (->> (fb-request/access-token-request app-id app-secret) 
       (http/post (fb-url/app-access-token-url))
       :body
       uri/form-url-decode
       :access_token))

(defn login-as [user]
  (print-vals "Logging in as:" (:name user) "-" (:id user))
  (assoc-in-state! [:current-user] user))

(defn create-user [full-name]
  (->> {:name full-name :id (rand-int 1e8)}
       (assoc-in-state! [:users full-name])))

(defn make-friend [main-user other-user]
  (print-vals "Making friend:" (:name main-user) "<->" (:name other-user))
  (assoc-in-state! [:friends (:id main-user)] (:id other-user))
  (assoc-in-state! [:friends (:id other-user)] (:id main-user)))

(defn send-message [from-user to-user message date]
  (print-vals "Message on" date "from" (:name from-user) "to" (:name to-user) ":" message)
  (let [msg {:from (:id from-user) :to (:id to-user) :message message :date date}]
    (append-in-state! [:messages (:id from-user)] msg)
    (append-in-state! [:messages (:id to-user)] msg)))

(defn fetch-messages [user]
  (print-vals "Fetching messages for" (:name user))
  (get-from-state [:messages (:id user)]))

(defn dump-test-state []
  (print-vals "TEST-STATE:" @TEST-STATE))

(defmacro in-facebook-lab [app-id app-secret & body]
  `(if-not (and ~app-id ~app-secret)
     (throw (RuntimeException. "You need to provide both an App ID and an App Secret"))
     (do
       (binding [TEST-STATE (atom {})]
         (try 
          ~@body)))))