(ns app.core
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.content-type :refer [wrap-content-type]]
            [app.home :as home]
            [app.simpleclick :as my-click]
            [app.counter :as my-counter]
            [app.dynamic :as my-dynamic]
            [app.form :as my-form])
  (:gen-class))

;; HTML Components using Hiccup

;; Router
(defn handler [request]
  (let [uri (:uri request)
        method (:request-method request)]
    (cond
      (and (= uri "/") (= method :get))
      {:status 200
       :headers {"Content-Type" "text/html"}
       :body (home/home-page)}

      (and (= uri "/greet") (= method :post))
      (my-form/greet-handler request)

      (and (= uri "/increment") (= method :get))
      (my-counter/increment-handler request)

      (and (= uri "/decrement") (= method :get))
      (my-counter/decrement-handler request)

      (and (= uri "/reset") (= method :get))
      (my-counter/reset-handler request)

      (and (= uri "/remove") (= method :get))
      (my-click/my-remove request)

      (and (= uri "/add") (= method :get))
      (my-click/my-add request)

      (and (= uri "/content") (= method :get))
      (my-dynamic/content-handler request)

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
