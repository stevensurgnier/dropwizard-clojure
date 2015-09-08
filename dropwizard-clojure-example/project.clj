(defproject dropwizard-clojure/dropwizard-clojure-example "0.2.0"
  :description "Dropwizard for Clojure"
  :url "https://github.com/stevensurgnier/dropwizard-clojure"
  :license {:name "Apache License 2.0"
            :url "http://www.apache.org/licenses/LICENSE-2.0.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]                 
                 [dropwizard-clojure/dropwizard-clojure "0.2.0"]]
  :source-paths ["src/main/clojure"]
  :java-source-paths ["src/main/java"]
  :main com.example.todo.core
  :profiles {:uberjar {:aot :all}}
  :global-vars {*warn-on-reflection* true})
