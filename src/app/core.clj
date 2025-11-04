(ns app.core
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.content-type :refer [wrap-content-type]]
            [app.home :as home]
            [app.view.new-invoice :refer [new-invoice]]
            [app.view.invoices :refer [invoices]]
            [app.view.new-customer :refer [new-customer]]
            [app.view.customers :refer [customers]])
  (:gen-class))

;; Router
(defn handler [request]
  (let [uri (:uri request)
        method (:request-method request)]
    (cond
      (and (= uri "/") (= method :get))
      {:status 200
       :headers {"Content-Type" "text/html"}
       :body (home/home-page)}

      (and (= uri "/new-invoice") (= method :get))
      (new-invoice request)

      (and (= uri "/invoices") (= method :get))
      (invoices request)

      (and (= uri "/new-customer") (= method :get))
      (new-customer request)

      (and (= uri "/customers") (= method :get))
      (customers request)

      :else
      {:status 404
       :headers {"Content-Type" "text/html"}
       :body (home/layout "404" [:h2 "Page Not Found"])})))

;; Application
(def app
  (-> handler
      (wrap-params)
      (wrap-resource "public") ; "imports" resource folder to be able to include-css
      (wrap-content-type)
      (wrap-reload {:dirs ["src"]})))

;; Main
(defn -main [& args]
  (println "Starting server on http://localhost:3000")
  (run-jetty app {:port 3000 :join? false}))

;; REPL helpers
(comment
  (def server (-main))
  (.stop server))
