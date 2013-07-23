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


(defn get-messages-from-folder-for-account [account-id folder]
  (it-> account-id
        (get-user it)
        (:email-address it)
        (state/get-from-state [:CONTEXT-IO :emails folder it])))

(defn get-messages-for-account [account-id]
  (get-messages-from-folder-for-account account-id :inbox))

(defn get-deleted-messages-for-account [account-id]
  (get-messages-from-folder-for-account account-id :trash))

(defn create-account
  ([first-name last-name email-address]
     (create-account first-name last-name email-address (random-guid-str)))
  ([first-name last-name email-address account-id]
     (let [user (factory/new-user first-name last-name email-address account-id)]
       (state/assoc-in-state! [:CONTEXT-IO :users (:account-id user)] user))))

(defn send-message
  ([from-email to-email subject message yyyy-dd-mm-hh-mm-string]
     (send-message from-email to-email subject (random-guid-str) message yyyy-dd-mm-hh-mm-string))
  ([from-email to-email subject thread-id message yyyy-dd-mm-hh-mm-string]
     (send-message from-email to-email subject thread-id message [] yyyy-dd-mm-hh-mm-string))  
  ([from-email to-email subject thread-id message attachment-map-seq yyyy-dd-mm-hh-mm-string]
     (let [account-id (-> from-email get-user-by-email :account-id)
           msg (factory/new-message yyyy-dd-mm-hh-mm-string from-email to-email subject thread-id message attachment-map-seq)]
       (state/append-in-state! [:CONTEXT-IO :emails :inbox (:from msg)] msg)
       (state/append-in-state! [:CONTEXT-IO :emails :inbox (:to msg)] msg)
       msg)))

(defn delete-message [msg]
  (let [msg (factory/move-to-trash msg)]
    (state/append-in-state! [:CONTEXT-IO :emails :trash (:from msg)] msg)
    (state/append-in-state! [:CONTEXT-IO :emails :trash (:to msg)] msg)
    (state/remove-from-state! [:CONTEXT-IO :emails :inbox (:from msg)] msg)
    (state/remove-from-state! [:CONTEXT-IO :emails :inbox (:to msg)] msg)))

(defn delete-messages [& msgs]
  (doeach delete-message msgs))