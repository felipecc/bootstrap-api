(ns bootstrap-api.components.database.documento-pg
  (:require [hugsql.core :as hugsql]))

(hugsql/def-db-fns "bootstrap_api/components/database/sql/documentos_pg.sql")
(hugsql/def-sqlvec-fns "bootstrap_api/components/database/sql/documentos_pg.sql")