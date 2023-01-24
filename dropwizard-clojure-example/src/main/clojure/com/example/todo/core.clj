(ns com.example.todo.core
  (:require [dropwizard-clojure.core
             :refer [defapplication defmain register-resource
                     register-healthcheck]]
            [com.example.todo.resources.todo :refer [todo-resource]]
            [com.example.todo.health.todo-size :refer [todo-size]])
  (:import [com.example.todo AbstractTodoApplication TodoConfiguration]
           [io.dropwizard.setup Environment])
  (:gen-class))

(defapplication todo-app
  (fn [^TodoConfiguration config ^Environment env]
    (let [resource (todo-resource)
          healthcheck #(todo-size (.getMaxSize config) resource)]
      (-> env
          (register-resource resource)
          (register-healthcheck :todo-size healthcheck)))))

(defmain todo-app)
