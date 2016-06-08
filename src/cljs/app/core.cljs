(ns app.core
  (:require [app.renderer :as renderer]
            [app.test :as test]
            [app.renderer-test]))

(defn init! []
  (renderer/init!)
  (test/run-all!))

(defn reload! []
  (renderer/reload!)
  (test/run-all!))
