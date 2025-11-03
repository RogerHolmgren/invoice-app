(ns app.home
  (:require
   [app.simpleclick :as my-click]
   [app.counter :as my-counter]
   [app.dynamic :as my-dynamic]
   [app.form :as my-form]
   [hiccup.page :refer [html5 include-css]]))

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
    [:h1 "Faktura Program"]
    content]))

(defn home-page []
  (layout "Faktura Program"
          [:div
           (my-click/main-menu)
           [:div#main-page]

           ; (my-form/form-part)
           ; (my-counter/counter-part)
           ; (my-dynamic/dynamic-part)
           ]))
