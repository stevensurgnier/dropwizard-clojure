(ns dropwizard-clojure.healthcheck
  (:import [com.codahale.metrics.health HealthCheck]
           [com.codahale.metrics.health HealthCheck$Result]))

(defn- to-result [clojure-result]
  (cond
    (true? clojure-result)
    (HealthCheck$Result/healthy)

    (or (nil? clojure-result) (false? clojure-result))
    (HealthCheck$Result/unhealthy "unhealthy")

    (vector? clojure-result)
    (let [[healthy? message-or-throwable] clojure-result]
      (if healthy?
        (HealthCheck$Result/healthy message-or-throwable)
        (HealthCheck$Result/unhealthy message-or-throwable)))

    :else
    (HealthCheck$Result/unhealthy
     (format "invalid healthcheck return value `%s'" clojure-result))))

(defn healthcheck [healthcheck-fn]
  (proxy [HealthCheck] []
    (check [] (to-result (healthcheck-fn)))))
