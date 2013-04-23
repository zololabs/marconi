(ns zolo.marconi.debug
  (:use zolo.utils.debug))

(def ^:dynamic VERBOSE false)

(defn print-debug [& args]
  (if VERBOSE
    (apply print-vals args)
    (last args)))