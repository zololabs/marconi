(ns zolodeck.clj-social-lab.facebook.data-factory)

(defn friend []
  (let [id (rand-int 100000000)]
    {:gender (if (zero? (rand-int 2)) "male" "female")
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