(ns zolo.marconi.context-io.factory
  (:use zolo.utils.debug
        zolo.utils.clojure)
  (:require [zolo.utils.calendar :as zcal]))

(defn new-user [first-name last-name email-address account-id]
  {:first_name first-name
   :last-name last-name
   :email-address email-address
   :account-id account-id})

(defn new-message [yyyy-dd-mm-hh-mm-string from-email to-email subject message files]
  {:id (random-guid-str)
   :thread-id (random-guid-str)
   :date (zcal/date-string->instant yyyy-dd-mm-hh-mm-string)
   :from from-email
   :to to-email
   :subject subject
   :message message
   :files files})

