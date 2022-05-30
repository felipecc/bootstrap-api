(ns bootstrap-api.api.documentos.documentos
  (:require [bootstrap-api.components.database.documentos :as doc]
            [bootstrap-api.components.database.documento-pg :as doc-pg]))


(def ^:private ok 200)
(def ^:private created 201)
(def ^:private not-found 404)


(def ^:private boostrap-api-documentos-get-all-documentos "application/vnd.boostrap-api.documentos.get-all-documentos.v1+json;charset=utf-8")
(def ^:private boostrap-api-documentos-get-all-documentos-pg "application/vnd.boostrap-api.documentos.get-all-documentos-pg.v1+json;charset=utf-8")

(defn get-all-documentos-oracle
  [app-config]
  (fn [request]
    (let [rs (doc/get-all-documentos-cabecalho (:db-oracle app-config))]
      {:status ok :body rs})))

(defn get-all-documentos-pg
  [app-config]
  (fn [request]
    (let [rs (doc-pg/get-all-documentos-pg (:db-postgres app-config))]
      {:status ok :body rs})))


(defn routes
  [app-config]
  [["/get-all-documentos-oracle" {:get {:handler (get-all-documentos-oracle app-config)}
                                  :swagger {:produces [boostrap-api-documentos-get-all-documentos]}}]
   ["/get-all-documentos-pg" {:get {:handler (get-all-documentos-pg app-config)}
                              :swagger {:produces [boostrap-api-documentos-get-all-documentos-pg]}}]])
  