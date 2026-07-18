(ns jinushi.test-runner
  (:require [clojure.java.io :as io]
            [clojure.test :as test]))

(defn test-namespaces []
  (->> (file-seq (io/file "test/jinushi/methods"))
       (filter #(and (.isFile %) (re-matches #"test_.*\.clj[cs]?" (.getName %))))
       (keep #(some-> (re-find #"\(ns\s+([^\s\)]+)" (slurp %)) second symbol))
       sort vec))

(defn -main [& _]
  (let [suites (test-namespaces)]
    (doseq [suite suites] (require suite))
    (let [{:keys [fail error]} (apply test/run-tests suites)]
      (shutdown-agents)
      (System/exit (if (zero? (+ fail error)) 0 1)))))
