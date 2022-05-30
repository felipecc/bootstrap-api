(ns user
  (:require [bootstrap-api.main :as main]
            [clojure.tools.namespace.repl :as nr]
            [next.jdbc.connection :as connection]
            [clojure.tools.namespace.repl :refer [refresh]]
            [juxt.clip.repl :refer [start stop set-init! system]]
            [bootstrap-api.components.database.documentos :as doc]
            [bootstrap-api.components.database.cli :as database-cli]
            [bootstrap-api.operations.documentos :as op-docs])
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

  (op-docs/batch-insert-documentos-ref-data-fechamento "data-fechamento" (:db-oracle app-config) (:db-postgres app-config) (:db-xtdb app-config))

  ;; (def mx (doc/get-documento-detalhe (:db-oracle app-config) {:cd_editor_registro 21564761}))
  (def mx (doc/get-documento-detalhe (:db-oracle app-config) {:cd_editor_registro 21572466}))


  (op-docs/extract-metadado-doc mx)
  (require '[bootstrap-api.utils.formatters :as frmt])
  ;; (require '[clojure.string :as strl])

  ;; (defn extract-metadado-docx
  ;; [m]
  ;; (reduce (fn [mf row]
  ;;           (let [{:keys [cd_campo
  ;;                         ds_campo
  ;;                         ds_identificador
  ;;                         cd_tipo
  ;;                         tipo
  ;;                         cd_tipo_item
  ;;                         tipo_item]} row
  ;;                 key-campo (keyword (strl/lower-case (frmt/field-only-letters ds_campo)))]
  ;;             (if-not (contains? mf key-campo)
  ;;               (assoc mf key-campo {:cd_campo cd_campo
  ;;                                   :ds_campo (frmt/field-only-letters ds_campo)
  ;;                                   :ds_identificador (frmt/field-only-letters ds_identificador)
  ;;                                   :cd_tipo_campo cd_tipo
  ;;                                   :ds_tipo_campo tipo
  ;;                                   :cd_visualizacao_campo cd_tipo_item
  ;;                                   :ds_visualizacao_campo tipo_item})
  ;;               mf)))
  ;;         {}
  ;;         m))

  ;; (defn extract-metadado-docx-2
  ;; [m]
  ;; (reduce-kv (fn [mf k v]
  ;;             (prn k v)
  ;;             (if-not (contains? mf k)
  ;;               (assoc mf k v)
  ;;               mf))
  ;;         {}
  ;;         m))

  ;; (def r (extract-metadado-docx mx))

  ;; (prn r)

  (doseq [[k v] {:a 1 :b 2 :c 3}]
    (println (str k " => " v)))

  (def astr "This contains      blanks \n \t \r and other whitespace")

  (->> "conselho_do_usuario_do_documento"
       (#(clojure.string/split % #"\s"))
       (remove clojure.string/blank?)
       (clojure.string/join "_"))

  (#(clojure.string/split % #"\s") "This contains      blanks \n \t \r and other whitespace")
  (remove clojure.string/blank? ["bola" "fora" "fora"])
  (if (clojure.string/blank? "") " ")

  (reduce (fn [m [k v]]
            (assoc m k (str v)))
          {}
          {:foo 1})

  (reduce-kv (fn [m k v]
               (assoc m k (str v)))
             {}
             {:foo 1})

  (def mx (doc/get-documento-detalhe (:db-oracle app-config) {:cd_editor_registro 7731595}))

  (op-docs/extract-metadado-doc mx)

  (require '[bootstrap-api.utils.formatters :as frmt])

  (reduce-kv (fn [m k v]
               (assoc m (frmt/keywordize-domain-field "documento" (get v :ds_campo)) (get v :lo_valor)))
             {}
             mx)


  ;; (xt/submit-tx
  ;;  (:db-xtdb app-config)
  ;;  [[::xt/delete 7731595M]])

  (xt/sync (:db-xtdb app-config))

  (clojure.string/split "a b c" #"\s")
  (clojure.string/join "_" ["a" "b" "c"])

  (require '[xtdb.api :as xt])

  (xt/q
   (xt/db (:db-xtdb app-config))
   '{:find [(pull ?doc [*])]
     :where [[?doc :documento/cd_paciente 510828M]]})

  (xt/q
   (xt/db (:db-xtdb app-config))
   '{:find [(pull ?doc [*])]
     :where [[?doc :xt/id 7731595M]]})

  
  )