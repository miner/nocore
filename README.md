# nocore

A Leiningen template for a Clojure "No Core" style project.  Instead of using the overloaded name
"core.clj" as your main source file, a **nocore** project uses `USER-NAME/PROJECT-NAME.clj`.  For
example, my project foo would have its main source file: `miner/foo.clj`.  I find this style makes
it much less confusing to have several projects open in Emacs at the same time.

The project.clj of a **nocore** project always uses a group-id.  The default comes from the user
name.  You can override the group id by setting the environment variable `LEIN_NOCORE_GROUPID`.  You
can override the user name by setting the environment variable `LEIN_NOCORE_USER`.  It probably
makes sense to have them match your github account.

## Usage

`lein new nocore PROJECT-NAME`

where PROJECT-NAME is the simple name of your new project.

## License

Copyright © 2013 Stephen E. Miner

Distributed under the Eclipse Public License version 1.0, same as Clojure.
