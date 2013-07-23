(ns zolo.marconi.context-io.factory
  (:use zolo.utils.debug
        zolo.utils.clojure)
  (:require [zolo.utils.calendar :as zcal]))

(defn new-user [first-name last-name email-address account-id]
  {:first-name first-name
   :last-name last-name
   :email-address email-address
   :account-id account-id
   :access-token (str (random-guid))})

(defn new-message [yyyy-dd-mm-hh-mm-string from-email to-email subject thread-id message files]
  {:id (random-guid-str)
   :thread-id thread-id
   :date (zcal/date-string->instant yyyy-dd-mm-hh-mm-string)
   :from from-email
   :to to-email
   :subject subject
   :message message
   :files files})

(defn move-to-trash [msg]
  (assoc msg :deleted-on (zcal/now-instant)))