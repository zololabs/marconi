(ns zolodeck.clj-social-lab.facebook.api
  (:use zolodeck.utils.system
        zolodeck.utils.debug
        zolodeck.utils.calendar
        zolodeck.utils.clojure
        zolodeck.utils.maps
        [slingshot.slingshot :only [throw+ try+]])
  (:require [clj-facebook-graph.auth :as fb-auth]
            [clj-http.client :as http]
            [uri.core :as uri]
            [zolodeck.clj-social-lab.facebook.request :as fb-request]
            [zolodeck.clj-social-lab.facebook.url :as fb-url]
            [clojure.data.json :as json]
            [clj-facebook-graph.client :as fb-client]))

(defn app-access-token [app-id app-secret]
  (print-vals "Getting App Access Token")
  (->> (fb-request/access-token-request app-id app-secret) 
       (http/post (fb-url/app-access-token-url))
       :body
       uri/form-url-decode
       :access_token))

(def APP-ID (system-env "SOCIAL_LAB_TEST_APP_ID"))
(def APP-SECRET (system-env "SOCIAL_LAB_TEST_APP_SECRET"))
(def APP-ACCESS-TOKEN (app-access-token APP-ID APP-SECRET))
(def DEFAULT-PERMISSIONS "email,friends_about_me,friends_birthday,friends_relationship_details,friends_location,friends_likes,friends_website,read_mailbox,offline_access")

(defn run-fql [auth-token query-string]
  (:body (fb-auth/with-facebook-auth {:access-token auth-token} 
           (fb-client/get :fql {:fql query-string}))))

(defn all [app-id app-access-token]
  (->> (fb-request/empty-user-request app-access-token)
       (http/get (fb-url/all-test-user-url app-id app-access-token))
       :body
       json/read-json
       :data
       (map keywordize-map)))

(defn user-from-fb [auth-token]
  (-> (fb-auth/with-facebook-auth {:access-token auth-token} 
        (fb-client/get [:me]))
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

(defn inbox-fql []
  "SELECT thread_id FROM thread WHERE folder_id = 0 ")

(defnk message-fql [:start-time BEGINNING-OF-TIME :thread-id nil]
  (if-not thread-id (throw+ {:severity :high :reason :missing-value :field :thread-id}))
  (str "SELECT message_id, thread_id, author_id, body, created_time, attachment, viewer_id FROM message WHERE thread_id = " thread-id))

(defn fetch-thread-messages [auth-token thread-id]
  (run-fql auth-token (message-fql :thread-id thread-id)))

(defn fetch-messages [{:keys [access-token]}]
  (print-vals "Access-token:" access-token)
  (->> (inbox-fql)
       (run-fql access-token)
       (map :thread_id)
       (mapcat (partial fetch-thread-messages access-token))))
