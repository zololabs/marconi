(ns zolo.marconi.context-io.core
  (:use zolo.utils.debug
        zolo.utils.clojure)
  (:require [zolo.marconi.test-state :as state]
            [zolo.marconi.context-io.factory :as factory]))

(defn get-user [account-id]
  (state/get-from-state [:CONTEXT-IO :users account-id]))

(defn get-user-by-email [email-address]
  (it-> (state/all-state)
        (get-in it [:CONTEXT-IO :users])
        (vals it)
        (filter #(= email-address (:email-address %)) it)
        (first it)))

(defn get-messages-for-account [account-id]
  (it-> account-id
        (get-user it)
        (:email-address it)
        (state/get-from-state [:CONTEXT-IO :emails it])))

(defn create-account
  ([first-name last-name email-address]
     (create-account first-name last-name email-address (random-guid-str)))
  ([first-name last-name email-address account-id]
     (let [user (factory/new-user first-name last-name email-address account-id)]
       (state/assoc-in-state! [:CONTEXT-IO :users (:account-id user)] user))))

(defn send-message
  ([yyyy-dd-mm-hh-mm-string from-email to-email subject message]
     (send-message yyyy-dd-mm-hh-mm-string from-email to-email subject message []))
  ([yyyy-dd-mm-hh-mm-string from-email to-email subject message attachment-map-seq]
     (let [account-id (-> from-email get-user-by-email :account-id)
           msg (factory/new-message yyyy-dd-mm-hh-mm-string from-email to-email subject message attachment-map-seq)]
       (state/append-in-state! [:CONTEXT-IO :emails (:from msg)] msg)
       (state/append-in-state! [:CONTEXT-IO :emails (:to msg)] msg))))

