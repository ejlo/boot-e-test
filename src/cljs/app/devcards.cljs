(ns app.devcards
  (:require [devcards.core :as dc :refer-macros [defcard defcard-rg]]
            [reagent.core :as reagent :refer [atom]]
            [reagent.ratom :refer-macros [reaction]]
            [app.renderer :as app]))

(enable-console-print!)

(defn testing-div []
  [:div
   "Testing a reagent devcard!"])

(defcard-rg div
  [testing-div])

(defcard-rg div*2
  [:div
   [testing-div]
   [testing-div]])

(defcard-rg main-page
  [app/main-page])


(defn main []
  (dc/start-devcard-ui!))

(defn reload! []
  (dc/start-devcard-ui!))
