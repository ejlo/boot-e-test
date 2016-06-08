(ns app.state
    (:require [reagent.core :as reagent :refer [atom]]))

(defonce app-state (atom {}))

(defn cursor [path]
  (reagent/cursor app-state path))

(def test-state (cursor [:test]))
