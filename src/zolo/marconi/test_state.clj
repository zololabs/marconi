(ns zolo.marconi.test-state
  (:use zolo.utils.debug))

(def ^:dynamic TEST-STATE)

(defn get-from-state [key-seq]
  (get-in @TEST-STATE key-seq))

(defn assoc-in-state! [key-seq value]
  (swap! TEST-STATE assoc-in key-seq value)
  value)

(defn update-in-state! [key-seq update-fn value]
  (swap! TEST-STATE #(update-in %1 key-seq update-fn value))
  value)

(defn append-in-state! [key-seq value]
  (update-in-state! key-seq conj value))

(defn remove-from-state! [key-seq value]
  (assoc-in-state! key-seq (remove #(= value %) (get-from-state key-seq))))

(defn all-state []
  @TEST-STATE)

(defn dump-test-state []
  (print-vals "TEST-STATE:" (all-state)))