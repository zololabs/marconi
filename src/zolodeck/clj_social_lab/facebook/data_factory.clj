(ns zolodeck.clj-social-lab.facebook.data-factory)

(defn- gender []
  (if (zero? (rand-int 2)) "male" "female"))

(defn- last-name [id]
  (str "last_name_" id))

(defn- first-name [id]
  (str "first_name_" id))

(defn user []
 (let [id (rand-int 100000000)]
   {:gender (gender),
    :last_name (last-name id)
    :link (str "http://www.facebook.com/" id),
    :timezone -7,
    :name (str (first-name id) " " (last-name id)),
    :locale "en_US",
    :username (str "username_" id),
    :email (str id "@gmail.com"),
    :updated_time "2012-02-17T17:36:14+0000",
    :first_name (first-name id)
    :verified true,
    :id (str id)}))

(defn friend []
  (let [id (rand-int 100000000)]
    {:gender (gender)
     :last_name (str "last_name_" id)
     :link (str "http://www.facebook.com/profile.php?id=" id),
     :installed true,
     :locale "en_US",
     :first_name (str "first_name_" id)
     :id (str id)
     :birthday "08/08/1980",
     :picture (str "http://profile.ak.fbcdn.net/static-ak/rsrc.php/v1/yo/r/" id  ".gif")}))

(defn friends [n]
  (repeatedly n friend))