(ns dropwizard-clojure.test.healthcheck
  (:require [clojure.test :refer :all]
            [dropwizard-clojure.healthcheck])
  (:import [com.codahale.metrics.health HealthCheck]
           [com.codahale.metrics.health HealthCheck$Result]))

;; deref the private vars
(def result @#'dropwizard-clojure.healthcheck/result)
(def default-unhealthy-message
  @#'dropwizard-clojure.healthcheck/default-unhealthy-message)

(defn to-result-vector
  [^HealthCheck$Result result]
  [(.isHealthy result) (.getMessage result) (.getError result)])

(defn is-result
  [expected-output input]
  (is (= expected-output (to-result-vector (result input)))))

(defn test-single-values
  [expected-output inputs]
  (doall (map (partial is-result expected-output)
              (mapcat #(map % inputs) [identity vector list]))))

(defn test-with-messages
  [expected-output input]
  (let [message (second expected-output)]
    (doall
     (map (partial is-result expected-output)
          (mapcat #(map % (partition 2 (interleave input
                                                   (repeat (count input)
                                                           message))))
                  [identity (partial into [])
                   (comp reverse (partial into '()))])))))

(deftest test-healthcheck
  (testing "healhthy return values"
    (let [input [true "a" :a 1 1.0 1/2 #{1 2 3} {:a 1 :b 2}]]
      (test-single-values [true nil nil] input)
      (test-with-messages [true "good" nil] input)))
  (testing "unhealthy return values"
    (let [input [false nil]]
      (test-single-values [false default-unhealthy-message nil] input)
      (test-with-messages [false "bad" nil] input))))
