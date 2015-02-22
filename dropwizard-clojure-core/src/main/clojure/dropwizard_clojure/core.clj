(ns dropwizard-clojure.core
  (:import [io.dropwizard Application]))

(defn- application
  ([app-name app-class run-fn]
   (application app-name app-class nil run-fn))
  ([app-name app-class init-fn run-fn]
   `(def ~app-name
      (proxy [~app-class] []
        (initialize [bootstrap#] ~init-fn)
        (run [configuration# environment#]
          (~run-fn configuration# environment#))))))

(defmacro defapplication
  [& args]
  (apply application args))

(defmacro defmain [app]
  `(def ~'-main
     (fn [& args#] (.run ^Application ~app (into-array String args#)))))
