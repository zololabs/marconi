(ns zolo.marconi.core
  (:use zolo.utils.debug)
  (:require [zolo.marconi.test-state :as state]))

(defmacro in-lab [& body]
  `(binding [state/TEST-STATE (atom {})]
     (do 
       ~@body)))