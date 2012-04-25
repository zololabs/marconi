(ns zolodeck.clj-social-lab.facebook.user
  (:use zolodeck.utils.debug
        zolodeck.utils.maps)
  (:require [zolodeck.clj-social-lab.facebook.request :as fb-request]
            [zolodeck.clj-social-lab.facebook.url :as fb-url]
            [zolodeck.clj-social-lab.facebook.context :as context]
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

(defn create 
  ([full-name]
     (create context/APP-ID context/APP-ACCESS-TOKEN full-name true context/DEFAULT-PERMISSIONS))
  ([app-id app-access-token full-name app-installed? permissions]
     (print-vals "Creating User : " full-name)
     (->> (fb-request/create-user-request app-access-token permissions app-installed? full-name)
          (http/post (fb-url/create-test-user-url app-id))
          mapify-response-body
          (merge {:name full-name})
          context/add-user!)))

(defn delete [user]
  (print-vals "Deleting User : " (:name user))
  (http/post (fb-url/delete-test-user-url (:id user)) 
             (fb-request/empty-user-request (:access-token user)))
  (context/remove-user! user))

(defn delete-all 
  ([]
     (delete-all context/APP-ID context/APP-ACCESS-TOKEN))
  ([users]
     (delete-all context/APP-ID context/APP-ACCESS-TOKEN users))
  ([app-id app-access-token]
     (delete-all app-id app-access-token (all app-id app-access-token)))
  ([app-id app-access-token users]
     (print-vals "Deleting All Users")
     (doseq [u users]
       (delete u))))

(defn make-friend 
  ([friend]
     (make-friend (context/current-user) friend))
  ([user friend]
     (print-vals "Making " (:name user) " friend of " (:name friend))
     (http/post 
      (fb-url/friend-request-url (:id user) (:id friend)) 
      (fb-request/empty-user-request (:access-token user)))
     
     (http/post 
      (fb-url/friend-request-url (:id friend) (:id user)) 
      (fb-request/empty-user-request (:access-token friend)))))

(defn friends [user])

(defn news-feed [user])

(defn details [user])

(defn facebook-id [user])

(defn access-token 
  ([]
     (access-token (context/current-user)))
  ([user]
     (:access-token user)))