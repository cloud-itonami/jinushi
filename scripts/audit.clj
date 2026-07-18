(require '[clojure.edn :as edn]
         '[clojure.java.io :as io]
         '[clojure.string :as str])
(def root (.getCanonicalFile (io/file ".")))
(def files (file-seq root))
(def relative #(.toString (.relativize (.toPath root) (.toPath %))))
(doseq [f files :when (and (.isFile f) (str/ends-with? (.getName f) ".edn"))]
  (try (edn/read-string (slurp f))
       (catch Exception e (throw (ex-info "invalid EDN" {:file (relative f)} e)))))
(let [bad (for [f files :when (.isFile f) :let [p (relative f)]
                :when (or (re-find #"\.(go|py|sh)$" p)
                          (re-find #"(^|/)requirements[^/]*\.txt$" p))] p)
      json (for [f files :when (.isFile f) :let [p (relative f)]
                 :when (and (re-find #"\.(json|jsonld)$" p)
                            (not (str/starts-with? p "wire/"))
                            (not= p ".well-known/did.json"))] p)]
  (when (seq bad) (throw (ex-info "deprecated artifacts" {:files bad})))
  (when (seq json) (throw (ex-info "JSON outside wire" {:files json}))))
(println "audit: ok")
