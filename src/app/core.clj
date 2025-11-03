(ns app.core
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.content-type :refer [wrap-content-type]]
            [hiccup.page :refer [html5 include-css]]
            [app.simpleclick :as my-click]
            [app.counter :as my-counter]
            [app.dynamic :as my-dynamic]
            [app.form :as my-form])
  (:gen-class))

;; HTML Components using Hiccup
(defn layout [title & content]
  (html5
   [:head
    [:meta {:charset "UTF-8"}]
    [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0"}]
    [:title title]
    [:script {:src "https://unpkg.com/htmx.org@1.9.10"}]
    [:script {:src "https://livejs.com/live.js"}]
    (include-css "/css/style.css")]
   [:body
    [:h1 "Faktura App"]
    content]))

(defn home-page []
  (layout "Faktura Program"
          [:div
           [:h2 "Hello, World!"]
           [:p "This is a minimal Clojure application using HTMX and Hiccup."]

           (my-click/simple-click-part)
           (my-form/form-part)
           (my-counter/counter-part)
           (my-dynamic/dynamic-part)]))

;; Router
(defn handler [request]
  (let [uri (:uri request)
        method (:request-method request)]
    (cond
      (and (= uri "/") (= method :get))
      {:status 200
       :headers {"Content-Type" "text/html"}
       :body (home-page)}

      (and (= uri "/hello") (= method :get))
      (my-click/hello-handler request)

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
       :body (layout "404" [:h2 "Page Not Found"])})))

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
