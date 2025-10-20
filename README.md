# Hello HTMX + Hiccup

A minimal Clojure web application using HTMX and Hiccup.

## Running

```bash
clj -M:run
```

Then visit: http://localhost:3000

## Development

Start a REPL:
```bash
clj
```

In the REPL:
```clojure
(require '[hello-htmx.core :as core])
(def server (core/-main))

;; Stop server
(.stop server)
```

## What's Inside

- **Ring**: Web server
- **Hiccup**: HTML generation
- **HTMX**: Interactive updates without JavaScript

