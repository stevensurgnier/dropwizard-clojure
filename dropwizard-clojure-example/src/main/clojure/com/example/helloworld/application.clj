(ns com.example.helloworld.application
  (:require [com.example.helloworld.resources.todo
             :refer [todo-resource]]
            [com.example.helloworld.health.todo-size
             :refer [todo-size]])
  (:import [com.example.helloworld AbstractHelloWorldApplication]
           [io.dropwizard Configuration]
           [io.dropwizard.setup Bootstrap Environment])
  (:gen-class))

(defn application []
  (proxy [AbstractHelloWorldApplication] []
    (initialize [^Bootstrap bootstrap])
    (run [^Configuration configuration ^Environment environment]
      (let [resource (todo-resource)
            healthcheck (todo-size (.getMaxSize configuration) resource)]
        (.register (.jersey environment) resource)
        (.register (.healthChecks environment) "todo-size" healthcheck)))))

(defn -main [& args]
  (.run ^AbstractHelloWorldApplication (application)
        (into-array String args)))
