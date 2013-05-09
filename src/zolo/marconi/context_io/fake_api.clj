(ns zolo.marconi.context-io.fake-api
  (:use zolo.utils.debug
        zolo.utils.clojure)
  (:require [zolo.marconi.context-io.core :as cio]
            [zolo.utils.calendar :as zcal]))

;; TODO - hard-coding the counts, fix to use actual counts if needed
(defn prepare-contact [email-address]
  {:email email-address
   :count 1100000
   :name (str (-> email-address (clojure.string/split #"@") first) " von LastName")
   :received_count 1000000
   :sent_count 100000
   :sent_from_accosunt_count 100000
   :thumbnail "https://secure.gravatar.com/avatar/986630a6219cf58ee49bb67bef1e5adf?s=50",
   :resource_url (str "https://api.context.io/2.0/accounts/506a10440d75b44a6e000000/contacts/" email-address)})

(defn- prepare-message [m]
  {:subject (:subject m)
   :date (zcal/to-seconds (:date m))
   :gmail_message_id (:id m)
   :folders ["INBOX" "\\Inbox"],
   :date_indexed (+ 1000 (zcal/to-seconds (:date m)))
   :gmail_thread_id (:thread-id m)
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

(defn- prepare-account [{:keys [first-name last-name email-address account-id]}]
  {:nb_files 10000
   :last_name last-name
   :suspended 0
   :email_addresses [email-address]
   :password_expired 0
   :username (str email-address "_" account-id)
   :created 1349128260
   :first_name first-name
   :sources
   [{:status "OK"
     :server "imap.googlemail.com"
     :service_level "pro"
     :resource_url (str "https://api.context.io/2.0/accounts/" account-id "/sources/" email-address "::imap.googlemail.com")
     :sync_period "1d"
     :port 993
     :username email-address
     :use_ssl true
     :type "imap"
     :authentication_type "oauth"
     :label (str email-address "::imap.googlemail.com")}]
   :nb_messages 1000000
   :id account-id})

(defn fetch-contacts [account-id]
  (->> account-id
       cio/get-messages-for-account
       (mapcat #(list (:to %) (:from %)))
       distinct
       (remove #(= % (:email-address (cio/get-user account-id))))
       (domap prepare-contact)))

(defn fetch-messages
  ([account-id]
     (fetch-messages account-id (zcal/to-seconds "1970-01-01")))
  ([account-id date-in-seconds]
     (let [since (zcal/seconds->instant date-in-seconds)]
       (->> account-id
            cio/get-messages-for-account
            (filter #(> (.compareTo (:date %) since) 0))
            (domap prepare-message)))))

(defn fetch-account [account-id]
  (->> account-id
       cio/get-user
       prepare-account))