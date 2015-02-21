(ns com.example.helloworld.resources.todo
  (:import [com.codahale.metrics.annotation Timed]
           [javax.ws.rs GET POST Path Consumes Produces PathParam]))

(definterface ITodo
  (add [^Long id ^String body])
  (get [^Long id]))

(deftype ^{Path "/todo"
           Consumes ["application/json"]
           Produces ["application/json"]}
    TodoResource [state]
  ITodo
  (^{Path "{id}" GET true Timed true}
   get [this ^{PathParam "id"} id]
   (get @state id))

  (^{Path "{id}" POST true Timed true}
   add [this ^{PathParam "id"} id body]
   (swap! state assoc id body)))

(defn todo-resource []
  (TodoResource. (atom {})))
