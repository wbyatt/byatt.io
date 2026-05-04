#!/usr/bin/env bb

;; ═══════════════════════════════════════════════════════════════════
;; responsa.clj — synthesize replies to ephemeron discussion threads
;;
;; For each ephemeron discussion, finds all comments posted since the
;; bot's last reply, then generates a single synthetic response that
;; addresses the interesting points holistically, @-mentioning
;; individual commenters where their contributions are engaged.
;;
;; Usage: ANTHROPIC_API_KEY=... GITHUB_TOKEN=... bb scripts/responsa.clj
;; Post:  Set RESPONSA_POST=1 to actually post (dry-run by default)
;; ═══════════════════════════════════════════════════════════════════

(require '[babashka.http-client :as http]
         '[cheshire.core :as json]
         '[clojure.string :as str]
         '[babashka.fs :as fs])

;; ── Configuration ──────────────────────────────────────────────────

(def config
  {:model       "claude-opus-4-6"
   :max-tokens  2048
   :api-url     "https://api.anthropic.com/v1/messages"
   :api-version "2023-06-01"
   :bot-name    "ephemeron-bot"
   :repo-root   (str (fs/parent (fs/parent (fs/absolutize *file*))))})

(def anthropic-key
  (or (System/getenv "ANTHROPIC_API_KEY")
      (throw (ex-info "ANTHROPIC_API_KEY not set" {}))))

(def github-token
  (or (System/getenv "GITHUB_TOKEN")
      (throw (ex-info "GITHUB_TOKEN not set" {}))))

(def dry-run?
  (not= "1" (System/getenv "RESPONSA_POST")))

;; ── API helpers ────────────────────────────────────────────────────

(defn call-claude [system-prompt user-message]
  (let [body   {:model      (:model config)
                :max_tokens (:max-tokens config)
                :system     system-prompt
                :messages   [{:role "user" :content user-message}]}
        resp   (http/post (:api-url config)
                          {:headers {"x-api-key"        anthropic-key
                                     "anthropic-version" (:api-version config)
                                     "content-type"      "application/json"}
                           :body    (json/generate-string body)})
        parsed (json/parse-string (:body resp) true)]
    (if (= 200 (:status resp))
      (->> (:content parsed)
           (filter #(= "text" (:type %)))
           first
           :text)
      (throw (ex-info "Claude API call failed"
                      {:status (:status resp) :body (:body resp)})))))

(defn github-graphql [query]
  (let [resp (http/post "https://api.github.com/graphql"
                        {:headers {"Authorization" (str "Bearer " github-token)
                                   "Content-Type"  "application/json"}
                         :body    (json/generate-string {:query query})})]
    (if (= 200 (:status resp))
      (json/parse-string (:body resp) true)
      (throw (ex-info "GitHub API call failed"
                      {:status (:status resp) :body (:body resp)})))))

(defn post-discussion-comment!
  "Post a top-level comment to a GitHub Discussion."
  [discussion-id body-text]
  (let [escaped (-> body-text
                    (str/replace "\\" "\\\\")
                    (str/replace "\"" "\\\"")
                    (str/replace "\n" "\\n"))
        mutation (str "mutation { addDiscussionComment(input: {"
                      "discussionId: \"" discussion-id "\", "
                      "body: \"" escaped "\""
                      "}) { comment { id url } } }")]
    (github-graphql mutation)))

;; ── Essay loading ──────────────────────────────────────────────────

(defn strip-frontmatter
  "Remove YAML front matter, return body text."
  [content]
  (let [parts (str/split content #"---\n" 3)]
    (if (>= (count parts) 3)
      (str/trim (nth parts 2))
      (str/trim content))))

(defn load-essay
  "Load the essay text for a given doc_id."
  [doc-id]
  (let [repo      (:repo-root config)
        eph-path  (str repo "/content/ephemeron.md")
        date-str  (second (re-find #"ephemeron\.(.+)" doc-id))
        post-path (when date-str
                    (str repo "/content/posts/ephemeron-" date-str ".md"))]
    (cond
      (and (fs/exists? eph-path)
           (str/includes? (slurp eph-path) doc-id))
      (strip-frontmatter (slurp eph-path))

      (and post-path (fs/exists? post-path))
      (strip-frontmatter (slurp post-path))

      :else nil)))

;; ── Discussion querying ────────────────────────────────────────────

(defn fetch-ephemeron-discussions []
  (let [query "{ repository(owner: \"wbyatt\", name: \"byatt.io\") {
                  discussions(categoryId: \"DIC_kwDOMvtUZM4C56QU\", first: 20,
                              orderBy: {field: UPDATED_AT, direction: DESC}) {
                    nodes {
                      id
                      title
                      comments(first: 100) {
                        nodes {
                          id
                          author { login }
                          body
                          createdAt
                        }
                      }
                    }
                  }
                }}"
        result (github-graphql query)]
    (->> (get-in result [:data :repository :discussions :nodes])
         (filter #(str/starts-with? (or (:title %) "") "ephemeron.")))))

(defn find-new-comments
  "Given a discussion's comments, return those posted after the bot's
   last top-level comment. Excludes bot's own comments."
  [comments]
  (let [bot-name   (:bot-name config)
        bot-comments (->> comments
                          (filter #(= bot-name (get-in % [:author :login]))))
        ;; Find the most recent bot comment timestamp
        last-bot-ts  (when (seq bot-comments)
                       (->> bot-comments
                            (map :createdAt)
                            sort
                            last))
        ;; Everything after the bot's last comment, excluding bot itself
        new-comments (->> comments
                          (remove #(= bot-name (get-in % [:author :login])))
                          (remove #(str/ends-with? (get-in % [:author :login] "") "[bot]")))]
    (if last-bot-ts
      (filter #(pos? (compare (:createdAt %) last-bot-ts)) new-comments)
      new-comments)))

;; ── Response generation ────────────────────────────────────────────

(defn format-comment-batch
  "Format the new comments into a readable block for Claude."
  [comments]
  (->> comments
       (map (fn [c]
              (str "@" (get-in c [:author :login]) " ("
                   (:createdAt c) "):\n"
                   (:body c))))
       (str/join "\n\n---\n\n")))

(defn generate-synthesis
  "Generate a single synthetic reply addressing the interesting points
   across all new comments."
  [essay-text comment-batch]
  (let [system (str "You are the responsa voice for byatt.io. You have been given an essay "
                    "(a daily ephemeron) and a batch of new comments on it. Your task is to "
                    "produce a SINGLE synthetic reply — one top-level comment that addresses "
                    "the interesting points raised across all the comments.\n\n"
                    "Guidelines:\n"
                    "- Synthesize, don't enumerate. Weave the interesting points into a "
                    "coherent response. You may group related concerns, draw connections "
                    "between different commenters' ideas, or follow one thread that illuminates "
                    "several.\n"
                    "- Use @username to address or credit specific commenters when engaging "
                    "their particular contribution. GitHub Discussions renders these as mentions.\n"
                    "- Be discriminating. If a comment is low-substance ('+1', 'interesting', "
                    "emoji-only, or less than ~100 characters of real argument), simply don't "
                    "address it. Do not explain why you're ignoring it.\n"
                    "- If NO comments are substantive enough to warrant engagement, respond "
                    "with exactly the word SKIP and nothing else.\n"
                    "- Be concise: 1-5 paragraphs. Intellectually serious. No filler.\n"
                    "- Engage with substance: agree where warranted, push back where the "
                    "argument is weak, extend where the commenter opens a productive thread.\n"
                    "- Do not thank people for commenting or reading.\n"
                    "- Do not use bullet points or headers.\n"
                    "- Sign off with: — *responsa, Claude Opus 4.6*")
        prompt (str "THE ESSAY:\n---\n" essay-text "\n---\n\n"
                    "NEW COMMENTS SINCE LAST REPLY:\n---\n"
                    comment-batch "\n---\n\n"
                    "Write your synthetic reply, or SKIP if nothing warrants engagement.")]
    (call-claude system prompt)))

;; ── Main ───────────────────────────────────────────────────────────

(defn run []
  (println "\n╔══════════════════════════════════════════════╗")
  (println (str "║  responsa" (if dry-run? " (DRY RUN)" "          ") "                            ║"))
  (println "╚══════════════════════════════════════════════╝\n")

  (let [discussions (fetch-ephemeron-discussions)]
    (println "Found" (count discussions) "ephemeron discussions\n")

    (doseq [disc discussions]
      (let [doc-id       (:title disc)
            disc-id      (:id disc)
            all-comments (get-in disc [:comments :nodes] [])
            new-comments (find-new-comments all-comments)
            essay        (load-essay doc-id)]

        (println "── " doc-id " ──")
        (println "  " (count all-comments) "total comments,"
                 (count new-comments) "since last reply")

        (cond
          (empty? new-comments)
          (println "  → Nothing new to address\n")

          (nil? essay)
          (println "  ⚠ Could not load essay — skipping\n")

          :else
          (let [batch (format-comment-batch new-comments)
                _     (println "  → Generating synthesis for"
                               (count new-comments) "new comments...")
                reply (generate-synthesis essay batch)]

            (if (= (str/trim reply) "SKIP")
              (println "  → No comments substantive enough to engage\n")

              (do
                (println "\n  ┌── Synthesis ──")
                (doseq [line (str/split-lines reply)]
                  (println "  │" line))
                (println "  └──────────────\n")

                (if dry-run?
                  (println "  → DRY RUN — set RESPONSA_POST=1 to post\n")
                  (do
                    (post-discussion-comment! disc-id reply)
                    (println "  → Posted\n")))))))))))

(run)
