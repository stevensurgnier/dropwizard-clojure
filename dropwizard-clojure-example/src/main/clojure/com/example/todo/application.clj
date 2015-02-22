(ns com.example.todo.application
  (:require [com.example.todo.resources.todo
             :refer [todo-resource]]
            [com.example.todo.health.todo-size
             :refer [todo-size]])
  (:import [com.example.todo AbstractTodoApplication]
           [io.dropwizard Configuration]
           [io.dropwizard.setup Bootstrap Environment])
  (:gen-class))

(defn application []
  (proxy [AbstractTodoApplication] []
    (initialize [^Bootstrap bootstrap])
    (run [^Configuration configuration ^Environment environment]
      (let [resource (todo-resource)
            healthcheck (todo-size (.getMaxSize configuration) resource)]
        (.register (.jersey environment) resource)
        (.register (.healthChecks environment) "todo-size" healthcheck)))))

(defn -main [& args]
  (.run ^AbstractTodoApplication (application)
        (into-array String args)))
