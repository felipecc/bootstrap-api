-- :name get-all-documentos-cabecalho:? :*
-- :doc Get all documentos cabecalho
select ed.cd_documento
      ,edi.cd_editor_registro -- usado para recuperar o pdf no link do mveditor
      ,ed.ds_documento
      ,doc.cd_documento_clinico
      ,doc.cd_atendimento
      ,doc.cd_paciente
      ,doc.dh_fechamento
      ,doc.tp_status
      ,el.cd_layout
      ,el.cd_versao_documento 
      ,me.cd_multi_empresa   
from dbamv.pw_documento_clinico doc
join dbamv.pw_editor_clinico        edi on(edi.cd_documento_clinico  = doc.cd_documento_clinico)
join dbamv.editor_registro          erg on(erg.cd_registro = edi.cd_editor_registro)
join dbamv.editor_layout             el on(el.cd_layout = erg.cd_layout)
join dbamv.editor_versao_documento   ev on(el.cd_versao_documento   = ev.cd_versao_documento)
join dbamv.editor_documento          ed on(ed.cd_documento = ev.cd_documento)
join dbamv.atendime                  me on(me.cd_atendimento = doc.cd_atendimento)
where doc.tp_status = upper('fechado')
  and trunc(doc.dh_fechamento) = to_date('23/06/2020 00:00:00','dd/mm/yyyy hh24:mi:ss')  



-- :name get-documento-detalhe :? :*
-- :doc Get documento detalhe com parametro :cd_editor_registro
select  edi.cd_editor_registro -- usado para recuperar o pdf no link do mveditor
        ,ecm.cd_campo
        ,ecm.ds_campo
        ,regexp_substr(upper(ecm.ds_identificador),'(\_?[a-z])+') ds_identificador_a
        ,upper(ecm.ds_identificador) ds_identificador
        ,to_number(regexp_substr(regexp_substr(ecm.ds_identificador,'_\d{1,3}$'),'\d+')) ds_identificador_ord
        ,upper(cp.lo_valor) lo_valor
        ,etv.cd_tipo_visualizacao cd_tipo
        ,etv.ds_identificador tipo
        ,eti.cd_tipo_item
        ,eti.ds_identificador tipo_item
from dbamv.pw_documento_clinico doc
join dbamv.pw_editor_clinico        edi on(edi.cd_documento_clinico  = doc.cd_documento_clinico)
join dbamv.editor_registro          erg on(erg.cd_registro = edi.cd_editor_registro)
join dbamv.editor_layout             el on(el.cd_layout = erg.cd_layout)
join dbamv.editor_versao_documento   ev on(el.cd_versao_documento   = ev.cd_versao_documento)
join dbamv.editor_registro_campo     cp on(edi.cd_editor_registro = cp.cd_registro)
join dbamv.editor_campo             ecm on(ecm.cd_campo = cp.cd_campo)
join dbamv.editor_documento          ed on(ed.cd_documento = ev.cd_documento)
join dbamv.editor_tipo_item         eti on(eti.cd_tipo_item = ecm.cd_tipo_item)
join dbamv.editor_tipo_visualizacao etv on(etv.cd_tipo_visualizacao = ecm.cd_tipo_visualizacao)
where edi.cd_editor_registro  =  :cd_editor_registro
order by 4,6


-- :name insert-document :! :n
-- :doc Insert a single character
insert into public.poc_doc (entity, attribute,value)
values (:entity, :attribute, :value)