(ns app.view.old.form
  (:require
   [hiccup.page :refer [html5]]
   [hiccup.form :as form]
   [app.counter :as my-counter]))

(defn form-part []
  [:div
   [:h3 "2. Interactive Form (Live Updates)"]
   (form/form-to [:post "/greet"]
                 [:input {:type "text"
                          :name "name"
                          :placeholder "Enter your name"
                          :hx-post "/greet"
                          :hx-target "#greet-result"
                          :hx-swap "innerHTML"
                          :hx-trigger "keyup changed delay:500ms"}]
                 [:button {:type "submit"} "Greet"])
   [:div#greet-result.message "Type your name to see live updates..."]])

(defn greet-handler [request]
  (let [name (get-in request [:params "name"] "")]
    {:status 200
     :headers {"Content-Type" "text/html"}
     :body (if (empty? name)
             (str [:div.message.info "Type your name..."])
             (str [:div.message.success
                   (str "Hello, " name "!")]))}))
