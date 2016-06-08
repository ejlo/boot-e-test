(ns app.renderer
  (:require [reagent.core :as reagent :refer [atom]]
            [app.state :as state :refer [test-state]]
            [app.test :as test]))

(defn test-component []
  (let [{:keys [pass fail error fail-messages]} @test-state
        class (if (or (pos? error) (pos? fail))
                :failed
                (when (pos? pass) :passed))]
    (if true #_@test-state
      [:div.test {:class class}
       [:div.test-count
        [:div.pass "Pass: " pass]
        [:div.fail "Fail: " fail]
        [:div.error "Error: " error]
        (when (= 0 fail pass error) [:div "No tests"])]
       #_[fail-message-list]]
      [:div.notests])))

(defn main-page []
  [:div.main "Hello World!"
   [:p>a {:href "cards.html"} "Devcards"]
   [test-component]])

(defn mount-root! []
  (when-let [app (.getElementById js/document "app")]
    (reagent/render [main-page] app)))

(defn init! []
  (js/console.log "Starting Application!")
  (mount-root!))

(defn reload! []
  (mount-root!))
