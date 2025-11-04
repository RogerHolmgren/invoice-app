(ns app.view.old.dynamic
  (:require
   [hiccup.page :refer [html5]]
   [hiccup.form :as form]
   [app.counter :as my-counter]))

(defn dynamic-part []
  [:div
   [:h3 "4. Load Dynamic Content"]
   [:button {:hx-get "/content"
             :hx-target "#dynamic-content"
             :hx-swap "innerHTML"}
    "📦 Load Content"]
   [:div#dynamic-content.message "Click to load dynamic content..."]])

(defn content-handler [_]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (html5 [:div.message.success
                 [:h4 "✨ Dynamic Content Loaded!"]
                 [:p "This content was loaded via HTMX at " (str (java.time.LocalDateTime/now))]
                 [:ul
                  [:li "Server-side rendering with Hiccup"]
                  [:li "Client-side updates with HTMX"]
                  [:li "No JavaScript frameworks needed!"]
                  [:li "Simple, fast, and elegant"]]])})
