(ns zolodeck.clj-facebook-lab.utils.core)

(defn system-env [property]
  (.get (System/getenv) property))