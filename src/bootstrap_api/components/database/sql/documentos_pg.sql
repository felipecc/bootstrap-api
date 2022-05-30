-- :name get-all-documentos-pg :? :*
-- :doc Get all documents pg
select cd_campo
     , ds_campo 
from public.campos
limit 10

-- :name insert-documento-metadado :! :n
-- :doc Insert documento metadado
insert into public.documento_metadado (cd_documento,
                                       ds_documento,
                                       cd_campo,
                                       ds_campo,
                                       ds_identificador,
                                       cd_tipo_campo,
                                       ds_tipo_campo,
                                       cd_visualizacao_campo,
                                       ds_visualizacao_campo)
values (:cd_documento,
        :ds_documento,
        :cd_campo,
        :ds_campo,
        :ds_identificador,
        :cd_tipo_campo,
        :ds_tipo_campo,
        :cd_visualizacao_campo,
        :ds_visualizacao_campo)