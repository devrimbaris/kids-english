# English Learning Game

## HOWTO RUN (emacs repl)
(require '[clojure.tools.namespace.repl :refer [refresh refresh-all]])
(require '[ring.adapter.jetty :as jty])
(refresh-all)
(defonce server (jty/run-jetty #'hello-world.core.handler/app {:port 8080 :join? false}))
(.start server)



## DBA
1. Use "lein ring server-headless"  for running server
2. Use cider-connect


## Prerequisites

You will need [Leiningen][] 2.0.0 or above installed.

[leiningen]: https://github.com/technomancy/leiningen

## Running

To start a web server for the application, run:

    lein ring server


## License

Copyright © 2014 FIXME
