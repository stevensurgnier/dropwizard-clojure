(defproject dropwizard-clojure/dropwizard-clojure "0.1.1"
  :description "Dropwizard for Clojure"
  :url "https://github.com/stevensurgnier/dropwizard-clojure"
  :license {:name "Apache License 2.0"
            :url "http://www.apache.org/licenses/LICENSE-2.0.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/tools.logging "0.3.1"]
                 [io.dropwizard/dropwizard-core "0.7.1"]]
  :source-paths ["src/main/clojure"]
  :test-paths ["src/test/clojure"]
  :profiles {:uberjar {:aot :all}}
  :global-vars {*warn-on-reflection* true})
