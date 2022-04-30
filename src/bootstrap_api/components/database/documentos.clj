(ns bootstrap-api.components.database.documentos
  (:require [hugsql.core :as hugsql]
            [hugsql.adapter.next-jdbc :as next-adapter]
            [next.jdbc.result-set :as rs]))


(hugsql/set-adapter! (next-adapter/hugsql-adapter-next-jdbc
                          {:builder-fn (rs/as-maps-adapter
                                        rs/as-lower-maps
                                        rs/clob-column-reader)}))

(hugsql/def-db-fns "bootstrap_api/components/database/sql/documentos.sql")
(hugsql/def-sqlvec-fns "bootstrap_api/components/database/sql/documentos.sql")