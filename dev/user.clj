(ns user
  (:require [bootstrap-api.main :as main]
            [clojure.tools.namespace.repl :as nr]
            [next.jdbc.connection :as connection]
            [juxt.clip.repl :refer [start stop set-init! system]]
            [bootstrap-api.components.database.documentos :as doc]
            [bootstrap-api.components.database.cli :as database-cli])
  (:import (com.zaxxer.hikari HikariDataSource)
           (com.zaxxer.hikari HikariConfig)))

(comment
  
  (def profile :local) ;; rename "config/config-example.edn" to "config/config-local.edn" and change values to suit your setup.

  (def config (str "config/config" (when-not (= :default profile) (str "-" (name profile))) ".edn"))

  (def system-config (#'main/load-config {:config config :profile profile}))

  (set-init! (constantly system-config))

  (start)
  (def app-config (:app-config system))
  (intern (find-ns 'bootstrap-api.main) 'app-config app-config) ; shove the value of app-config into this namespace

  (do
    (stop)
    (start))
  )