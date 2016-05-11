(set-env!
 :source-paths    #{"src/cljs" "src/styles"}
 :resource-paths  #{"resources"}
 :dependencies '[[org.clojure/clojurescript     "1.8.51"]
                 [adzerk/boot-cljs              "1.7.228-1" :scope "test"]
                 [adzerk/boot-cljs-repl         "0.3.0"     :scope "test"]
                 [adzerk/boot-reload            "0.4.7"     :scope "test"]
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
  (comp (cljs :ids #{"main"}
              :optimizations :simple)
        (cljs :ids #{"renderer"}
              :optimizations :advanced)))



(deftask dev-build []
  (comp
   ;; Inject REPL and reloading code into renderer build =======
   (cljs-repl :ids #{"js/renderer"})
   (reload    :ids #{"js/renderer"}
              :on-jsload 'app.renderer/init
              :ws-host "localhost")

   ;; Compile renderer =========================================
   (cljs      :ids #{"js/renderer"})

   ;; Compile css  =============================================
   (garden    :styles-var 'app.styles/screen
              :output-to "css/screen.css")

   ;; Compile JS for electron main process =====================
   (cljs      :ids #{"electron/main"}
              :compiler-options {:closure-defines {'app.main/dev? true}})))

(deftask dev []
  (comp
   (serve)
   (watch)
   (speak)
   (dev-build)
   (target :dir #{"target"})))
