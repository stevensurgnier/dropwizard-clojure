(ns com.example.helloworld.resources.todo
  (:import [com.example.helloworld.representations Todo]
           [com.codahale.metrics.annotation Timed]
           [javax.ws.rs GET POST DELETE Path Consumes Produces PathParam]
           [javax.validation Valid]))

(definterface ITodo
  (get [])
  (delete [])
  (add [^Long id ^com.example.helloworld.representations.Todo todo])
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
