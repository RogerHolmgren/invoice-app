(ns app.simpleclick
  (:require
   [hiccup.page :refer [html5]]
   [app.counter :as my-counter]))

(defn simple-click-part []
  [:div
   [:h3 "1. Simple Click"]
   [:button {:hx-get "/hello"
             :hx-target "#hello-result"
             :hx-swap "innerHTML"}
    "Click Me!"]
   [:div#hello-result.message "Click the button above..."]
   [:button {:hx-get "/add"
             :hx-target "#cou"
             :hx-swap "innerHTML"}
    "Add"]
   [:button {:hx-get "/remove"
             :hx-target "#cou"
             :hx-swap "innerHTML"}
    "Remove"]])

(defn hello-handler [_]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body "Hello from HTMX!"})

(defn my-remove [_]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (html5 [:p "hello hiccup paragraf"])})

(defn my-add [_]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body "<p>hello html paragraf</p>"})
