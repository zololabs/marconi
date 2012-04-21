(ns zolodeck.clj-facebook-lab.facebook
  (:use 
   zolodeck.clj-facebook-lab.utils.debug)
  (:require 
   [clj-http.client :as http]
   [uri.core :as uri]))

(def ^:dynamic APP-ID)
(def ^:dynamic APP-SECRET)
(def ^:dynamic APP-ACCESS-TOKEN)

(def ^:dynamic CURRENT-USER)

(def FACEBOOK-OAUTH2
  {:authorization-uri "https://graph.facebook.com/oauth/authorize"
   :access-token-uri "https://graph.facebook.com/oauth/access_token"
   :client-id APP-ID
   :client-secret APP-SECRET
   :access-query-param :access_token
   :grant-type "authorization_code"})

(defn app-access-token 
  ([]
     (app-access-token APP-ID APP-SECRET))
  ([app-id app-secret]
     (let [request (-> {:content-type "application/x-www-form-urlencoded"
                        :throw-exceptions false
                        :body {:grant_type "client_credentials"
                               :client_id app-id
                               :client_secret app-secret}}
                       (update-in [:body] uri/form-url-encode))]
       (let [{:keys [body headers status]} (http/post (:access-token-uri FACEBOOK-OAUTH2) request)
             content-type (headers "content-type")]
         (:access_token (uri/form-url-decode body))))))