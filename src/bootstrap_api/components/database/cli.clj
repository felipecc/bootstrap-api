(ns bootstrap-api.components.database.cli
  (:require [next.jdbc :as jdbc]
            [next.jdbc.connection :as connection]
            [hugsql.adapter.next-jdbc :as next-adapter]
            [next.jdbc.result-set :as rs]
            [hugsql.core :as hugsql])
  (:import (com.zaxxer.hikari HikariDataSource)
           (com.zaxxer.hikari HikariConfig)))


(defn datasource-config [spec-db]
  (let [config (HikariConfig.)]
    (doto config
      (.setDataSourceClassName (:classname spec-db))
      (.setJdbcUrl  (:jdbcurl spec-db))
      (.setUsername (:user spec-db))
      (.setPassword (:password spec-db)))
    config))

(defn start
  [config]
  (connection/->pool HikariDataSource config))


(defn stop
  [datasource]
  (.close datasource))