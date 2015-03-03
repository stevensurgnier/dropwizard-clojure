(ns dropwizard-clojure.core
  (:require [dropwizard-clojure.healthcheck :refer [healthcheck]])
  (:import [io.dropwizard Application]
           [io.dropwizard.setup Environment]
           [io.dropwizard.jersey.setup JerseyEnvironment]
           [com.codahale.metrics.health HealthCheckRegistry]))

(defn- application
  ([app-name app-class run-fn]
   (application app-name app-class '(constantly nil) run-fn))
  ([app-name app-class init-fn run-fn]
   `(def ~app-name
      (proxy [~app-class] []
        (initialize [bootstrap#] (~init-fn bootstrap#))
        (run [configuration# environment#]
          (~run-fn configuration# environment#))))))

(defmacro defapplication
  [& args]
  (apply application args))

(defmacro defmain [app]
  `(def ~'-main
     (fn [& args#] (.run ^Application ~app (into-array String args#)))))

(defn register-resources
  [^Environment env resources]
  (let [^JerseyEnvironment jersey (.jersey env)]
    (dorun (map #(.register jersey %) resources))
    env))

(defn register-resource
  [^Environment env resource]
  (register-resources env [resource]))

(defn register-healthchecks
  [^Environment env healthchecks]
  (let [^HealthCheckRegistry hc (.healthChecks env)]
    (dorun (map (fn [[hc-name hc-fn]]
                  (.register hc (name hc-name) (healthcheck hc-fn)))
                healthchecks))
    env))

(defn register-healthcheck
  [^Environment env hc-name hc-fn]
  (register-healthchecks env {hc-name hc-fn}))
