(ns bootstrap-api.utils.formatters
  (:require [clojure.string :as str]))


(defn field-only-letters
  [field-str]
  (str/join "_" (re-seq #"[a-z]+" (str/lower-case field-str))))

(defn keywordize-domain-field
  [domain field]
  (keyword (format "%s/%s" domain field)))

(defn extract-value-for-field-type
  [value type]
  (condp = type
    "TEXT" value
    "CHECKBOX" value
    "TEXTAREA" value
    "COMBOBOX" (let [vr (re-seq #"(\d+)\|\|([\(\)\[\]\.\s\s1-9A-Za-záàâãéèêíïóôõöúçñÁÀÂÃÉÈÍÏÓÔÕÖÚÇÑ]+)" (or valor ""))]
                 (if vr
                   (nth (first vr) 2)
                   vr))
    value))