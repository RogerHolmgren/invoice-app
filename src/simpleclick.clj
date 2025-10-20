(ns simpleclick)

(defn simple-click-part []
  [:div
   [:h3 "1. Simple Click"]
   [:button {:hx-get "/hello"
             :hx-target "#hello-result"
             :hx-swap "innerHTML"}
    "Click Me!"]
   [:div#hello-result.message "Click the button above..."]])

(defn hello-handler [_]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (str [:div.message.success "Hello from HTMX!"])})
