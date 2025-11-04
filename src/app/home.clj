(ns app.home
  (:require
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

(defn menu-button [text page]
  [:button {:hx-get (str "/" page)
            :hx-target "#main-page"
            :hx-swap "innerHTML"}
   text])

(defn main-menu []
  [:div
   (menu-button "Ny Faktura" "new-invoice")
   (menu-button "Fakturor" "invoices")
   (menu-button "Ny Kund" "new-customer")
   (menu-button "Kunder" "customers")])

(defn home-page []
  (layout "Faktura Program"
          [:div
           (main-menu)
           [:div#main-page]

           ; (my-form/form-part)
           ; (my-counter/counter-part)
           ; (my-dynamic/dynamic-part)
           ]))
