(ns app.simpleclick
  (:require
   [hiccup.page :refer [html5]]
   [app.counter :as my-counter]))

(defn menu-button [text page]
  [:button {:hx-get (str "/" page)
            :hx-target "#hello-result"
            :hx-swap "innerHTML"}
   text])

(defn main-menu []
  [:div
   (menu-button "Kunder" "customers")
   (menu-button "Faktura" "invoice")])

(defn my-remove [_]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (html5 [:p "hello hiccup paragraf"])})

(defn my-add [_]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body "<p>hello html paragraf</p>"})
