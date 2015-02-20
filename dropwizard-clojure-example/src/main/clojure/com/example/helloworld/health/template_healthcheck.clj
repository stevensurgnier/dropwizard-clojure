(ns com.example.helloworld.health.template-healthcheck
  (:import [com.codahale.metrics.health HealthCheck]
           [com.codahale.metrics.health HealthCheck$Result]))

(defn template-healthcheck [template]
  (proxy [HealthCheck] []
    (check []
      (if (.contains (format template "TEST") "TEST")
        (HealthCheck$Result/healthy)
        (HealthCheck$Result/unhealthy "template doesn't include a name")))))
