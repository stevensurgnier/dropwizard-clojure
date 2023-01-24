(ns com.example.todo.health.todo-size
  (:import [com.example.todo.resources.todo TodoResource]))

(defn todo-size [max-size ^TodoResource resource]
  (if (<= (count (.get resource)) max-size)
    [true (str max-size)]
    [false "too many todos"]))
