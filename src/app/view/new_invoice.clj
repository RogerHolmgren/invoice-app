(ns app.view.new-invoice
  (:require
   [hiccup.form :as form]
   [hiccup.page :refer [html5]]))

(defn new-invoice [req]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (html5
          [:h3 "Skapa faktura"]
          (form/form-to [:post "/create-invoice"]
                        [:input {:type "text"
                                 :name "name"
                                 :placeholder "Kundnamn"
                                 :hx-post "/create-invoice"
                                 :hx-target "#greet-result"
                                 :hx-swap "innerHTML"
                                 :hx-trigger "keyup changed delay:500ms"}]
                        [:button {:type "submit"} "Skapa"]))})
