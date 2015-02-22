(ns com.example.helloworld.health.todo-size
  (:import [com.codahale.metrics.health HealthCheck]
           [com.codahale.metrics.health HealthCheck$Result]
           [com.example.helloworld.resources.todo TodoResource]))

(defn todo-size [max-size resource]
  (proxy [HealthCheck] []
    (check []
      (if (<= (count (.get ^TodoResource resource)) max-size)
        (HealthCheck$Result/healthy)
        (HealthCheck$Result/unhealthy "too many todos")))))
