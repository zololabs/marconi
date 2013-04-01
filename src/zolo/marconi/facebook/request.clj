(ns zolo.marconi.facebook.request
  (:use zolodeck.utils.debug)
  (:require [uri.core :as uri]))

(defn encoded-request-params [body-map]
  {:content-type "application/x-www-form-urlencoded"
   :accept "application/json"
   :throw-exceptions false
   :body (uri/form-url-encode body-map)})

(defn empty-user-request [access-token]
  (encoded-request-params {:access_token access-token}))

(defn access-token-request [app-id app-secret]
  (encoded-request-params {:grant_type "client_credentials"
                           :client_id app-id
                           :client_secret app-secret}))

(defn create-user-request [app-access-token permissions installed? full-name]
  (encoded-request-params {:installed installed?
                           :name full-name
                           :permissions permissions
                           :access_token app-access-token}))

