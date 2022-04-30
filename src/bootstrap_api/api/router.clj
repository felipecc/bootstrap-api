(ns bootstrap-api.api.router
  (:require [muuntaja.core :as m]
            [jsonista.core :as j]
            [reitit.coercion.malli :as rcm]
            [reitit.ring :as ring]
            [reitit.ring.coercion :as coercion]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [reitit.ring.middleware.parameters :as parameters]            
            [reitit.spec :as rs]
            [reitit.swagger :as swagger]
            [reitit.swagger-ui :as swagger-ui]
            [malli.util :as mu]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.cors :refer [wrap-cors]]
            [bootstrap-api.api.general.swagger :as swagger-api]
            [bootstrap-api.api.general.health :as health-api]
            [bootstrap-api.api.documentos.documentos :as documentos-api])
  (:import
   [org.eclipse.jetty.server Server]
   [com.fasterxml.jackson.annotation JsonInclude$Include]))


(set! *warn-on-reflection* true)

(def ^:private cors-middleware
  [wrap-cors
   :access-control-allow-origin [#".*"]
   :access-control-allow-methods [:delete :get :patch :post]])



(defn mount-router
  [app-config]
  (ring/router 
   [(swagger-api/routes app-config)
    (documentos-api/routes app-config)
    health-api/routes]
   {:validade rs/validate
    :data {:coercion  (rcm/create
                          {;; set of keys to include in error messages
                           :error-keys #{#_:type :coercion :in :schema :value :errors :humanized #_:transformed}
                           ;; schema identity function (default: close all map schemas)
                           :compile mu/closed-schema
                           ;; strip-extra-keys (effects only predefined transformers)
                           :strip-extra-keys true
                           ;; add/set default values
                           :default-values true
                           ;; malli options
                           :options nil})
           :muuntaja m/instance
           :middleware [cors-middleware
                        swagger/swagger-feature
                        muuntaja/format-middleware
                        parameters/parameters-middleware
                        coercion/coerce-exceptions-middleware
                        coercion/coerce-request-middleware
                        coercion/coerce-response-middleware]}}))

(defn start 
  [{:keys [:runtime-config] :as app-config}]
  (jetty/run-jetty
   (ring/ring-handler (mount-router app-config)
                      (ring/routes 
                       (swagger-ui/create-swagger-ui-handler {:path "/"
                                                              :operationsSorter "alpha"
                                                              :docExpansion "full"
                                                              :validatorUrl nil})
                       (ring/create-default-handler)))
   (merge (:jetty runtime-config) {:allow-null-path-info true
                                   :send-server-version? false
                                   :send-date-header? false
                                   :join? false})))

(defn stop
  [^Server server]
  (.stop server)
  (.join server))
