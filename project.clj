(defproject zololabs/marconi "1.0.0-SNAPSHOT"
  :description "Fake APIs for email-inboxes, social-networks, and more"
  
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [zololabs/zolo-utils "0.1.0-SNAPSHOT"]]

  :plugins [[lein-swank "1.4.4"]
            [lein-pprint "1.1.1"]
            [lein-clojars "0.9.1"]]

  :dev-dependencies [[clj-stacktrace "0.2.4"]]
  
  :min-lein-version "2.0.0"

  :test-selectors {:default (fn [t] (not (:integration t)))
                   :integration :integration
                   :all (fn [t] true)}
  
  :project-init (do (use 'clojure.pprint)
                    (use 'clojure.test))

  :warn-on-reflection false
  
  :resources-path "config")