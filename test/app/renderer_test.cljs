(ns app.renderer-test
  (:require [app.renderer :as app]
            [reagent.core :as reagent :refer [atom]]
            [reagent.ratom :refer-macros [reaction]]
            [cljs.test :as test :refer-macros [deftest is use-fixtures]]
            [goog.dom :as gdom]))


(def test-container-id :test-container)

(defn get-element [id]
  (gdom/getElement (name id)))

(defn test-elem []
  (get-element test-container-id))

(defn nodes->seq [nodes]
  (for [i (range (.-length nodes))]
    (aget nodes i)))

(defn children-by-class [parent child-class]
  (nodes->seq (gdom/getElementsByClass (name child-class) parent)))

(defn body-elt []
  (aget (.getElementsByTagName js/document "body") 0))

#_(def temporary-container-fixture
  {:before
   #(test/async test/done
     (dom/append (body-elt) [:div {:id test-container-id}])
     (dom/hide test-container-id)
     (done))
   :after
   #(dom/remove-element test-container-id)})

(deftest main-test
  (print "main-test")
  (let [parent (test-elem)]
    (reagent/render-component
     [app/main-page]
     parent)
    (is (> (count (children-by-class parent :main)) 0))
    (is (= 1 1))))
