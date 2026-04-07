---
description: "Review the NomNom CI/CD pipeline and suggest improvements for caching, security, testing, and reliability."
agent: "agent"
---

Review the GitHub Actions CI/CD pipeline for the NomNom project and suggest improvements.

## Instructions

1. Read the workflow file at [ci.yml](.github/workflows/ci.yml)
2. Analyze for gaps in the following areas:

### Caching & Performance
- Is Gradle dependency caching configured?
- Is the Gradle build cache enabled?
- Is Node.js `node_modules` cached?
- Are there unnecessary repeated steps?

### Testing & Quality
- Are test results uploaded as artifacts?
- Is there a JaCoCo or other code coverage report?
- Are test failures clearly visible in the PR?

### Security
- Is there a dependency vulnerability scan (e.g., `gradle dependencyCheckAnalyze`, Trivy, or Snyk)?
- Are secrets handled properly?
- Is there a SAST scan step?

### Reliability
- Are job dependencies configured correctly?
- Is there a timeout on jobs?
- Are specific versions pinned for actions (not `@latest`)?

### Missing Steps
- Is there a lint step for the frontend?
- Is there a Docker build step?
- Is there a deployment step (even if just to staging)?

## Output Format

Present findings as a numbered list of **actionable suggestions**, each with:
- **What**: The improvement
- **Why**: The benefit
- **How**: A code snippet showing the change to `ci.yml`
