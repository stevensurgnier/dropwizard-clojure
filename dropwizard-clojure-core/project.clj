(defproject dropwizard-clojure/dropwizard-clojure-core "0.3.0-SNAPSHOT"
  :description "Dropwizard for Clojure"
  :url "https://github.com/stevensurgnier/dropwizard-clojure"
  :license {:name "Apache License 2.0"
            :url "http://www.apache.org/licenses/LICENSE-2.0.html"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [org.clojure/tools.logging "1.2.4"]
                 [jakarta.xml.bind/jakarta.xml.bind-api "2.3.2"]
                 [io.dropwizard/dropwizard-core "0.8.2"]]
  :source-paths ["src/main/clojure"]
  :java-source-paths ["src/main/java"]
  :test-paths ["src/test/clojure"]
  :profiles {:uberjar {:aot :all}}
  :global-vars {*warn-on-reflection* true})
