(ns app.core
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.content-type :refer [wrap-content-type]]
            [hiccup.page :refer [html5 include-css]]
            [hiccup.form :as form]
            [app.simpleclick :as my-click]
            [app.counter :as my-counter])
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

     ;; Example 1: Simple HTMX button
           (my-click/simple-click-part)

     ;; Example 2: HTMX form with live updates
           [:div
            [:h3 "2. Interactive Form (Live Updates)"]
            (form/form-to [:post "/greet"]
                          [:input {:type "text"
                                   :name "name"
                                   :placeholder "Enter your name"
                                   :hx-post "/greet"
                                   :hx-target "#greet-result"
                                   :hx-swap "innerHTML"
                                   :hx-trigger "keyup changed delay:500ms"}]
                          [:button {:type "submit"} "Greet"])
            [:div#greet-result.message "Type your name to see live updates..."]]

     ;; Example 3: Counter
           (my-counter/counter-part)

     ;; Example 4: Dynamic content
           [:div
            [:h3 "4. Load Dynamic Content"]
            [:button {:hx-get "/content"
                      :hx-target "#dynamic-content"
                      :hx-swap "innerHTML"}
             "📦 Load Content"]
            [:div#dynamic-content.message "Click to load dynamic content..."]]]))

;; HTMX Handlers

(defn greet-handler [request]
  (let [name (get-in request [:params "name"] "")]
    {:status 200
     :headers {"Content-Type" "text/html"}
     :body (if (empty? name)
             (str [:div.message.info "Type your name..."])
             (str [:div.message.success
                   (str "Hello, " name "!")]))}))

(defn content-handler [_]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (str [:div.message.success
               [:h4 "✨ Dynamic Content Loaded!"]
               [:p "This content was loaded via HTMX at " (str (java.time.LocalDateTime/now))]
               [:ul
                [:li "Server-side rendering with Hiccup"]
                [:li "Client-side updates with HTMX"]
                [:li "No JavaScript frameworks needed!"]
                [:li "Simple, fast, and elegant"]]])})

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
      (greet-handler request)

      (and (= uri "/increment") (= method :get))
      (my-counter/increment-handler request)

      (and (= uri "/decrement") (= method :get))
      (my-counter/decrement-handler request)

      (and (= uri "/reset") (= method :get))
      (my-counter/reset-handler request)

      (and (= uri "/remove") (= method :get))
      (my-counter/my-remove request)

      (and (= uri "/content") (= method :get))
      (content-handler request)

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
