(ns bootstrap-api.api.documentos.documentos
  (:require [bootstrap-api.components.database.documentos :as doc]))


(def ^:private ok 200)
(def ^:private created 201)
(def ^:private not-found 404)


(def ^:private boostrap-api-documentos-get-all-documentos "application/vnd.boostrap-api.documentos.get-all-documentos.v1+json;charset=utf-8")

(defn get-all-documentos
  [app-config]
  (fn [request]
    (let [rs (doc/get-all-documentos (:db app-config))]
      {:status ok :body rs})))

(defn routes
  [app-config]
  ["/get-all-documentos" {:get {:handler (get-all-documentos app-config)}
                                :swagger {:produces [boostrap-api-documentos-get-all-documentos]}}])
  