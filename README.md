# dropwizard-clojure

[dropwizard.io](http://dropwizard.io/)

## Table of Contents

1. [Leiningen](#leiningen)
2. [Creating a Configuration class](#creating-a-configuration-class)
3. [Creating an Application class](#creating-an-application-class)
4. [Creating a Representation class](#creating-a-representation-class)
5. [Creating a Resource class](#creating-a-resource-class)
6. [Creating a HealthCheck](#creating-a-healthcheck)
7. [Building fat JARs](#building-fat-jars)
8. [Running your Application](#running-your-application)

## Leiningen

```clojure
[dropwizard-clojure/dropwizard-clojure "0.1.0"]
```

## Creating a Configuration class

[TodoConfiguration.java](dropwizard-clojure-example/src/main/java/com/example/todo/TodoConfiguration.java)

```java
package com.example.todo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import javax.validation.constraints.NotNull;

public class TodoConfiguration extends Configuration {
    @NotNull
    private int maxSize;

    @JsonProperty
    public int getMaxSize() {
        return maxSize;
    }

    @JsonProperty
    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }
}
```

Dropwizard will serialize a yml file into your configuration class before starting the application.

[dev-todo.yml](dropwizard-clojure-example/resources/dev-todo.yml)

```yml
maxSize: 4
```

## Creating an Application class

Your dropwizard application requires a concrete implementation of `io.dropwizard.Application` that is parameterized with your configuration class from above. Unfortunately, clojure does not yet support specifying parameterized types. Thus, a tiny abstract application class is recommended to specify the parameterized configuration class.

[AbstractTodoApplication.java](dropwizard-clojure-example/src/main/java/com/example/todo/AbstractTodoApplication.java)

```java
package com.example.todo;

import io.dropwizard.Application;

public abstract class AbstractTodoApplication
    extends Application<TodoConfiguration> {
}
```

Now we're ready to write some clojure.

The `defapplication` macro is provided to conveniently create proxy instances of your abstract application class. Two function signatures are available. One that allows you override the `initialize` and `run` methods. The other allows you to override the `run` method only. Below the latter is shown.

The `defmain` macro is provided to conveniently generate a `main` method that will start your application correctly.

[core.clj](dropwizard-clojure-example/src/main/clojure/com/example/todo/core.clj)

```clojure
(ns com.example.todo.core
  (:require [dropwizard-clojure.core :refer [defapplication defmain]]
  (:import [com.example.todo AbstractTodoApplication TodoConfiguration]
           [io.dropwizard.setup Environment])
  (:gen-class))

(defapplication todo-app
  AbstractTodoApplication
  (fn [^TodoConfiguration config ^Environment env]
    ;; nothing to do yet
    ))

(defmain todo-app)

```

## Creating a Representation class

[Todo.java](dropwizard-clojure-example/src/main/java/com/example/todo/representations/Todo.java)

```java
package com.example.todo.representations;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class Todo {
    @NotNull
    private Boolean complete;
    @NotEmpty
    private String description;

    public Todo() {
        // Jackson deserialization
    }

    public Todo(Boolean complete, String description) {
        this.complete = complete;
        this.description = description;
    }

    @JsonProperty
    public Boolean getComplete() {
        return complete;
    }

    @JsonProperty
    public String getDescription() {
        return description;
    }
}
```

## Creating a Resource class

[core.clj](dropwizard-clojure-example/src/main/clojure/com/example/todo/resources/todo.clj)

```clojure
(ns com.example.todo.resources.todo
  (:require [clojure.tools.logging :as log])
  (:import [com.example.todo.representations Todo]
           [com.codahale.metrics.annotation Timed]
           [javax.validation Valid]
           [javax.ws.rs GET POST DELETE Path Consumes Produces PathParam]))

(definterface ITodo
  (get [])
  (delete [])
  (add [^Long id ^com.example.todo.representations.Todo todo])
  (get [^Long id])
  (toggle [^Long id])
  (delete [^Long id]))

(deftype ^{Path "/todo"
           Consumes ["application/json"]
           Produces ["application/json"]}
    TodoResource [state]
    ITodo
    (^{GET true Timed true} get [this] @state)

    (^{DELETE true Timed true} delete [this]
     (reset! state (atom {}))
     {})
    
    (^{Path "{id}" POST true Timed true}
     add [this ^{PathParam "id"} id ^{Valid true} todo]
     (swap! state assoc id todo))
    
    (^{Path "{id}" GET true Timed true}
     get [this ^{PathParam "id"} id]
     (get @state id))
    
    (^{Path "{id}/toggle" POST true Timed true}
     toggle [this ^{PathParam "id"} id]
     (swap! state update-in [id]
       #(Todo. (not (.getComplete %)) (.getDescription %))))

    (^{Path "{id}" DELETE true Timed true}
     delete [this ^{PathParam "id"} id]
     (swap! state dissoc id)))

(defn todo-resource []
  (TodoResource. (atom {})))
```

### Registering a Resource

[core.clj](dropwizard-clojure-example/src/main/clojure/com/example/todo/core.clj)

```clojure
(ns com.example.todo.core
  (:require [dropwizard-clojure.core :refer [defapplication defmain]]
            [com.example.todo.resources.todo :refer [todo-resource]]
  (:import [com.example.todo AbstractTodoApplication TodoConfiguration]
           [io.dropwizard.setup Environment])
  (:gen-class))

(defapplication todo-app
  AbstractTodoApplication
  (fn [^TodoConfiguration config ^Environment env]
    (.register (.jersey env) (todo-resource))))

(defmain todo-app)
```

## Creating a HealthCheck

The `healthcheck` function is used to create proxy instances of `com.codahale.metrics.health.HealthCheck`. It accepts a function that when applied to zero arguments must return one of the following values:

- a single value ∈ {true, false, nil}
- a vector of the form [healthy? ∈ {true, false, nil}, message ∈ {string, throwable}]

[todo_size.clj](dropwizard-clojure-example/src/main/clojure/com/example/todo/health/todo_size.clj)

```clojure
(ns com.example.todo.health.todo-size
  (:require [dropwizard-clojure.healthcheck :refer [healthcheck]])
  (:import [com.example.todo.resources.todo TodoResource]))

(defn todo-size [max-size ^TodoResource resource]
  (healthcheck #(if (<= (count (.get resource)) max-size)
                  true
                  [false "too many todos"])))
```

### Registering a HealthCheck

[core.clj](dropwizard-clojure-example/src/main/clojure/com/example/todo/core.clj)

```clojure
(ns com.example.todo.core
  (:require [dropwizard-clojure.core :refer [defapplication defmain]]
            [com.example.todo.resources.todo :refer [todo-resource]]
            [com.example.todo.health.todo-size :refer [todo-size]])
  (:import [com.example.todo AbstractTodoApplication TodoConfiguration]
           [io.dropwizard.setup Environment])
  (:gen-class))

(defapplication todo-app
  AbstractTodoApplication
  (fn [^TodoConfiguration config ^Environment env]
    (let [resource (todo-resource)
          healthcheck (todo-size (.getMaxSize config) resource)]
      (.register (.jersey env) resource)
      (.register (.healthChecks env) "todo-size" healthcheck))))

(defmain todo-app)
```

## Building fat JARs

```sh
lein uberjar
```

## Running your Application

```sh
lein run server resources/dev-todo.yml
```

or

```sh
java -jar target/dropwizard-clojure-example-0.1.0-SNAPSHOT-standalone.jar server resources/dev-todo.yml
```
