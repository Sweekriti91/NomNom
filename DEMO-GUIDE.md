# Copilot CLI Demo Guide — NomNom

## Repo State

- **13 open issues** — all CI/CD enhancements
- **1 open PR** — `#1` driver ETA tracking (with planted bugs)
- **1 closed PR** — `#15` dependency vulnerability scanning
- **3 workflows** — NomNom CI, Copilot Code Review, Copilot Cloud Agent
- **2 branches** — `main`, `feature/driver-eta-tracking`

---

## 🎆 WOW Demos

| # | Demo | Flow |
|---|------|------|
| 1 | **`/review` on PR #1** | `/review` on `feature/driver-eta-tracking` — watch Copilot find the null pointer, missing validation, and hardcoded secret independently |
| 2 | **Fix PR #1 with Rubber Duck** | `"Fix all the issues in PR #1"` in Plan mode → Rubber Duck critiques the plan → agent fixes all 3 bugs → `/diff` to review |
| 3 | **Solve issue #9 end-to-end** | `"Implement #9 — matrix strategy for parallel service builds"` in Autopilot → agent reads the issue, edits `ci.yml`, pushes, creates PR |
| 4 | **`/delegate` an issue** | `"Fix #7 — add Gradle and npm caching to CI"` then `/delegate` → hands off to Copilot cloud agent on GitHub to create the PR autonomously |
| 5 | **Multi-issue blitz** | Autopilot + `/yolo` → `"Implement #6 and #7 together — add concurrency control and dependency caching to CI"` — two issues, one shot |
| 6 | **`/research` CI best practices** | `"/research What are the best CI/CD practices for Spring Boot microservices with GitHub Actions?"` → full report, then use it to tackle the open issues |
| 7 | **Docker from scratch** | `"Implement #8 — create Dockerfiles for all 4 services and add a Docker build job to CI"` → agent creates 4 Dockerfiles + workflow changes |
| 8 | **Frontend lint setup** | `"Implement #4 — add ESLint to the frontend with a CI lint step"` → agent installs deps, creates config, adds npm script, updates workflow |
| 9 | **Test coverage pipeline** | `"Implement #11 — add JaCoCo coverage to all services"` → agent edits every `build.gradle.kts`, adds CI step, sets thresholds |
| 10 | **Code review + `/pr`** | Push a deliberate test failure → `/pr` → agent reads CI logs, diagnoses, fixes, pushes — all from the terminal |

---

## 💡 Pro Tips (using your repo)

| # | Tip | How to show it |
|---|-----|---------------|
| 1 | **`#9` issue mention** | Type `"Implement #9"` — Copilot auto-fetches the issue body with all the tasks and context |
| 2 | **`@` file context** | `"Add caching to @.github/workflows/ci.yml"` — targeted file context |
| 3 | **`/instructions`** | Show the audience your `api-contracts.instructions.md` and `react-components.instructions.md` are loaded — explain how they guide the agent |
| 4 | **`/env`** | Show everything loaded: custom agents (`test-writer`, `orderflow-api`), skills (`testing`), instruction files |
| 5 | **Plan mode for CI** | `Shift+Tab` → Plan → `"How should we implement CodeQL scanning (#13)?"` — get a plan without changing anything |
| 6 | **`/model` switching** | Start with Haiku for quick questions about your CI, switch to Opus for implementing the complex matrix strategy |
| 7 | **`/context` awareness** | Show context usage after loading several `@` files — demonstrate why `/compact` matters |
| 8 | **Custom agents** | `"Use the test-writer agent to add tests for order-service"` — show your `.github/agents/test-writer.agent.md` in action |
| 9 | **`/diff` mid-flight** | While agent is implementing #8 (Dockerfiles), run `/diff` to peek at progress |
| 10 | **`/share gist`** | After a great demo session, `/share gist` → share the URL with the audience in real-time |

---

## ⚡ Power User Flows (using your issues)

| # | Flow | Commands |
|---|------|----------|
| 1 | **Issue triage** | `"Look at all open issues and suggest which to tackle first based on dependencies"` — agent reads all 13 issues via GitHub MCP |
| 2 | **Batch CI improvements** | `"Implement #6, #7, and #3 together in one branch"` — concurrency + caching + test reports in a single workflow update |
| 3 | **PR #1 full lifecycle** | `git checkout feature/driver-eta-tracking` → `/review` → fix bugs → run tests → `/pr` to update the PR |
| 4 | **Security audit** | `"Implement #13 (CodeQL) and #12 (dependency scanning) — build a complete security pipeline"` |
| 5 | **Test + Coverage combo** | `"Implement #11 (JaCoCo) and #10 (JUnit test reporter) together, then add real tests for order-service"` |
| 6 | **Autopilot Dockerization** | `/yolo on` → Autopilot → `"Implement #8 and #2 — Dockerfiles + GHCR push on main"` — full container pipeline |
| 7 | **Frontend CI pipeline** | `"Implement #4 (ESLint) and #5 (preview deployment) for the frontend"` — lint + deploy preview |
| 8 | **Cross-reference check** | `"Are there any duplicate issues? Compare #12 and #14"` — agent notices they're identical and suggests closing one |
| 9 | **`/chronicle standup`** | After running several demos: `/chronicle standup` → auto-generated report of everything you just did |
| 10 | **One-liner roadmap** | `copilot -p "List all open issues and create a project roadmap"` — generates a prioritized roadmap from your 13 issues |

---

## 🎬 Suggested Demo Arc

**"From Issues to Production Pipeline in 15 minutes"**

1. `/env` → show the setup
2. `"Look at open issues and prioritize"` → agent triages 13 issues
3. Pick #6 + #7 → Autopilot implements both
4. `/diff` → review changes
5. `/review` on PR #1 → find the planted bugs
6. Fix the bugs → `/pr` to update
7. `/share html` → export the session
8. `/chronicle standup` → "here's what we just did"

---

## 🏆 Top Rubber Duck Demo Prompt

> **"Add an endpoint to place an order from the frontend — when a user clicks Order on a restaurant page, create the order and show a confirmation notification."**

Why it wows: The agent plans a full-stack implementation, and Rubber Duck catches the **existing `totalAmount`/`totalPrice` contract mismatch** before code is written. Real bug, unscripted.

---

## 📋 Pre-Demo Checklist

- [ ] `/experimental on` — enables Rubber Duck, Autopilot, Chronicle
- [ ] `/model` → select Claude Sonnet 4.6 (activates Rubber Duck with GPT-5.4)
- [ ] `/streamer-mode` — hide quota and preview model names
- [ ] `/env` — verify instructions, agents, skills are loaded
- [ ] Ensure `feature/driver-eta-tracking` branch is available locally

---

## 🔑 Key Slash Commands Reference

| Command | What it does | Status |
|---------|-------------|--------|
| `/env` | Show loaded environment | GA |
| `/review` | AI code review | GA |
| `/diff` | Review changes in terminal | GA |
| `/pr` | Create/fix PRs, fix CI, address feedback | GA |
| `/delegate` | Hand off to Copilot cloud agent | GA |
| `/share html/gist` | Export session | GA |
| `/instructions` | View/toggle instruction files | GA |
| `/model` | Switch AI model | GA |
| `/usage` | Session stats & token breakdown | GA |
| `/context` | Context window visualization | GA |
| `/compact` | Compress conversation history | GA |
| `/ide` | Connect to VS Code | GA |
| `/remote` | Remote control from web/mobile | Preview |
| `/streamer-mode` | Hide sensitive info | GA |
| `/yolo` | Allow-all permissions | GA |
| `/research` | Deep research with citations | GA |
| Autopilot (Shift+Tab) | Autonomous agent mode | Experimental |
| Rubber Duck | Cross-model review agent | Experimental |
| `/chronicle` | Session insights & standup | Experimental |
| Extensions | Custom tools & hooks | Experimental |
