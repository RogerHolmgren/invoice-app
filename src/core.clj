(ns core
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.reload :refer [wrap-reload]]
            [hiccup.page :refer [html5]]
            [hiccup.form :as form]
            [simpleclick :as my-click]
            [counter :as my-counter])
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
    [:style "
       body { font-family: Arial, sans-serif; max-width: 800px; margin: 50px auto; padding: 20px; }
       button { padding: 10px 20px; font-size: 16px; cursor: pointer; margin: 5px; }
       input { padding: 10px; font-size: 16px; margin-right: 10px; width: 300px; }
       .message { padding: 20px; background: #f0f0f0; border-radius: 5px; margin: 20px 0; }
       .success { background: #d4edda; color: #155724; }
       .info { background: #d1ecf1; color: #0c5460; }
       h3 { margin-top: 40px; border-bottom: 2px solid #333; padding-bottom: 10px; }
     "]]
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
      wrap-params
      (wrap-reload {:dirs ["src"]})))

;; Main
(defn -main [& args]
  (println "Starting server on http://localhost:3000")
  (run-jetty app {:port 3000 :join? false}))

;; REPL helpers
(comment
  (def server (-main))
  (.stop server)
  (reset! counter 0))
