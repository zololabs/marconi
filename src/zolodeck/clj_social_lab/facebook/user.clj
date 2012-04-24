(ns zolodeck.clj-social-lab.facebook.user
  (:use zolodeck.clj-social-lab.utils.debug
        zolodeck.clj-social-lab.utils.core)
  (:require [zolodeck.clj-social-lab.facebook.test-user-store :as store]
            [zolodeck.clj-social-lab.facebook.request :as fb-request]
            [zolodeck.clj-social-lab.facebook.url :as fb-url]
            [clj-http.client :as http]
            [uri.core :as uri]
            [clojure.data.json :as json]))

(defn mapify-response-body [response]
  (-> response
      :body
      json/read-json
      keywordize-map))

(defn all [app-id app-access-token]
  (->> (fb-request/empty-user-request app-access-token)
       (http/get (fb-url/all-test-user-url app-id app-access-token))
       :body
       json/read-json
       :data
       (map keywordize-map)))

(defn create [app-id app-access-token full-name app-installed? permissions]
  (->> (fb-request/create-user-request app-access-token permissions app-installed? full-name)
       (http/post (fb-url/create-test-user-url app-id))
       mapify-response-body))

(defn delete [user]
  (http/post (fb-url/delete-test-user-url (:id user)) 
             (fb-request/empty-user-request (:access-token user))))

(defn delete-all [app-id app-access-token]
  (map delete (all app-id app-access-token)))

(defn make-friends [user friend]
  (http/post 
   (fb-url/friend-request-url (:id user) (:id friend)) 
   (fb-request/empty-user-request (:access-token user)))

  (http/post 
   (fb-url/friend-request-url (:id friend) (:id user)) 
   (fb-request/empty-user-request (:access-token friend))))

(defn friends [user])

(defn news-feed [user])

(defn details [user])

(defn facebook-id [user])

(defn access-token [user])