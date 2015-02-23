(defproject dropwizard-clojure/dropwizard-clojure "0.1.0"
  :description "Dropwizard for Clojure"
  :url "https://github.com/stevensurgnier/dropwizard-clojure"
  :license {:name "Apache License 2.0"
            :url "http://www.apache.org/licenses/LICENSE-2.0.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/tools.logging "0.3.1"]
                 [io.dropwizard/dropwizard-core "0.7.1"]]
  :source-paths ["src/main/clojure"]
  :profiles {:uberjar {:aot :all}}
  :global-vars {*warn-on-reflection* true})
