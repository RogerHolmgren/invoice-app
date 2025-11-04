(ns app.view.new-invoice
  (:require
   [hiccup.page :refer [html5]]))

(defn new-invoice [req]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (html5
          [:h3 "Skapa faktura"])})
