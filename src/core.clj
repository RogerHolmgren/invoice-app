(ns core
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.params :refer [wrap-params]]
            [hiccup.page :refer [html5]]
            [hiccup.form :as form])
  (:gen-class))

;; HTML Components using Hiccup
(defn layout [title & content]
  (html5
   [:head
    [:meta {:charset "UTF-8"}]
    [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0"}]
    [:title title]
    [:script {:src "https://unpkg.com/htmx.org@1.9.10"}]
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
    [:h1 "🚀 Clojure + HTMX + Hiccup"]
    content]))

(defn home-page []
  (layout "HTMX + Hiccup Demo"
          [:div
           [:h2 "Hello, World!"]
           [:p "This is a minimal Clojure application using HTMX and Hiccup."]

     ;; Example 1: Simple HTMX button
           [:div
            [:h3 "1. Simple Click"]
            [:button {:hx-get "/hello"
                      :hx-target "#hello-result"
                      :hx-swap "innerHTML"}
             "Click Me!"]
            [:div#hello-result.message "Click the button above..."]]

     ;; Example 2: HTMX form with live updates
           [:div
            [:h3 "2. Interactive Form (Updates)"]
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
           [:div
            [:h3 "3. Counter"]
            [:div#counter.message "Count: 0"]
            [:button {:hx-get "/increment"
                      :hx-target "#counter"
                      :hx-swap "innerHTML"}
             "➕ Increment"]
            [:button {:hx-get "/decrement"
                      :hx-target "#counter"
                      :hx-swap "innerHTML"}
             "➖ Decrement"]
            [:button {:hx-get "/reset"
                      :hx-target "#counter"
                      :hx-swap "innerHTML"}
             "🔄 Reset"]]

     ;; Example 4: Dynamic content
           [:div
            [:h3 "4. Load Dynamic Content"]
            [:button {:hx-get "/content"
                      :hx-target "#dynamic-content"
                      :hx-swap "innerHTML"}
             "📦 Load Content"]
            [:div#dynamic-content.message "Click to load dynamic content..."]]]))

;; HTMX Handlers
(defn hello-handler [_]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (str [:div.message.success "Hello from HTMX! 👋"])})

(defn greet-handler [request]
  (let [name (get-in request [:params "name"] "")]
    {:status 200
     :headers {"Content-Type" "text/html"}
     :body (if (empty? name)
             (str [:div.message.info "Type your name..."])
             (str [:div.message.success
                   (str "Hello, " name "! 🎉")]))}))

;; Counter state
(def counter (atom 0))

(defn increment-handler [_]
  (swap! counter inc)
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (str [:div.message.info (str "Count: " @counter)])})

(defn decrement-handler [_]
  (swap! counter dec)
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (str [:div.message.info (str "Count: " @counter)])})

(defn reset-handler [_]
  (reset! counter 0)
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (str [:div.message.info "Count: 0"])})

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
      (hello-handler request)

      (and (= uri "/greet") (= method :post))
      (greet-handler request)

      (and (= uri "/increment") (= method :get))
      (increment-handler request)

      (and (= uri "/decrement") (= method :get))
      (decrement-handler request)

      (and (= uri "/reset") (= method :get))
      (reset-handler request)

      (and (= uri "/content") (= method :get))
      (content-handler request)

      :else
      {:status 404
       :headers {"Content-Type" "text/html"}
       :body (layout "404" [:h2 "Page Not Found"])})))

;; Application
(def app
  (-> handler
      wrap-params))

;; Main
(defn -main [& args]
  (println "Starting server on http://localhost:3000")
  (run-jetty app {:port 3000 :join? false}))

;; REPL helpers
(comment
  (def server (-main))
  (.stop server)
  (reset! counter 0))
