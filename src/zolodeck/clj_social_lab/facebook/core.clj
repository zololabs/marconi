(ns zolodeck.clj-social-lab.facebook.core
  (:use zolodeck.clj-social-lab.facebook.factory
        zolodeck.utils.debug
        zolodeck.utils.clojure))

(def ^:dynamic TEST-STATE)

(defn assoc-in-state! [key-seq value]
  (swap! TEST-STATE assoc-in key-seq value)
  value)

(defn update-in-state! [key-seq update-fn value]
  (swap! TEST-STATE #(update-in %1 key-seq update-fn value))
  value)

(defn append-in-state! [key-seq value]
  (update-in-state! key-seq conj value))

(defn get-from-state [key-seq]
  (get-in @TEST-STATE key-seq))

(defn login-as [user]
  (print-vals "Logging in as:" (:name user) "-" (:id user))
  (assoc-in-state! [:current-user] user))

(defn create-user [first-name last-name]
  (let [user (new-user first-name last-name)]
    (assoc-in-state! [:users (:id user)] user)))

(defn make-friend [main-user other-user]
  (print-vals "Making friend:" (:name main-user) "<->" (:name other-user))
  (append-in-state! [:friends (:id main-user)] (:id other-user))
  (append-in-state! [:friends (:id other-user)] (:id main-user)))

(defn fetch-friends [user]
  (print-vals "Fetching friends for" (:name user))
  (->> (get-from-state [:friends (:id user)])
       (map #(as-friend (get-from-state [:users (:id %)])))))

(defn send-message [from-user to-user thread-id message yyyy-mm-dd-string]
  (print-vals "Message on" yyyy-mm-dd-string "from" (:name from-user) "to" (:name to-user) ":" message)
  (let [msg (new-message from-user to-user thread-id message yyyy-mm-dd-string)]
    (append-in-state! [:messages (:id from-user)] msg)
    (append-in-state! [:messages (:id to-user)] msg)))

(defn fetch-messages [user]
  (print-vals "Fetching messages for" (:name user))
  (get-from-state [:messages (:id user)]))

(defn dump-test-state []
  (print-vals "TEST-STATE:" @TEST-STATE))

(defmacro in-facebook-lab [& body]
  `(binding [TEST-STATE (atom {})]
     (do 
       ~@body)))