(ns zolodeck.clj-social-lab.utils.core)

(defn system-env [property]
  (.get (System/getenv) property))