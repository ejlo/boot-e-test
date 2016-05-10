(set-env!
 :source-paths    #{"src"}
 :resource-paths  #{"resources"}
 :dependencies '[[adzerk/boot-cljs "1.7.228-1" :scope "test"]
                 [org.clojure/clojurescript "1.8.51"]
                 [adzerk/boot-cljs-repl "0.3.0" :scope "test"]
                 [adzerk/boot-reload    "0.4.7"  :scope "test"]
                 [org.clojure/tools.nrepl "0.2.12"     :scope "test"]
                 [com.cemerick/piggieback "0.2.1" :scope "test"]
                 [weasel "0.7.0" :scope "test"]
                 [boot-deps                     "0.1.6"      :scope "test"]
])

(System/setProperty "BOOT_EMIT_TARGET" "no")

(require
 '[adzerk.boot-cljs      :refer [cljs]]
 '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl]]
 '[adzerk.boot-reload    :refer [reload]]
 '[boot-deps             :refer [ancient]])

(deftask prod-build []
  (comp (cljs :ids #{"main"}
              :optimizations :simple)
        (cljs :ids #{"renderer"}
              :optimizations :advanced)))

(deftask dev-build []
  (comp ;; Audio feedback about warnings etc. =======================
        (speak)
        ;; Inject REPL and reloading code into renderer build =======
        (cljs-repl :ids #{"renderer"})
        (reload    :ids #{"renderer"}
                   :on-jsload 'app.renderer/init
                   :ws-host "localhost")
        ;; Compile renderer =========================================
        (cljs      :ids #{"renderer"})
        ;; Compile JS for main process ==============================
        ;; path.resolve(".") which is used in CLJS's node shim
        ;; returns the directory `electron` was invoked in and
        ;; not the directory our main.js file is in.
        ;; Because of this we need to override the compilers `:asset-path option`
        ;; See http://dev.clojure.org/jira/browse/CLJS-1444 for details.
        (cljs      :ids #{"main"}
                   :compiler-options {:asset-path "target/main.out"
                                      :closure-defines {'app.main/dev? true}})))

(deftask dev []
  (comp
   (watch)
   (dev-build)
   (target)))