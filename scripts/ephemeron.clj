#!/usr/bin/env bb

;; ═══════════════════════════════════════════════════════════════════
;; ephemeron.clj — daily essay pipeline for byatt.io
;;
;; A multi-stage dialectical pipeline that generates a serious
;; theoretical essay at the intersection of technology and the
;; humanities, then publishes it as the site's ephemeron.
;;
;; Stages: ideation → critique → writing → editorial → publication
;;
;; The critique stage acts as adversarial pressure, replacing the
;; human interlocutor who would normally steer the discourse.
;; The editorial stage is the final quality gate.
;;
;; Usage: bb scripts/ephemeron.clj
;; Env:   ANTHROPIC_API_KEY must be set
;; ═══════════════════════════════════════════════════════════════════

(require '[babashka.http-client :as http]
         '[cheshire.core :as json]
         '[clojure.string :as str]
         '[babashka.fs :as fs]
         '[babashka.process :as proc])

;; ── Configuration ──────────────────────────────────────────────────

(def config
  {:model       "claude-opus-4-6"
   :max-tokens  4096
   :api-url     "https://api.anthropic.com/v1/messages"
   :api-version "2023-06-01"
   :max-retries 2
   :repo-root   (str (fs/parent (fs/parent (fs/absolutize *file*))))})

(def api-key
  (or (System/getenv "ANTHROPIC_API_KEY")
      (throw (ex-info "ANTHROPIC_API_KEY not set" {}))))

;; ── Conceptual seed corpus ─────────────────────────────────────────
;; Each run selects a random subset to cross-pollinate.

(def thinkers
  [{:name "Gilbert Simondon"   :concepts "individuation, technical objects, transduction, associated milieu"}
   {:name "Bernard Stiegler"   :concepts "technics, grammatization, tertiary retention, pharmacology"}
   {:name "Friedrich Kittler"  :concepts "discourse networks, media archaeology, inscription systems"}
   {:name "Donna Haraway"      :concepts "cyborgs, situated knowledges, string figures, sympoiesis"}
   {:name "Bruno Latour"       :concepts "actor-network theory, immutable mobiles, matters of concern"}
   {:name "Ludwig Wittgenstein" :concepts "language games, family resemblance, forms of life, rule-following"}
   {:name "Martin Heidegger"   :concepts "enframing, ready-to-hand, technological revealing"}
   {:name "Sylvia Wynter"      :concepts "genre of the human, sociogeny, overrepresentation of Man, coloniality"}
   {:name "Karen Barad"        :concepts "agential realism, intra-action, diffractive methodology, mattering"}
   {:name "Wendy Chun"         :concepts "programmed visions, updating to remain the same, habitual new media"}
   {:name "Lucy Suchman"       :concepts "situated action, human-machine reconfigurations, plans vs practice"}
   {:name "Judith Butler"      :concepts "performativity, precarious life, undoing gender, citation and iteration"}
   {:name "Rosi Braidotti"     :concepts "posthuman ethics, nomadic subjects, affirmative ethics, zoe"}
   {:name "Gilles Deleuze"     :concepts "difference, assemblage, control societies, virtuality"}
   {:name "Vilém Flusser"      :concepts "technical images, apparatus, post-history, gestures"}
   {:name "Lewis Mumford"      :concepts "megamachine, technics and civilization, polytechnics vs monotechnics"}
   {:name "Norbert Wiener"     :concepts "cybernetics, feedback, human use of human beings"}
   {:name "Gregory Bateson"    :concepts "ecology of mind, double bind, cybernetic epistemology"}
   {:name "N. Katherine Hayles" :concepts "posthuman, technogenesis, cognitive assemblages, unthought"}
   {:name "Yuk Hui"            :concepts "cosmotechnics, recursivity, technological diversity, Qi"}
   {:name "Alfred North Whitehead" :concepts "process philosophy, prehension, actual occasions, creativity"}
   {:name "Hans-Georg Gadamer" :concepts "hermeneutics, effective history, fusion of horizons, play"}
   {:name "Walter Benjamin"    :concepts "aura, mechanical reproduction, dialectical image, constellation"}
   {:name "Simone Weil"        :concepts "attention, gravity and grace, decreation, affliction"}
   {:name "Isabelle Stengers"  :concepts "cosmopolitics, slow science, ecology of practices, the curse of tolerance"}
   {:name "Wendy Brown"        :concepts "neoliberal rationality, walled states, undoing the demos, political theology of markets"}
   {:name "Luce Irigaray"     :concepts "mechanics of fluids, specular economy, sexual difference, this sex which is not one"}
   {:name "Sara Ahmed"         :concepts "queer phenomenology, orientation, complaint, willfulness, affective economies"}
   {:name "Silvia Federici"    :concepts "reproductive labor, primitive accumulation, Caliban and the Witch, commons"}
   {:name "María Puig de la Bellacasa" :concepts "matters of care, soil politics, touch, speculative ethics of more than human"}
   {:name "Lili Loofbourow"    :concepts "the male glance, underreading, gendered attention, cultural devaluation of women's complexity"}])

(def contemporary-topics
  ["the epistemology of foundation models and what counts as 'knowledge' in a trained network"
   "software supply chain as cultural infrastructure — what we lose when we treat dependencies as inert"
   "the relationship between code review practices and theories of textual interpretation"
   "whether distributed consensus protocols encode political philosophies"
   "the phenomenology of debugging — what kind of knowing is involved in reading a stack trace"
   "technical debt as a form of institutional memory and its relationship to organizational identity"
   "the ethics of automation and what 'replacing' human labor actually means ontologically"
   "how observability tools construct the systems they claim to merely observe"
   "the aesthetics of command-line interfaces and their relationship to literary minimalism"
   "whether version control systems embody a particular philosophy of time and identity"
   "the relationship between type systems and epistemological commitments"
   "how containerization and microservices recapitulate debates about modularity in philosophy of mind"
   "the hermeneutics of error messages — what systems communicate when they fail"
   "whether pair programming is a form of dialectical reasoning"
   "the political economy of open source and its relationship to gift economies"
   "the gendered epistemology of software metrics — what we choose to measure encodes masculine preferences for the quantifiable and competitive over the relational and sustaining"
   "maintenance as reproductive labor — how the structural invisibility of dependency maintenance, documentation, and community work mirrors the devaluation of feminized care work"
   "the male glance in product design — how rapid categorization of user needs systematically undervalues women's experience and complexity"
   "why 'hard' and 'soft' in computing — hard skills, hard real-time, hardware — maps onto a gendered hierarchy that devalues the adaptive, relational, and interpretive"
   "the phenomenology of 'culture fit' — how engineering hiring encodes masculine sociality as technical competence"])

(def existing-posts
  ["'The Big Picture of AI' — Kuhn's paradigm shifts applied to LLM adoption"
   "'The Raft Consensus' — understandability as the driver of Raft's dominance over Paxos"
   "'Code Review as Literary Criticism' — Barthes' death of the author applied to code review"
   "'Observability from the Beginning' — observability as a product requirement, not an ops afterthought"
   "'The Art of Technical Debt' — financial debt as a strategic framework for technical debt"
   "'Deconstructing the Instruction-Data Dichotomy' — Derridean deconstruction of the code/data binary"])

;; ── API interaction ────────────────────────────────────────────────

(defn call-claude
  "Send a message to the Anthropic API. Returns the text content."
  [system-prompt user-message]
  (let [body   {:model      (:model config)
                :max_tokens (:max-tokens config)
                :system     system-prompt
                :messages   [{:role "user" :content user-message}]}
        resp   (http/post (:api-url config)
                          {:headers {"x-api-key"         api-key
                                     "anthropic-version"  (:api-version config)
                                     "content-type"       "application/json"}
                           :body    (json/generate-string body)})
        parsed (json/parse-string (:body resp) true)]
    (if (= 200 (:status resp))
      (->> (:content parsed)
           (filter #(= "text" (:type %)))
           (first)
           (:text))
      (throw (ex-info "API call failed"
                      {:status (:status resp)
                       :body   (:body resp)})))))

;; ── Seed generation ────────────────────────────────────────────────

(defn select-seed
  "Choose a random subset of thinkers and a contemporary topic."
  []
  (let [selected-thinkers (take 3 (shuffle thinkers))
        topic             (rand-nth contemporary-topics)]
    {:thinkers selected-thinkers
     :topic    topic}))

(defn format-seed
  "Format the seed as a string for the ideation prompt."
  [{:keys [thinkers topic]}]
  (str "Today's conceptual seed (randomly selected for cross-pollination):\n"
       (str/join "\n" (map (fn [{:keys [name concepts]}]
                             (str "- " name " — " concepts))
                           thinkers))
       "\n- Contemporary topic: " topic))

;; ── Pipeline stages ────────────────────────────────────────────────

(defn ideate
  "Stage 1: Generate a specific, falsifiable thesis."
  [seed]
  (println "═══ Stage 1: Ideation ═══")
  (let [system "You are the ideation stage of a daily essay pipeline for byatt.io, a personal website publishing serious theoretical work at the intersection of technology and the humanities. The author is a software engineer with deep interests in philosophy of science, literary theory, and distributed systems."
        prompt (str "Existing essays on the site (do NOT repeat these topics):\n"
                    (str/join "\n" (map #(str "- " %) existing-posts))
                    "\n\n"
                    (format-seed seed)
                    "\n\n"
                    "Generate a single specific, falsifiable thesis at the intersection of technology and the humanities. The thesis must:\n"
                    "1. Make a strong, committal claim — not a survey, not a 'raises interesting questions' hedge\n"
                    "2. Be genuinely synthetic — connecting thinkers or ideas not usually connected\n"
                    "3. Be contemporary and relevant to someone building or thinking about software today\n"
                    "4. Be falsifiable — someone should be able to disagree on substantive grounds\n\n"
                    "Output ONLY the thesis (2-4 sentences) and a brief sketch (3-5 bullet points) of the argument's structure. No preamble, no hedging.")
        result (call-claude system prompt)]
    (println "  → Thesis generated")
    result))

(defn critique
  "Stage 2: Adversarial critique. Returns {:verdict :text}."
  [thesis]
  (println "═══ Stage 2: Critique ═══")
  (let [system "You are the adversarial critique stage of an essay pipeline. Your job is to attack the thesis mercilessly and determine whether it is genuinely saying something or is dressed-up conventional wisdom."
        prompt (str "THE THESIS:\n---\n" thesis "\n---\n\n"
                    "Evaluate on three axes. Be ruthless.\n\n"
                    "1. **Is it actually saying something?** Real claim someone could disagree with, or continental vocabulary applied to a tech topic?\n"
                    "2. **Is the synthesis genuine?** Are the thinkers doing real work, or are they decorative citations?\n"
                    "3. **Is any engineering consequence real?** Genuine prediction, or rebranding of existing practice?\n\n"
                    "After your critique, deliver one of three verdicts:\n"
                    "- PASS — thesis is strong enough to write\n"
                    "- REVISE — has merit but needs specific changes (state them)\n"
                    "- REJECT — not worth writing\n\n"
                    "End your response with exactly one of these words on its own line: PASS, REVISE, or REJECT")
        result (call-claude system prompt)
        verdict (cond
                  (re-find #"(?m)^PASS\s*$" result)   :pass
                  (re-find #"(?m)^REVISE\s*$" result)  :revise
                  (re-find #"(?m)^REJECT\s*$" result)  :reject
                  ;; Fall back to searching within text
                  (str/includes? result "REJECT")       :reject
                  (str/includes? result "REVISE")       :revise
                  :else                                 :pass)]
    (println "  → Verdict:" (name verdict))
    {:verdict verdict :text result}))

(defn write-draft
  "Stage 3: Write the essay. Takes thesis and critique revisions."
  [thesis critique-text]
  (println "═══ Stage 3: Writing ═══")
  (let [system "You are the writing stage of a daily essay pipeline for byatt.io. Produce a 500-2000 word essay."
        prompt (str "THE THESIS:\n" thesis "\n\n"
                    "CRITIQUE AND REQUIRED REVISIONS:\n" critique-text "\n\n"
                    "STYLE AND TONE:\n"
                    "- Dense, precise, intellectually ambitious. Strong claims, not hedges.\n"
                    "- Draws on continental philosophy, literary theory, and computer science without being precious.\n"
                    "- Uses concrete technical examples to ground abstract claims.\n"
                    "- Audience: thoughtful engineers and technically-literate humanists.\n"
                    "- No bullet points. Flowing prose, well-paragraphed.\n"
                    "- No 'In conclusion' or 'In this essay I will' scaffolding.\n"
                    "- Structure the essay with markdown ## headings for 3-5 sections.\n\n"
                    "STYLE EXAMPLES (opening lines from existing essays):\n"
                    "- \"A specter is haunting software, the specter of technical debt.\"\n"
                    "- \"It's 9:30 AM on a Tuesday and I've just sat at my desk with a cup of coffee.\"\n"
                    "- \"In the software engineering discourse, a pervasive myth has long shaped our understanding.\"\n\n"
                    "Produce ONLY the essay body. No meta-commentary. No title line — just the ## sections and prose.")
        result (call-claude system prompt)]
    (println "  → Draft written (" (count (str/split result #"\s+")) "words )")
    result))

(defn editorial-review
  "Stage 4: Final quality gate. Returns {:verdict :text}."
  [draft]
  (println "═══ Stage 4: Editorial ═══")
  (let [system "You are the editorial stage of a daily essay pipeline for byatt.io. You are the final quality gate."
        prompt (str "THE SITE'S EXISTING ESSAYS (for register calibration):\n"
                    (str/join "\n" (map #(str "- " %) existing-posts))
                    "\n\nTHE ESSAY:\n---\n" draft "\n---\n\n"
                    "Evaluate: Does it belong on the site? Does it make a real claim? Does any engineering consequence land? Is the prose good? Any line-level problems?\n\n"
                    "Deliver a verdict:\n"
                    "- PUBLISH — ready as-is\n"
                    "- REVISE — with specific fixes (cite exact passages)\n"
                    "- REJECT — not worth publishing\n\n"
                    "End your response with exactly one of these words on its own line: PUBLISH, REVISE, or REJECT")
        result (call-claude system prompt)
        verdict (cond
                  (re-find #"(?m)^PUBLISH\s*$" result) :publish
                  (re-find #"(?m)^REVISE\s*$" result)  :revise
                  (re-find #"(?m)^REJECT\s*$" result)   :reject
                  (str/includes? result "PUBLISH")       :publish
                  (str/includes? result "REJECT")        :reject
                  :else                                  :revise)]
    (println "  → Verdict:" (name verdict))
    {:verdict verdict :text result}))

(defn apply-revisions
  "Stage 4b: Apply editorial revisions to a draft."
  [draft editorial-text]
  (println "═══ Stage 4b: Applying revisions ═══")
  (let [system "You are the revision stage of an essay pipeline. Apply ALL editorial fixes. Preserve everything else. Output ONLY the revised essay."
        prompt (str "EDITORIAL FIXES:\n" editorial-text "\n\nDRAFT:\n" draft "\n\nApply all fixes. Output only the revised essay.")
        result (call-claude system prompt)]
    (println "  → Revisions applied")
    result))

;; ── Output formatting ──────────────────────────────────────────────

(defn generate-metadata
  "Ask Claude to generate front matter fields from the final essay."
  [essay]
  (println "═══ Generating metadata ═══")
  (let [system "You generate metadata for essays. Be precise and concise."
        prompt (str "Given this essay, produce exactly 5 lines of output, nothing else:\n"
                    "Line 1: A short title (3-7 words, no quotes)\n"
                    "Line 2: A subtitle (under 12 words, no quotes)\n"
                    "Line 3: A chain of 3-5 key concepts separated by ' → ' (like: grounding → individuation → milieu → signal)\n"
                    "Line 4: A hook paragraph (2-3 sentences, ~50 words) that summarizes the thesis compellingly for a home page. Use plain text, no markdown.\n"
                    "Line 5: The word DONE\n\n"
                    "THE ESSAY:\n" essay)
        result (call-claude system prompt)
        lines  (str/split-lines (str/trim result))]
    {:title    (str/trim (first lines))
     :subtitle (str/trim (second lines))
     :chain    (str/trim (nth lines 2))
     :hook     (str/trim (nth lines 3))}))

(defn generate-annotations
  "Ask Claude to produce sigils and margin notes for each section of the essay.
   Returns a vector of {:sigil ... :notes [{:ref ... :body ...} ...]} per section."
  [essay]
  (println "═══ Generating annotations ═══")
  (let [system "You generate scholarly annotations for essays. Output valid JSON only, no commentary."
        prompt (str "Given this essay with ## section headings, produce a JSON array with one object per section, in order.\n\n"
                    "Each object must have:\n"
                    "- \"sigil\": a single evocative word or short phrase (1-2 words, lowercase) that captures the section's essence. "
                    "Think of it as a gutter mark in a manuscript — conceptual, not descriptive. Examples: 'megamachine', 'mauss', 'cosmotechnics', 'falsify'.\n"
                    "- \"notes\": an array of 1-2 margin notes for this section. Each note has:\n"
                    "  - \"ref\": a label like 'REF // Author Year' or 'NOTE // concept' (uppercase REF or NOTE, then //, then detail)\n"
                    "  - \"body\": 1-3 sentences of scholarly annotation — a reference, a counterpoint, a connection the main text doesn't make. "
                    "Written in the style of Talmudic marginalia: concise, precise, adding a layer the reader wouldn't get from the main text alone.\n\n"
                    "Output ONLY the JSON array. No markdown fences, no explanation.\n\n"
                    "THE ESSAY:\n" essay)
        result (call-claude system prompt)
        ;; Strip any markdown code fences if present
        cleaned (-> result
                    (str/replace #"(?s)^```json?\s*" "")
                    (str/replace #"(?s)\s*```\s*$" "")
                    (str/trim))]
    (try
      (let [parsed (json/parse-string cleaned true)]
        (println "  → Annotations generated for" (count parsed) "sections")
        parsed)
      (catch Exception e
        (println "  → Warning: failed to parse annotations JSON, using empty annotations")
        (println "  → " (.getMessage e))
        []))))

(defn idx->roman [n]
  (get {0 "I" 1 "II" 2 "III" 3 "IV" 4 "V" 5 "VI" 6 "VII" 7 "VIII" 8 "IX" 9 "X"} n (str n)))

(defn essay->treatise-shortcodes
  "Convert a markdown essay with ## headings into illuminated treatise shortcodes.
   Annotations is a vector of {:sigil ... :notes [...]} per section."
  [essay annotations]
  (let [;; Split on ## headings
        sections (rest (str/split essay #"(?m)^## "))]
    (->> sections
         (map-indexed
          (fn [idx section]
            (let [lines     (str/split-lines section)
                  heading   (str/trim (first lines))
                  body      (str/trim (str/join "\n" (rest lines)))
                  num       (format "%02d" (inc idx))
                  ;; Get annotation for this section, or default
                  ann       (nth annotations idx nil)
                  sigil     (or (:sigil ann)
                                (-> heading str/lower-case (str/replace #"[^a-z0-9\s]" "")
                                    (str/split #"\s+") first))
                  notes     (or (:notes ann) [])
                  ;; First section gets a drop cap
                  dropcap?  (zero? idx)
                  ;; Wrap paragraphs in <p> tags if not already
                  body-html (as-> body b
                              (str/split b #"\n\n+")
                              (map (fn [para]
                                     (let [para (str/trim para)]
                                       (if (or (str/starts-with? para "<")
                                               (str/starts-with? para "{{"))
                                         para
                                         (str "<p>" para "</p>"))))
                                   b)
                              (str/join "\n\n" b))
                  ;; For drop cap, extract first letter from first <p>
                  body-html (if dropcap?
                              (str/replace-first
                               body-html
                               #"<p>(.)(.*?)</p>"
                               (fn [[_ first-char rest-text]]
                                 (str "{{< dropcap letter=\""
                                      (str/upper-case first-char)
                                      "\" >}}\n\n<p>"
                                      rest-text "</p>")))
                              body-html)
                  ;; Build margin notes
                  margin-html (if (seq notes)
                                (str/join "\n"
                                          (map (fn [{:keys [ref body]}]
                                                 (str "{{< note ref=\"" ref "\" >}}\n"
                                                      body "\n"
                                                      "{{< /note >}}"))
                                               notes))
                                "")
                  ;; Section rule before all but first section
                  rule      (if (pos? idx)
                              "{{< section-rule >}}\n\n"
                              "")]
              (str rule
                   "{{< section num=\"" num "\" label=\"§ " (idx->roman idx) ".  " heading "\" >}}\n\n"
                   "{{< gutter sigil=\"" sigil "\" >}}\n\n"
                   "{{< main-col >}}\n\n"
                   body-html "\n\n"
                   "{{< /main-col >}}\n\n"
                   "{{< margin >}}\n"
                   margin-html "\n"
                   "{{< /margin >}}"))))
         (str/join "\n\n"))))

(defn format-ephemeron
  "Assemble the final ephemeron markdown file."
  [essay metadata annotations today-str]
  (let [body (essay->treatise-shortcodes essay annotations)]
    (str "---\n"
         "title: \"" (:title metadata) "\"\n"
         "subtitle: \"" (:subtitle metadata) "\"\n"
         "date: " today-str "\n"
         "draft: false\n"
         "layout: treatise\n"
         "doc_class: \"ephemeron\"\n"
         "doc_id: \"ephemeron." today-str "\"\n"
         "revision: \"diurnal\"\n"
         "chain: \"" (:chain metadata) "\"\n"
         "footer_status: \"ephemeral unless preserved\"\n"
         "status_pips:\n"
         "  - active\n"
         "  - amber\n"
         "  - dim\n"
         "hook: \"" (str/replace (:hook metadata) "\"" "\\\"") "\"\n"
         "generated_by: \"Claude Opus 4.6\"\n"
         "generated_date: \"" (.format (java.time.LocalDate/parse today-str)
                                        (java.time.format.DateTimeFormatter/ofPattern "d MMMM yyyy")) "\"\n"
         "---\n\n"
         body "\n")))

;; ── Persistence check ──────────────────────────────────────────────

(defn check-comments
  "Query GitHub Discussions to see if yesterday's ephemeron has comments.
   If so, persist it before overwriting."
  []
  (println "═══ Checking for comments on current ephemeron ═══")
  (let [gh-token (System/getenv "GITHUB_TOKEN")
        query    "{\"query\": \"{ repository(owner: \\\"wbyatt\\\", name: \\\"byatt.io\\\") { discussions(categoryId: \\\"DIC_kwDOMvtUZM4C56QU\\\", first: 20) { nodes { title url comments { totalCount } } } } }\"}"
        resp     (when gh-token
                   (try
                     (http/post "https://api.github.com/graphql"
                                {:headers {"Authorization" (str "Bearer " gh-token)
                                           "Content-Type"  "application/json"}
                                 :body    query})
                     (catch Exception e
                       (println "  → GitHub API error:" (.getMessage e))
                       nil)))]
    (if (and resp (= 200 (:status resp)))
      (let [parsed      (json/parse-string (:body resp) true)
            discussions  (get-in parsed [:data :repository :discussions :nodes])
            ephemeron-d  (first (filter #(str/includes? (or (:title %) "") "ephemeron")
                                        discussions))
            has-comments (and ephemeron-d
                              (pos? (get-in ephemeron-d [:comments :totalCount] 0)))]
        (when has-comments
          (println "  → Comments found! Persisting current ephemeron.")
          (let [source   (str (:repo-root config) "/content/ephemeron.md")
                content  (slurp source)
                ;; Extract date from front matter
                date-m   (re-find #"date:\s*(\d{4}-\d{2}-\d{2})" content)
                date-str (or (second date-m) "undated")
                dest     (str (:repo-root config) "/content/posts/ephemeron-" date-str ".md")]
            ;; Copy to posts, remove ephemeral-specific fields
            (spit dest (-> content
                           (str/replace #"footer_status:.*\n" "footer_status: \"preserved from ephemeron\"\n")
                           (str/replace #"doc_class:.*\n" "doc_class: \"ephemeron/preserved\"\n")
                           (str/replace #"hook:.*\n" "")))
            (println "  → Persisted to" dest)))
        (println "  → No comments found, ephemeron will be overwritten."))
      (println "  → No GITHUB_TOKEN or API error, skipping persistence check."))))

;; ── Git operations ─────────────────────────────────────────────────

(defn git-commit-and-push!
  "Stage and commit the ephemeron, then push."
  [today-str]
  (println "═══ Publishing ═══")
  (let [repo (:repo-root config)]
    (proc/shell {:dir repo} "git" "add" "content/ephemeron.md")
    ;; Also add any persisted ephemeron
    (try (proc/shell {:dir repo} "git" "add" "content/posts/ephemeron-*.md")
         (catch Exception _))
    (proc/shell {:dir repo} "git" "commit" "-m"
                (str "ephemeron: " today-str "\n\nGenerated by Claude Opus 4.6 via scripts/ephemeron.clj"))
    (proc/shell {:dir repo} "git" "push" "origin" "main")
    (println "  → Pushed to origin/main")))

;; ── Main pipeline ──────────────────────────────────────────────────

(defn run-pipeline
  "Execute the full dialectical pipeline."
  []
  (let [today-str (.toString (java.time.LocalDate/now))
        seed      (select-seed)]

    (println "\n╔══════════════════════════════════════════════╗")
    (println "║  ephemeron pipeline — " today-str "          ║")
    (println "╚══════════════════════════════════════════════╝\n")
    (println "Seed:" (format-seed seed) "\n")

    ;; Check if current ephemeron should be persisted
    (check-comments)

    ;; Stage 1: Ideation (with retry on REJECT)
    (loop [attempt 0]
      (when (> attempt (:max-retries config))
        (throw (ex-info "Max retries exceeded at ideation/critique" {:attempt attempt})))

      (let [thesis         (ideate seed)
            ;; Stage 2: Critique
            {:keys [verdict text]} (critique thesis)]

        (case verdict
          :reject
          (do (println "  → REJECTED, retrying with fresh seed...")
              (recur (inc attempt)))

          ;; :pass or :revise both proceed to writing
          (let [;; Stage 3: Write
                draft          (write-draft thesis text)
                ;; Stage 4: Editorial
                {:keys [verdict text]} (editorial-review draft)
                final-essay    (case verdict
                                 :publish draft
                                 :revise  (apply-revisions draft text)
                                 :reject  (throw (ex-info "Editorial rejected the draft" {:editorial text})))
                ;; Generate metadata and annotations
                metadata       (generate-metadata final-essay)
                annotations    (generate-annotations final-essay)
                ;; Format as ephemeron
                ephemeron-md   (format-ephemeron final-essay metadata annotations today-str)
                output-path    (str (:repo-root config) "/content/ephemeron.md")]

            ;; Write the file
            (spit output-path ephemeron-md)
            (println "\n═══ Written to" output-path "═══")

            ;; Git commit and push (only if GITHUB_TOKEN is set, indicating CI)
            (when (System/getenv "GITHUB_TOKEN")
              (git-commit-and-push! today-str))

            (println "\n╔══════════════════════════════════════════════╗")
            (println "║  pipeline complete                           ║")
            (println "║  title: " (:title metadata))
            (println "╚══════════════════════════════════════════════╝\n")))))))

;; ── Entry point ────────────────────────────────────────────────────

(run-pipeline)
