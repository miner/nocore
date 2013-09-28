# nocore

A Leiningen template for a Clojure "No Core" style project.  Instead of using the ambiguous
"core.clj" as your main source file, **nocore** project uses *user-name*/*project-name*.clj.  

Also, the name of a **nocore** project uses a group-id taken from the user name.

## Usage

`lein new nocore PROJECT-NAME`

where PROJECT-NAME is the name of your new project.

## License

Copyright © 2013 Stephen E. Miner

Distributed under the Eclipse Public License version 1.0, same as Clojure.
