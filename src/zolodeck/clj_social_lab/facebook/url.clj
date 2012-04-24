(ns zolodeck.clj-social-lab.facebook.url
  (:use zolodeck.clj-social-lab.utils.debug
        zolodeck.clj-social-lab.utils.core)
  (:require [uri.core :as uri]))

(defn app-access-token-url []  
  "https://graph.facebook.com/oauth/access_token")

(defn all-test-user-url [app-id app-access-token]
  (str "https://graph.facebook.com/" app-id "/accounts/test-users?limit=500&access_token=" (uri/url-encode app-access-token)))

(defn create-test-user-url [app-id]
  (str "https://graph.facebook.com/" app-id "/accounts/test-users"))

(defn delete-test-user-url [user-id]
  (str "https://graph.facebook.com/" user-id "?method=delete"))

(defn friend-request-url [user-id friend-id]
  (str "https://graph.facebook.com/"
       user-id
       "/friends/"
       friend-id
       "?method=post"))

