(ns leiningen.new.nocore
  "Generate a nocore library project."
  (:require [clojure.string :as str]
            [leiningen.new.templates :refer [renderer year project-name
                                             ->files sanitize sanitize-ns name-to-path
                                             multi-segment]]
            [leiningen.core.main :as main]))

(defn getprop
  "Returns the value of a Java property named `prop` (a String) or the optional `default` if the
  property is not defined.  The default `default` is nil."
  ([prop] (getprop prop nil))
  ([prop default]  (or (not-empty (System/getProperty prop)) default)))

(defn intermediate-path [main-ns]
  "Constructs intermediate directory structure from fully qualified artifact name. For example,

  \"my.foo-bar.proj\" becomes \"my/foo_bar/\"

  The last segment of the namespace is dropped. Uses platform-specific file separators."
  (let [dot (.lastIndexOf main-ns ".")
        spath (if (pos? dot) (subs main-ns 0 dot) main-ns)]
    (-> spath sanitize (str/replace "." java.io.File/separator))))

(defn ns-test [nsname]
  (let [dot (.lastIndexOf nsname ".")]
    (if (pos? dot)
      (str (subs nsname 0 dot) ".test-" (subs nsname (inc dot)))
      ;; single segment ns shouldn't happen but just in case
      (str "test-" nsname))))

;; This supports a name like "com.example/my-project" for users who are used to the "default"
;; template, but I don't recommend that style.  It's better to use a simple project NAME, and :group
;; keyword argument instead.
(defn group-from-name [name]
  (let [slash (.lastIndexOf name "/")]
    (when (pos? slash)
      (subs name 0 slash))))

(defn colon-str? [s]
  (.startsWith ^String s ":"))

(defn- check-kwargs [kwargs]
  (and (even? (count kwargs))
       (every? not-empty kwargs)
       (let [colons (map colon-str? kwargs)]
         (every? true? (take-nth 2 colons))
         (every? false? (take-nth 2 (rest colons))))))

(defn rationalize-ns [main-ns projname user]
  (if (not main-ns)
    (str user "." projname)
    (let [dot (.lastIndexOf main-ns ".")]
      (if (and (pos? dot) (= projname (subs main-ns (inc dot))))
          main-ns
          (str main-ns "." projname)))))

(defn nocore
  "A general project template for libraries using the NoCore convention.
Typical usage: `lein new nocore foo`
Optional keyword args can override project name, group ID, user, and namespace:
 `lein new nocore foo :group com.example :user jmc :ns sail.jmc.foo :name foo`"
  [name & kwargs]
  (when-not (check-kwargs kwargs)
    (throw (IllegalArgumentException. "Improper keyword arguments")))
  (let [render (renderer "nocore")
        ;; Leiningen passes all the args as strings so we fake the keyword args.
        {user ":user" group ":group" main-ns ":ns" kname ":name"} kwargs
        projname (or kname (project-name name))
        user (or user (getprop "user.name") "unknown")
        group (or group (group-from-name name) user)
        main-ns (rationalize-ns main-ns projname user)
        data {:group group 
              :user user
              :name projname
              :namespace main-ns
              :test-namespace (ns-test main-ns)
              :dirs (intermediate-path main-ns)
              :year (year)}]
    (when (empty? projname)
      (throw (IllegalArgumentException. "Project name is missing")))
    (when-not (neg? (.indexOf projname "."))
      (throw (IllegalArgumentException. "Periods are not allowed in a 'No Core' project name")))
    (main/info "Generating a project called" projname "based on the 'nocore' template.")
    (main/info (format " Project ID: %s/%s" (:group data) (:name data)))
    (main/info " Namespace:" (:namespace data))
    (->files data
             ["project.clj" (render "project.clj" data)]
             ["README.md" (render "README.md" data)]
             ["doc/intro.md" (render "intro.md" data)]
             [".gitignore" (render "gitignore" data)]
             ["src/{{dirs}}/{{name}}.clj" (render "main.clj" data)]
             ["test/{{dirs}}/test_{{name}}.clj" (render "test.clj" data)]
             ["LICENSE" (render "LICENSE" data)]
             "resources")))
