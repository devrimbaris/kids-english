# English Learning Game

## HOWTO RUN (emacs repl)
(require '[clojure.tools.namespace.repl :refer [refresh refresh-all]])
(require '[ring.adapter.jetty :as jty])
(refresh-all)
(defonce server (jty/run-jetty #'hello-world.core.handler/app {:port 8080 :join? false}))
(.start server)



## DBA
Once lein ring server-headless ile sunucuyu calistir
eww ile sayfalari gorebilirsin
cider-connect cek


## Prerequisites

You will need [Leiningen][] 2.0.0 or above installed.

[leiningen]: https://github.com/technomancy/leiningen

## Running

To start a web server for the application, run:

    lein ring server


## License

Copyright © 2014 FIXME
