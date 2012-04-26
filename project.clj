(defproject clj-social-lab "1.0.0-SNAPSHOT"
  :description "Library to do integration test with Social Networks"
  
  :dependencies [[org.clojure/clojure "1.4.0"]
                 
                 [org.clojure/data.json "0.1.2"  :exclude [org.clojure/clojure]]
                 [clj-http "0.3.6"]
                 [clj-oauth2 "0.2.0"]

                 [joda-time "1.6"]
                 [clj-time "0.3.7"]
                 [slingshot "0.10.2"]

                 [zolodeck/zolo-utils "0.1.0-SNAPSHOT"]]

  :plugins [[lein-swank "1.4.4"]
            [lein-pprint "1.1.1"]
            [lein-difftest "1.3.8"]
            [lein-notes "0.0.1"]]

  :hooks [leiningen.hooks.difftest]

  :dev-dependencies [[clj-stacktrace "0.2.4"]
                     [swank-clojure "1.3.3"]
                     [clojure-csv/clojure-csv "2.0.0-alpha1"]]
  
  :min-lein-version "1.7.0"

  :test-selectors {:default (fn [t] (not (:integration t)))
                   :integration :integration
                   :all (fn [t] true)}
  
  :project-init (do (use 'clojure.pprint)
                    (use 'clojure.test))

  :warn-on-reflection false
  
  :repositories {"jboss" "http://repository.jboss.org/nexus/content/groups/public/"
                 "local" ~(str (.toURI (java.io.File. "../mvn_repo")))}
  
  :resources-path "config")