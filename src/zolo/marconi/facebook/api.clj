(ns zolo.marconi.facebook.api
  (:use zolo.utils.debug
        zolo.utils.calendar
        zolo.utils.clojure
        zolo.utils.maps
        [slingshot.slingshot :only [throw+ try+]])
  (:require [clj-facebook-graph.auth :as fb-auth]
            [clj-http.client :as http]
            [uri.core :as uri]
            [zolo.marconi.facebook.request :as fb-request]
            [zolo.marconi.facebook.url :as fb-url]
            [clojure.data.json :as json]
            [clj-facebook-graph.client :as fb-client]))

(defn app-access-token [app-id app-secret]
  (print-vals "Getting App Access Token")
  (->> (fb-request/access-token-request app-id app-secret) 
       (http/post (fb-url/app-access-token-url))
       :body
       uri/form-url-decode
       :access_token))

(def ^:dynamic APP-ID)
(def ^:dynamic APP-SECRET)
(def ^:dynamic APP-ACCESS-TOKEN)

(def DEFAULT-PERMISSIONS "email,friends_about_me,friends_birthday,friends_relationship_details,friends_location,friends_likes,friends_website,read_mailbox,offline_access")

(defn run-fql [auth-token query-string]
  (->> {:fql query-string}
       (fb-client/get :fql)
       (fb-auth/with-facebook-auth {:access-token auth-token})
       :body))

(defn all [app-id app-access-token]
  (->> (fb-request/empty-user-request app-access-token)
       (http/get (fb-url/all-test-user-url app-id app-access-token))
       :body
       json/read-json
       :data
       (map keywordize-map)))

(defn user-from-fb [auth-token]
  (-> (fb-auth/with-facebook-auth {:access-token auth-token} (fb-client/get [:me]))
      :body
      (assoc :access-token auth-token)))

(defn all-users []
  (map #(user-from-fb (:access-token %)) (all APP-ID APP-ACCESS-TOKEN)))

(defn friends-list [user]
  (fb-auth/with-facebook-auth {:access-token (:access-token user)} 
    (fb-client/get [:me :friends]
                   {:query-params 
                    {:fields "id,first_name,last_name,gender,locale,link,username,installed,bio,birthday,education,email,hometown,interested_in,location,picture,relationship_status,significant_other,website"}
                    :extract :data 
                    :paging true})))

(defn message-fql [thread-id]
  (str "SELECT message_id, thread_id, author_id, body, created_time, attachment FROM message WHERE thread_id = " thread-id))

(defn update-message [subject recipient msg]
  (when-not (= recipient (:author_id msg))
    (-> msg
        (assoc :subject subject)
        (assoc :to recipient))))

(defn expand-messages [subject recipients messages]
  (mapcat
   (fn [msg]
     (keep #(update-message subject % msg) recipients))
   messages))

(defn fetch-thread-messages [auth-token {:keys [thread_id recipients subject] :as thread-info}]
  (->> (run-fql auth-token (message-fql thread_id))
       (expand-messages subject recipients)))

(defn fetch-messages [{:keys [access-token]}]
  (print-vals "Access-token:" access-token)
  (->> "SELECT thread_id, recipients, subject FROM thread WHERE folder_id = 0"
       (run-fql access-token)
       (mapcat (partial fetch-thread-messages access-token))))
