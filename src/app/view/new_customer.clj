(ns app.view.new-customer
  (:require
   [hiccup.page :refer [html5]]))

(defn new-customer [req]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (html5
          [:h3 "Ny kund"])})
