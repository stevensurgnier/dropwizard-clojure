(ns com.example.helloworld.application
  (:require [com.example.helloworld.resources.helloworld-resource
             :refer [helloworld-resource]])
  (:import [com.example.helloworld AbstractHelloWorldApplication]
           [com.example.helloworld.health TemplateHealthCheck]
           [io.dropwizard Configuration]
           [io.dropwizard.setup Bootstrap Environment])
  (:gen-class))

(defn application []
  (proxy [AbstractHelloWorldApplication] []
    (initialize [^Bootstrap bootstrap])
    (run [^Configuration configuration ^Environment environment]
      (let [resource (helloworld-resource (.getTemplate configuration)
                                          (.getDefaultName configuration))
            healthcheck (TemplateHealthCheck. (.getTemplate configuration))]
        (.register (.jersey environment) resource)
        (.register (.healthChecks environment) "template" healthcheck)))))

(defn -main [& args]
  (.run ^AbstractHelloWorldApplication (application)
        (into-array String args)))
