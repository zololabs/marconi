(ns zolodeck.clj-facebook-lab.facebook
  (:use 
   zolodeck.clj-facebook-lab.utils.debug
   zolodeck.clj-facebook-lab.utils.core)
  (:require 
   [clj-http.client :as http]
   [uri.core :as uri]))

(def APP-ACCESS-TOKEN (atom nil))

(def ^:dynamic CURRENT-USER (atom nil))

(def ACCESS-TOKEN-URI  "https://graph.facebook.com/oauth/access_token")

;; (def FACEBOOK-OAUTH2
;;   {:authorization-uri "https://graph.facebook.com/oauth/authorize"
;;    :access-token-uri "https://graph.facebook.com/oauth/access_token"
;;    :client-id APP-ID
;;    :client-secret APP-SECRET
;;    :access-query-param :access_token
;;    :grant-type "authorization_code"})

(defn app-access-token [app-id app-secret]
  (let [request (-> {:content-type "application/x-www-form-urlencoded"
                     :accept-content-type "application/json"
                     :throw-exceptions false
                     :body {:grant_type "client_credentials"
                            :client_id app-id
                            :client_secret app-secret}}
                    (update-in [:body] uri/form-url-encode))]
    (let [{:keys [body headers status]} (print-vals (http/post ACCESS-TOKEN-URI request))
          content-type (headers "content-type")]
      (:access_token (uri/form-url-decode body)))))

(defn init-facebook-lab 
  ([]
     (init-facebook-lab (system-env "APP_ID") (system-env "APP_SECRET")))
  ([app-id app-secret]
     (reset! APP-ACCESS-TOKEN (print-vals (app-access-token app-id app-secret)))))

