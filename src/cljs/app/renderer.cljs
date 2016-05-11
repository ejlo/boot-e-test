(ns app.renderer
  (:require [reagent.core :as reagent :refer [atom]]))

(defn main-page []
  [:div "Hello World!"])

(defn mount-root! []
  (reagent/render [main-page] (.getElementById js/document "app")))

(defn init! []
  (js/console.log "Starting Application!")
  (mount-root!))

(defn reload! []
  (mount-root!))
