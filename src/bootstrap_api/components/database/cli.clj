(ns bootstrap-api.components.database.cli
  (:require [next.jdbc :as jdbc]
            [next.jdbc.connection :as connection])
  (:import [com.zaxxer.hikari HikariDataSource]))


(defn start
  ^HikariDataSource [config]
  (connection/->pool HikariDataSource config))


(defn stop
  [^HikariDataSource datasource]
  (.close datasource))