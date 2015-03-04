(ns com.example.todo.health.todo-size
  (:import [com.example.todo.resources.todo TodoResource]))

(defn todo-size [max-size ^TodoResource resource]
  (if (<= (count (.get resource)) max-size)
    true
    [false "too many todos"]))
