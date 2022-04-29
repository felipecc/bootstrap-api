(ns bootstrap-api.main
  (:require
   [aero.core :refer [read-config]]
   [clojure.java.io :as io]
   [clojure.tools.cli :refer [parse-opts]]
   [juxt.clip.core :as clip])
  (:gen-class))


(set! *warn-on-reflection* true)

(defn ^:private load-config
  [opts]
  (-> (io/resource (:config opts))
      (read-config opts)))

(def ^:private cli-options
  [["-c" "--config FILE" "Config file, found on the classpath, to use."
    :default "config/config.edn"]
   ["-p" "--profile PROFILE" "Profile to use, i.e., local."
    :default :default
    :parse-fn #(keyword %)]])

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var :unused-binding]}
(defn ^:private process
  [arguments system-config {:keys [app-config] :as system}]
  ;; process arguments here...
  (clip/stop system-config system)
  (shutdown-agents))