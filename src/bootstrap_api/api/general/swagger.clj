(ns bootstrap-api.api.general.swagger
  (:require [reitit.swagger :as swagger]))

(set! *warn-on-reflection* true)

(defn routes
  [config]
  ["/swagger.json" {:get {:no-doc true
                          :swagger {:info {:title (:title-api config)}
                                    :basePath "/"}
                          :handler (swagger/create-swagger-handler)}}])