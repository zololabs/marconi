(ns zolodeck.clj-social-lab.facebook
  (:use 
   zolodeck.clj-social-lab.utils.debug
   zolodeck.clj-social-lab.utils.core)
  (:require 
   [zolodeck.clj-social-lab.facebook.url :as fb-url]
   [zolodeck.clj-social-lab.facebook.context :as context]
   [zolodeck.clj-social-lab.facebook.user :as user]
   [zolodeck.clj-social-lab.facebook.request :as fb-request]
   [clj-http.client :as http]
   [uri.core :as uri]))

(defn app-access-token [app-id app-secret]
  (print-vals "Getting App Access Token")
  (->> (fb-request/access-token-request app-id app-secret) 
       (http/post (fb-url/app-access-token-url))
       :body
       uri/form-url-decode
       :access_token))

(defn login-as [user]
  (print-vals "Logging in as " (:id user))
  (reset! context/CURRENT-USER user))

(defmacro in-facebook-lab [app-id app-secret & body]
  `(binding [context/APP-ID ~app-id
             context/APP-ACCESS-TOKEN (app-access-token ~app-id ~app-secret)]
     (user/delete-all)
     (try 
      ~@body
      (finally 
       (user/delete-all)))))



