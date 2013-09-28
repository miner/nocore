(ns leiningen.new.nocore
  "Generate a nocore library project."
  (:require [clojure.string :as str]
            [leiningen.new.templates :refer [renderer year project-name
                                             ->files sanitize sanitize-ns name-to-path
                                             multi-segment]]
            [leiningen.core.main :as main]))

(defn getprop
  "Returns the value of a Java property named `prop` (a String) or the `default` if the property is
  not defined."
  ([prop] (getprop prop nil))
  ([prop default]  (or (not-empty (System/getProperty prop)) default)))

(defn getenv
  "Returns the value of the environment variable named `v` (a String) or the `default` if the
  environment variable is not defined."
  ([v] (getenv v nil))
  ([v default]  (or (not-empty (System/getenv v)) default)))

(defn intermediate-path [main-ns]
  "Constructs intermediate directory structure from fully qualified artifact name:

  \"my.foo-bar.proj\" becomes \"my/foo_bar/\"

  and so on.  The last segment of the namespace is dropped. Uses platform-specific file separators."
  (let [dot (.lastIndexOf main-ns ".")
        spath (if (pos? dot) (subs main-ns 0 dot) main-ns)]
    (-> spath sanitize (str/replace "." java.io.File/separator))))

(defn username []
  (or (getenv "LEIN_NOCORE_USER") (getprop "user.name") "unknown"))

(defn username-multi-segment [projname]
  (if (.contains projname ".")
    projname
    (format "%s.%s" (username) projname)))

(defn group-id [projname]
  (let [slash (.lastIndexOf projname "/")]
    (if (pos? slash)
      (subs 0 projname slash)
      (or (getenv "LEIN_NOCORE_GROUPID") (username)))))
  
(defn nocore
  "A general project template for libraries in the nocore style.

Accepts a group id in the project name: `lein new nocore foo.bar/baz`"
  [name]
  (let [render (renderer "nocore")
        main-ns (username-multi-segment (sanitize-ns name))
        user (getprop "user.name")
        data {:groupid (group-id name)
              :user (username)
              :name (project-name name)
              :namespace main-ns
              :dirs (intermediate-path main-ns)
              :year (year)}]
    (main/info "Generating a project called" name "based on the 'nocore' template.")
    (main/info "To see other templates (app, lein plugin, etc), try `lein help new`.")
    (->files data
             ["project.clj" (render "project.clj" data)]
             ["README.md" (render "README.md" data)]
             ["doc/intro.md" (render "intro.md" data)]
             [".gitignore" (render "gitignore" data)]
             ["src/{{dirs}}/{{name}}.clj" (render "main.clj" data)]
             ["test/{{dirs}}/test_{{name}}.clj" (render "test.clj" data)]
             ["LICENSE" (render "LICENSE" data)]
             "resources")))

