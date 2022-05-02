(ns bootstrap-api.components.database.documentos
  (:require [hugsql.core :as hugsql]
            [hugsql.adapter.next-jdbc :as next-adapter]
            [next.jdbc.result-set :as rs]))

(hugsql/def-db-fns "bootstrap_api/components/database/sql/documentos.sql")
(hugsql/def-sqlvec-fns "bootstrap_api/components/database/sql/documentos.sql")