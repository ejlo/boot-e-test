(ns app.renderer
  (:require [reagent.core :as reagent :refer [atom]]))

(defn main-page []
  [:div "Hello World!"
   [:p>a {:href "cards.html"} "Devcards"]])

(defn mount-root! []
  (when-let [app (.getElementById js/document "app")]
    (reagent/render [main-page] app)))

(defn init! []
  (js/console.log "Starting Application!")
  (mount-root!))

(defn reload! []
  (mount-root!))
