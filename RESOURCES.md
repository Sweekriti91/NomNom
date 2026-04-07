# NomNom Demo — Resources & References

## Your Project
- **Repo**: https://github.com/Sweekriti91/NomNom
- **Feature Branch (Code Review)**: `feature/driver-eta-tracking`

---

## Official Documentation

### VS Code + Java
| Resource | URL |
|----------|-----|
| Getting Started with Java in VS Code | https://code.visualstudio.com/docs/java/java-tutorial |
| Java Editing in VS Code | https://code.visualstudio.com/docs/java/java-editing |
| Java Testing (JUnit) in VS Code | https://code.visualstudio.com/docs/java/java-testing |
| Java Debugging in VS Code | https://code.visualstudio.com/docs/java/java-debugging |
| Spring Boot in VS Code | https://code.visualstudio.com/docs/java/java-spring-boot |
| Java Project Management | https://code.visualstudio.com/docs/java/java-project |

### VS Code Copilot Customization
| Resource | URL |
|----------|-----|
| Customization Overview (instructions, agents, prompts, skills) | https://code.visualstudio.com/docs/copilot/copilot-customization |
| Custom Instructions | https://code.visualstudio.com/docs/copilot/customization/custom-instructions |
| Custom Agents | https://code.visualstudio.com/docs/copilot/customization/custom-agents |
| Prompt Files | https://code.visualstudio.com/docs/copilot/customization/prompt-files |
| Agent Skills | https://code.visualstudio.com/docs/copilot/customization/agent-skills |
| Hooks | https://code.visualstudio.com/docs/copilot/customization/hooks |
| Customize AI for Your Project (Guide) | https://code.visualstudio.com/docs/copilot/guides/customize-copilot-guide |

### VS Code Integrated Browser
| Resource | URL |
|----------|-----|
| Integrated Browser Docs | https://code.visualstudio.com/docs/debugtest/integrated-browser |
| Browser Agent Testing Guide | https://code.visualstudio.com/docs/copilot/guides/browser-agent-testing-guide |

### GitHub Copilot Code Review
| Resource | URL |
|----------|-----|
| Using Copilot Code Review | https://docs.github.com/en/copilot/using-github-copilot/code-review/using-copilot-code-review |
| About Copilot Code Review | https://docs.github.com/en/copilot/concepts/code-review |
| Automatic Code Review Setup | https://docs.github.com/en/copilot/how-tos/agents/copilot-code-review/automatic-code-review |

### Spring Boot
| Resource | URL |
|----------|-----|
| Spring Boot 3.4 Release Notes | https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.4-Release-Notes |
| Spring Boot 3.4 Announcement | https://spring.io/blog/2024/11/21/spring-boot-3-4-0-available-now |
| Spring Boot Reference Docs | https://docs.spring.io/spring-boot/reference/ |
| Spring Initializr | https://start.spring.io/ |

### Gradle
| Resource | URL |
|----------|-----|
| Gradle Kotlin DSL Primer | https://docs.gradle.org/current/userguide/kotlin_dsl.html |
| Gradle Java Toolchains | https://docs.gradle.org/current/userguide/toolchains.html |

---

## VS Code Extensions Used

| Extension | ID | Purpose |
|-----------|----|---------|
| Extension Pack for Java | `vscjava.vscode-java-pack` | Core Java support (language server, debugger, test runner, Maven/Gradle) |
| Spring Boot Extension Pack | `vmware.vscode-boot-dev-pack` | Spring Boot dashboard, live hover, properties intellisense |
| Language Support for Java (Red Hat) | `redhat.java` | Java language server — linting, IntelliSense, refactoring |
| Gradle for Java | `vscjava.vscode-gradle` | Gradle task runner and project support |
| SonarQube for IDE (SonarLint) | `sonarsource.sonarlint-vscode` | Real-time code quality & security linting |
| Checkstyle for Java | `shengchen.vscode-checkstyle` | Code style enforcement (import order, naming) |
| GitHub Copilot Chat | `github.copilot-chat` | AI chat, agents, inline completions |
| GitHub Pull Requests | `github.vscode-pull-request-github` | PR management and code review from VS Code |
| ESLint | `dbaeumer.vscode-eslint` | Frontend JavaScript linting |
| Tailwind CSS IntelliSense | `bradlc.vscode-tailwindcss` | Tailwind class autocomplete for React frontend |

---

## Frontend Stack

| Tool | URL |
|------|-----|
| React 18 | https://react.dev/ |
| Vite 5 | https://vite.dev/ |
| Tailwind CSS 3 | https://tailwindcss.com/ |
| Tailwind Dark Mode | https://tailwindcss.com/docs/dark-mode |

---

## YouTube / Video Resources

### VS Code + Java
| Video | URL |
|-------|-----|
| Java in VS Code (Official Channel) | https://www.youtube.com/playlist?list=PLj6YeMhvp2S7abEHqkUPRkCSt4N1L2sP- |
| Getting Started with Java in VS Code | https://www.youtube.com/watch?v=jLMT0LlmSuA |

### GitHub Copilot
| Video | URL |
|-------|-----|
| GitHub Copilot YouTube Channel | https://www.youtube.com/@GitHub |
| GitHub Copilot Dev Days | https://aka.ms/githubcopilotdevdays |
| Copilot Agent Mode (VS Code) | https://www.youtube.com/watch?v=6cPTkMbVNqo |

### Spring Boot Microservices
| Video | URL |
|-------|-----|
| Spring Boot Microservices (Official Spring) | https://www.youtube.com/c/SpringSourceDev |

---

## Key VS Code Settings for Demo

```json
{
  "workbench.browser.enableChatTools": true,
  "workbench.browser.openLocalhostLinks": true
}
```

---

## Planted Bugs (Code Review Demo)

Branch: `feature/driver-eta-tracking`

| # | Bug | File | What Code Review Should Catch |
|---|-----|------|-------------------------------|
| 1 | Null pointer | `DeliveryTrackingService.java` line ~64 | `tracking.getDriverName().toUpperCase()` — driver may be null when not yet assigned |
| 2 | Missing input validation | `UpdateEtaRequest.java` | `etaMinutes` accepts negative values, no `@Min(0)` or `@NotNull` annotation |
| 3 | Hardcoded secret | `DeliveryTracking.java` line ~33 | API key hardcoded as string constant instead of env variable |
