(ns zolodeck.clj-social-lab.facebook
  (:use 
   zolodeck.clj-social-lab.utils.debug
   zolodeck.clj-social-lab.utils.core)
  (:require 
   [zolodeck.clj-social-lab.facebook.url :as fb-url]
   [zolodeck.clj-social-lab.facebook.request :as fb-request]
   [clj-http.client :as http]
   [uri.core :as uri]))

(def APP-ACCESS-TOKEN (atom nil))

(def ^:dynamic CURRENT-USER (atom nil))

(defn app-access-token [app-id app-secret]
  (->> (fb-request/access-token-request app-id app-secret) 
       (http/post (fb-url/app-access-token-url))
       :body
       uri/form-url-decode
       :access_token))

(defn init-facebook-lab 
  ([]
     (init-facebook-lab (system-env "APP_ID") (system-env "APP_SECRET")))
  ([app-id app-secret]
     (reset! APP-ACCESS-TOKEN (print-vals (app-access-token app-id app-secret)))))


