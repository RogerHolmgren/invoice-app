(ns app.view.invoices
  (:require
   [hiccup.page :refer [html5]]))

(defn invoices [req]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (html5
          [:h3 "Lista fakturor"])})
