(ns com.example.helloworld.application
  (:require [com.example.helloworld.resources.helloworld
             :refer [helloworld-resource]]
            [com.example.helloworld.health.template-healthcheck
             :refer [template-healthcheck]])
  (:import [com.example.helloworld AbstractHelloWorldApplication]
           [io.dropwizard Configuration]
           [io.dropwizard.setup Bootstrap Environment])
  (:gen-class))

(defn application []
  (proxy [AbstractHelloWorldApplication] []
    (initialize [^Bootstrap bootstrap])
    (run [^Configuration configuration ^Environment environment]
      (let [hw-resource (helloworld-resource (.getTemplate configuration)
                                             (.getDefaultName configuration))
            healthcheck (template-healthcheck (.getTemplate configuration))]
        (.register (.jersey environment) hw-resource)
        (.register (.healthChecks environment) "template" healthcheck)))))

(defn -main [& args]
  (.run ^AbstractHelloWorldApplication (application)
        (into-array String args)))
