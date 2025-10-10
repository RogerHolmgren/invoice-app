# Hello HTMX + Hiccup

A minimal Clojure web application using HTMX and Hiccup.

## Features

- Server-side rendering with Hiccup
- Interactive UI with HTMX (no JavaScript needed!)
- Simple counter example
- Live form updates
- Dynamic content loading

## Requirements

- Java
- Clojure CLI (clj)

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

## Examples

1. Simple button click
2. Live form with real-time updates
3. Counter with increment/decrement
4. Dynamic content loading

