(ns app.counter
  (:require [hiccup.page :refer [html5]])
  (:gen-class))

(defn counter-part []
  [:div#cou
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
    "🔄 Reset"]])

;; HTMX Handlers
;; Counter state
(def counter (atom 0))

(defn increment-handler [_]
  (swap! counter inc)
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (str "Count: " @counter)})

(defn decrement-handler [_]
  (swap! counter dec)
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (str "Count: " @counter)})

(defn reset-handler [_]
  (reset! counter 0)
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body "Count: 0"})

(comment
  (reset! counter 0))
