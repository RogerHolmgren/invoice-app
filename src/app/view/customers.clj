(ns app.view.customers
  (:require
   [hiccup.page :refer [html5]]))

(defn customers [req]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (html5
          [:h3 "Lista Kunder"])})
