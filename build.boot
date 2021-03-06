(set-env!
 :source-paths    #{"src/cljs" "src/styles"}
 :resource-paths  #{"resources"}
 :dependencies '[[org.clojure/clojurescript     "1.8.51"]
                 [reagent                       "0.6.0-alpha"]
                 [adzerk/boot-cljs              "1.7.228-1" :scope "test"]
                 [adzerk/boot-cljs-repl         "0.3.0"     :scope "test"]
                 [org.clojars.samroberton/boot-reload "0.4.9-SNAPSHOT" :scope "test"]
                 [devcards                      "0.2.1-7"   :scope "test"]
                 [pandeiro/boot-http            "0.7.3"     :scope "test"]
                 [boot-deps                     "0.1.6"     :scope "test"]
                 [org.martinklepsch/boot-garden "1.3.0-0"   :scope "test"]
                 [org.clojure/tools.nrepl       "0.2.12"    :scope "test"]
                 [com.cemerick/piggieback       "0.2.1"     :scope "test"]
                 [weasel                        "0.7.0"     :scope "test"]
])

(System/setProperty "BOOT_EMIT_TARGET" "no")

(require
 '[adzerk.boot-cljs              :refer [cljs]]
 '[adzerk.boot-cljs-repl         :refer [cljs-repl start-repl]]
 '[adzerk.boot-reload            :refer [reload]]
 '[org.martinklepsch.boot-garden :refer [garden]]
 '[pandeiro.boot-http            :refer [serve]]
 '[boot-deps                     :refer [ancient]])



(deftask prod-build []
  (comp (garden :styles-var 'app.styles/screen
                :output-to "css/screen.css")
        (cljs :ids #{"main"}
              :optimizations :simple)
        (cljs :ids #{"js/renderer"}
              :optimizations :advanced)))

(deftask renderer-build []
  (println "source-path, renderer: " (get-env :source-paths))
  (comp
   (cljs-repl :ids #{"js/renderer"})
   (reload    :ids #{"js/renderer"}
              :on-jsload 'app.core/reload!
              :only-by-re [#"^js/renderer.out/.*"]
              :ws-host "localhost")
   (cljs      :ids #{"js/renderer"})))


(deftask devcards-build []
  (println "source-path, devcards: " (get-env :source-paths))
  (comp
   (reload    :ids #{"js/devcards"}
              :on-jsload 'app.devcards/reload!
              :only-by-re [#"^js/devcards.out/.*"]
              :ws-host "localhost")
   (cljs      :ids #{"js/devcards"}
              :compiler-options {:devcards true})))

(deftask electron-main-build []
  (println "source-path, main: " (get-env :source-paths))
  (comp
   (cljs      :ids #{"main"}
              :compiler-options {:output-wrapper true
                                 :closure-defines {'app.main/dev? true}})))

(deftask css-build []
  (garden    :styles-var 'app.styles/screen
             :output-to "css/screen.css"))

#_(deftask test-build []
  (set-env! :source-paths #(conj % "test"))
  (comp
   (reload    :ids #{"js/test"}
              :on-jsload 'app.test/run!
              :only-by-re [#"^js/test/.*"]
              :ws-host "localhost")
   (cljs      :ids #{"test"})))

(deftask dev []
  (set-env! :source-paths #(conj % "test"))
  (comp
   (serve :resource-root)
   (watch)
   (speak)
   (electron-main-build)
   (devcards-build)
   (renderer-build)
   (css-build)
   (target :dir #{"target"})))

(deftask prod []
  (comp
   (speak)
   (prod-build)
   (target :dir #{"target"})))
