(ns com.example.helloworld.resources.helloworld-resource
  (:import [com.example.helloworld.representations Saying]
           [com.codahale.metrics.annotation Timed]
           [javax.ws.rs GET Path Produces QueryParam]
           [javax.ws.rs.core MediaType]
           [java.util.concurrent.atomic AtomicLong]))

(definterface SayHello
  (sayHello [^String name]))

(deftype ^{Path "/hello-world"
           Produces ["application/json"]}
    HelloWorldResource
  [^String template ^String default-name ^AtomicLong counter]
  SayHello
  (^{GET true Timed true}
   sayHello [this ^{QueryParam "name"} name]
   (let [value (format template (or name default-name))]
     (Saying. (.incrementAndGet counter) value))))

(defn helloworld-resource
  [template default-name]
  (HelloWorldResource. template default-name (AtomicLong.)))
