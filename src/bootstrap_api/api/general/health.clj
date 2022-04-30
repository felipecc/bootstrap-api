(ns bootstrap-api.api.general.health)

(set! *warn-on-reflection* true)

(def routes 
  ["/ping" {:get {:handler (constantly {:status 200 
                                        :body "Pong!"})}}])
