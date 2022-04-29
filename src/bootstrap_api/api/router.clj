(ns bootstrap-api.api.router
  (:require [muuntaja.core :as m]
            [reitit.coercion.malli :as rcm]
            [reitit.ring :as ring]
            [reitit.ring.coercion :as coercion]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [reitit.ring.middleware.parameters :as parameters]
            [reitit.spec :as rs]
            [reitit.swagger :as swagger]
            [reitit.swagger-ui :as swagger-ui]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.cors :refer [wrap-cors]]
            [bootstrap-api.api.general.swagger :as swagger-api]
            [malli.util :as mu])
  (:import
   [org.eclipse.jetty.server Server]))


(set! *warn-on-reflection* true)

(def ^:private cors-middleware
  [wrap-cors
   :access-control-allow-origin [#".*"]
   :access-control-allow-methods [:delete :get :patch :post]])



(defn mount-router
  [app-config]
  (ring/router 
   [(swagger-api/router app-config)]
   {:validade rs/validate
    :data {:coercion (reitit.coercion.malli/create
                      {:error-keys #{#_:type :coercion :in :schema :value :errors :humanized #_:transformed}
                       :compile mu/closed-schema
                       :strip-extra-keys true
                       :default-values true
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
