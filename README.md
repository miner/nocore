# NoCore

A Leiningen template for a **NoCore** style Clojure project.  

>  I think that this situation absolutely requires a really futile and stupid gesture be done 
>  on somebody's part.  -- Otter

## Core.clj Considered Harmful

In my humble opinion, the proliferation of "core.clj" files across the Clojure universe is
pointless.  It's fine for the Clojure language to define a "core" but there's no need to add "core"
to every library.  In particular, I find it tedious to have lots of "core.clj" files open in Emacs.

The "core" convention is perpetuated by the Leiningen "default" template.  That's an unfortunate
accident of history.  However, Leiningen makes it fairly easy to use a custom template for new
projects so that's what I did.  You're welcome to use "nocore" or make your own modified version and
share it with the world.

## NoCore Convention

The **NoCore** style involves four values: NAME, USER, GROUP, and NS.  (We're using the all-capital
terms as placeholder values in this document.)

As usual, the Leiningen `new` command takes a template ("nocore" is our focus here) and a NAME for
the new project.  A new project directory, named NAME, will be created.

`lein new nocore NAME`

The default USER is taken from the Java property "user.name".  You can overide this with the
optional keyword argument `:user USER`.  You might want to use your Github account name if it's
different than your login name.

The default main namespace for a NoCore project is `USER.NAME`.  The source file is
`NAME/src/USER/NAME.clj`.  You can overide this with the optional keyword argument `:ns NS`.  If the
value for `NS` does not end with the segment matching NAME, it will be added automatically.  (That
is, your main source file always matches your project NAME.)  Normally, the GROUP does not affect
the default namespace.  However, if you use the qualified GROUP/NAME form for the project name,
the GROUP will be part of the default namespace, which becomes `GROUP.NAME` in this special case.

The default test namespace is derived from the main namespace with a "test-" prefix inserted before
the last element.  If your main namespace is `com.example.jmc`, the test namespace will be
`com.example.test-jmc`.  I find it convenient to have my test files follow the `test_NAME.clj`
naming pattern rather than `NAME_test.clj`.  I think it's easier to bounce back and forth between
the main file and the test file that way.  Admittedly, that's just a matter of personal preference.

All **NoCore** projects have a group ID to help avoid project name clashes.  The default GROUP is
the USER.  You can override the default with the keyword argument `:group GROUP`.  Normally, the
GROUP is used only for the Group-ID in the `defproject` name.  As mentioned above, there is one
specail case: you can specify the GROUP/NAME form as the project name, which defines the
GROUP and the main namespace at the same time.

If you own a domain, the Java-style reverse domain name (such as "com.example") is the best choice
for a GROUP.  In my humble opinion, a Github user name makes a reasonably unique GROUP so that is
the default.  I contend that it's a better choice than duplicating the project's NAME as its
group-id.

## Usage

Leiningen 2.0 is smart enough to find project templates on Clojars.org so there's no need to install
anything.  Just choose a name for your project.

`lein new nocore NAME`

where NAME is the simple name of your new project.

As with the `default` template, you can optionally use a qualified project name.  In that case, the
project name is combined as one argument in the form GROUP/NAME (joined with a literal slash).

`lein new nocore GROUP/NAME`

Optional keyword arguments may be used to override the default values for :name, :user, :group, and
:ns (the main namespace).  You can use any combination of these optional keyword arguments.

`lein new nocore NAME :user USER`
`lein new nocore NAME :group GROUP`
`lein new nocore NAME :ns NS`
`lein new nocore ignored :user USER :group GROUP :ns NS :name NAME`

where you choose your values for NAME, USER, GROUP and NS.

Note that `lein new` requires the project name argument even if you decide to override it with a
:name keyword argument.  It may seem strange to override the :name, but it can be convenient if
you want to use `nocore` in a lein alias.

## License

Copyright © 2013 Stephen E. Miner

Distributed under the Eclipse Public License version 1.0, same as Clojure.
