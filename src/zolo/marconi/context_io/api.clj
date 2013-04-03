(ns zolo.marconi.context-io.api
  (:use zolo.utils.debug)
  (:require [zolo.marconi.context-io.core :as cio]
            [zolo.utils.calendar :as zcal]))

(defn fetch-contact [email-address]
  :email "punitr@gmail.com",
  :count 2112,
  :name "Punit Rathore",
  :received_count 44,
  :sent_count 2068,
  :sent_from_account_count 2063,
  :thumbnail
  "https://secure.gravatar.com/avatar/986630a6219cf58ee49bb67bef1e5adf?s=50",
  :resource_url
  "https://api.context.io/2.0/accounts/506a10440d75b44a6e000000/contacts/punitr@gmail.com")

(defn- prepare-message [m]
  {:subject (:subject m)
   :date (zcal/to-seconds (:date m))
   :gmail_message_id (str "gmail_" (:id m))
   :folders ["INBOX" "\\Inbox"],
   :date_indexed (+ 1000 (zcal/to-seconds (:date m)))
   :gmail_thread_id (str "gmail_thread_" (:thread-id m))
   :resource_url
   (str "https://api.context.io/2.0/accounts/506a10440d75b44a6e000000/messages/" (:id m))
   :person_info
   {(:to m)
    {:thumbnail
     "https://secure.gravatar.com/avatar/ebbba1fbc333440aca75b3ff85fe777a?s=50&d=https%3A%2F%2Fs3.amazonaws.com%2Fcontextio-icons%2Fcontact.png"}
    (:from m)
    {:thumbnail
     "https://secure.gravatar.com/avatar/17b710d2dbed67afc4246c4036905e82?s=50&d=https%3A%2F%2Fs3.amazonaws.com%2Fcontextio-icons%2Fcontact.png"}}
   :email_message_id (str "<" (:id m) "@prod-worker.X.ABC.context-io.mocked.com.tmail>")
   :message_id (:id m)
   :sources
   [{:label (str (:from m) "::imap.googlemail.com")
     :resource_url (str "https://api.c ontext.io/2.0/accounts/506a10440d75b44a6e000000/sources/"
                        (:from m)
                        "%3A%3Aimap.googlemail.com")}]
   :date_received (+ 10 (zcal/to-seconds (:date m)))
   :addresses
   {:from
    {:email (:from m)
     :name "From Name"},
    :to [{:email (:to m)}]}})

(defn fetch-messages [account-id]
  (->> account-id
       cio/get-messages-for-account
       (map prepare-message)))