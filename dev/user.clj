(ns user
  (:require [bootstrap-api.main :as main]
            [juxt.clip.repl :refer [start stop set-init! system]]))

(comment

  (def profile :local) ;; rename "config/config-example.edn" to "config/config-local.edn" and change values to suit your setup.

  (def config (str "config/config" (when-not (= :default profile) (str "-" (name profile))) ".edn"))

  (def system-config (#'main/load-config {:config config :profile profile}))

  (set-init! (constantly system-config))

  (start)
  (def app-config (:app-config system))
  (intern (find-ns 'startrek.main) 'app-config app-config) ; shove the value of app-config into this namespace

  (do
    (stop)
    (start))
  )