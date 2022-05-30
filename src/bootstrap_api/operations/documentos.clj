(ns bootstrap-api.operations.documentos
  (:require [bootstrap-api.components.database.documentos :as doc-ora-sql]
            [bootstrap-api.components.database.documento-pg :as doc-pg-sql]
            [bootstrap-api.utils.formatters :as frmt]
            [xtdb.api :as xt]
            [clojure.string :as str]))


(defn extract-metadado-doc
  [m]
  (reduce (fn[mf row]
            (let [{:keys [cd_campo
                          ds_campo
                          ds_identificador
                          cd_tipo
                          tipo
                          cd_tipo_item
                          tipo_item
                          ds_identificador_ord
                          lo_valor]} row
                  key-campo (str/lower-case (frmt/field-only-letters ds_campo))]
              (if-not (contains? mf key-campo)
                (assoc mf key-campo {:cd_campo cd_campo
                                     :ds_campo (frmt/field-only-letters ds_campo)
                                     :ds_identificador (frmt/field-only-letters ds_identificador)
                                     :cd_tipo_campo cd_tipo
                                     :ds_tipo_campo tipo
                                     :cd_visualizacao_campo cd_tipo_item
                                     :ds_visualizacao_campo tipo_item
                                     :valor (frmt/extract-value-for-field-type lo_valor tipo)})
                (update-in mf [key-campo :valor] #(format "%s\n%s" % (frmt/extract-value-for-field-type lo_valor tipo))))))
          {}
          m))

(defn batch-insert-documentos-ref-data-fechamento
  [data-fechamento ds-oracle ds-pg ds-xtdb]
  (doseq [doc-cabecalho (doc-ora-sql/get-all-documentos-cabecalho ds-oracle)]
    (let [doc-detalhe (doc-ora-sql/get-documento-detalhe ds-oracle {:cd_editor_registro (:cd_editor_registro doc-cabecalho)})
          {:keys [cd_documento
                  cd_editor_registro
                  ds_documento
                  cd_documento_clinico
                  cd_atendimento
                  cd_paciente
                  dh_fechamento
                  tp_status
                  cd_layout
                  cd_versao_documento
                  cd_multi_empresa]} doc-cabecalho
          metadado-doc (extract-metadado-doc doc-detalhe)
          detail-xtdb (reduce-kv (fn [m k v]
                                   (assoc m (frmt/keywordize-domain-field "campo" (get v :ds_campo)) (get v :valor)))
                                 {}
                                 metadado-doc)
          master-xtdb {(frmt/keywordize-domain-field "documento" "cd_documento") cd_documento
                       (frmt/keywordize-domain-field "documento" "cd_editor_registro") cd_editor_registro
                       (frmt/keywordize-domain-field "documento" "ds_documento") ds_documento
                       (frmt/keywordize-domain-field "documento" "cd_documento_clinico") cd_documento_clinico
                       (frmt/keywordize-domain-field "documento" "cd_atendimento") cd_atendimento
                       (frmt/keywordize-domain-field "documento" "cd_paciente") cd_paciente
                       (frmt/keywordize-domain-field "documento" "dh_fechamento") dh_fechamento
                       (frmt/keywordize-domain-field "documento" "tp_status") tp_status
                       (frmt/keywordize-domain-field "documento" "cd_layout") cd_layout
                       (frmt/keywordize-domain-field "documento" "cd_versao_documento") cd_versao_documento
                       (frmt/keywordize-domain-field "documento" "cd_multi_empresa") cd_multi_empresa
                       :xt/id cd_editor_registro}]

      (doseq [[k v] metadado-doc]
        (let [{:keys [cd_campo
                      ds_campo
                      ds_identificador
                      cd_tipo_campo
                      ds_tipo_campo
                      cd_visualizacao_campo
                      ds_visualizacao_campo]} v]
          (doc-pg-sql/insert-documento-metadado ds-pg {:cd_documento cd_documento
                                                       :ds_documento ds_documento
                                                       :cd_campo cd_campo
                                                       :ds_campo ds_campo
                                                       :ds_identificador ds_identificador
                                                       :cd_tipo_campo cd_tipo_campo
                                                       :ds_tipo_campo ds_tipo_campo
                                                       :cd_visualizacao_campo cd_visualizacao_campo
                                                       :ds_visualizacao_campo ds_visualizacao_campo})))
      
      (xt/submit-tx ds-xtdb [[::xt/put (merge master-xtdb detail-xtdb)]])
      
      )))
