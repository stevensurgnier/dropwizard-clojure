(ns com.example.todo.health.todo-size
  (:require [dropwizard-clojure.healthcheck :refer [healthcheck]])
  (:import [com.example.todo.resources.todo TodoResource]))

(defn todo-size [max-size ^TodoResource resource]
  (healthcheck #(if (<= (count (.get resource)) max-size)
                  true
                  [false "too many todos"])))
