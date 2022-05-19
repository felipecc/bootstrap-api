-- :name get-all-documentos-pg :? :*
-- :doc Get all documents pg
select cd_campo
     , ds_campo 
from public.campos
limit 10