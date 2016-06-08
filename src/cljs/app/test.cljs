(ns app.test
  (:require [cljs.test :as t :refer [report]]
            [reagent.core :as reagent :refer [atom]]
            [app.state :as state :refer [test-state]]))

(enable-console-print!)


;; temporary state, we don't want to mess with the app state during testing
(defonce fail-messages (atom '()))
(defonce test-is-running (atom false))

(defmethod report [:cljs.test/default :fail] [m]
  (t/inc-report-counter! :pass))

(defn update-test-failure [m]
  (let [msg (-> m
                (select-keys [:message :expected :actual :type])
                (assoc :test-name (t/testing-vars-str m)
                       :test-context (t/testing-contexts-str)))]
    (swap! fail-messages conj msg)))

(defmethod report [:cljs.test/default :fail] [m]
  (t/inc-report-counter! :fail)
  (update-test-failure m))

(defmethod report [:cljs.test/default :error] [m]
  (t/inc-report-counter! :error)
  (let [actual (:actual m)
        m (assoc m :actual (if (instance? js/Error actual)
                             (do (println "Stacktrace for test: "
                                          (:test-name m) "\n")
                                 (println (.-stack actual))
                                 (.-stack actual))
                             actual))]
    (update-test-failure m)))

(defmethod report [:cljs.test/default :begin-test-ns] [m])

(defn set-test-state! [{:keys [test pass fail error] :as m}]
  (reset! test-state {:pass pass
                      :fail fail
                      :error error
                      :fail-messages @fail-messages}))

(defmethod report [:cljs.test/default :end-run-tests] [m]
  (println "Final Test summary: " (select-keys m [:pass :fail :error]))
  (set-test-state! m)
  (reset! test-is-running false))

(defmethod report [:cljs.test/default :summary] [m]
  (println "Test summary: " (select-keys m [:pass :fail :error]))
  (set-test-state! m))


(defn run-all! []
  (when-not @test-is-running ;; TODO: rerun test later otherwise
    (enable-console-print!)
    (println "Running tests...")
    (reset! fail-messages '())
    (reset! test-is-running true)
    (t/run-all-tests))
  )
