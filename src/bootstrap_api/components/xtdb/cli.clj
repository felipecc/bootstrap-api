(ns bootstrap-api.components.xtdb.cli
  (:require [xtdb.api :as xt]))

(defn conf-store-jdbc [db-spec]
  {:xtdb.jdbc/connection-pool {:dialect {:xtdb/module 'xtdb.jdbc.psql/->dialect}
                                         :db-spec db-spec}
                       :xtdb/tx-log {:xtdb/module 'xtdb.jdbc/->tx-log
                                     :connection-pool :xtdb.jdbc/connection-pool}
                       :xtdb/document-store {:xtdb/module 'xtdb.jdbc/->document-store
                                             :connection-pool :xtdb.jdbc/connection-pool}})

(defn start
  [db-spec]
  (xt/start-node (conf-store-jdbc db-spec)))

(defn stop
  [node]
  (.close node))