
#### 1. Why bother with code‑quality tools?

|Traditional flow|Gap it leaves|
|---|---|
|✅ Write business logic → ✅ QA tests → ✅ Ship to prod|🚫 No automated check that the code is **clean, maintainable, secure, or standards‑compliant**|

Modern teams therefore wire a _continuous code‑quality gate_ into their pipelines; the Sonar platform is the de‑facto standard.

---

#### 2. The Sonar family at a glance

|Tool|Runs where?|Typical use|
|---|---|---|
|**SonarQube**|Your own server / container|Full dashboard for _local_ or _on‑prem_ analysis|
|**SonarCloud**|sonarcloud.io (SaaS)|Same analysis but maintained by SonarSource; hooks natively into GitHub/GitLab/Azure|
|**SonarLint**|IDE plugin (IntelliJ, VS Code, etc.)|Inline feedback while you type; same rule set as the server|

---

#### 3. Setting up **SonarQube 7.9.6 LTS** locally

1. **Prerequisites** 
    
    - **JDK 11** is mandatory for 7.9+ servers. ([Sonar Community](https://community.sonarsource.com/t/upgrading-to-7-9-version-sonarqube-requires-java-11-to-run/14836?utm_source=chatgpt.com "Upgrading to 7.9 version - SonarQube requires Java 11+ to run"))
        
    - Ensure `JAVA_HOME` and _PATH_ point to that JDK.
        
2. **Download & extract** `sonarqube-7.9.6.zip`.
    
3. **Start the server** (Windows example):
    
    ```bash
    # inside the extracted folder
    bin\windows-x86-64\StartSonar.bat
    ```
    
4. **Log in** at `http://localhost:9000` (default credentials `admin / admin`). ([Sonar Community](https://community.sonarsource.com/t/disable-default-password-change/41920?utm_source=chatgpt.com "Disable default password change - Sonar Community"))
    

---

#### 4. Wiring a Maven project to SonarQube

1. **Add the Maven Scanner plugin** to your `pom.xml` (parent section works too):
    
    ```xml
    <plugin>
      <groupId>org.sonarsource.scanner.maven</groupId>
      <artifactId>sonar-maven-plugin</artifactId>
      <version>3.11.0.3924</version>
    </plugin>
    ```
    
2. **Run the analysis** (tests skipped here to avoid blocking CI on failing tests):
    
    ```bash
    mvn -DskipTests clean verify sonar:sonar \
        -Dsonar.projectKey=journal-app \
        -Dsonar.host.url=http://localhost:9000 \
        -Dsonar.login=<generated-token>
    ```
    
    > After the goal finishes, refresh SonarQube and the **Journal‑app** project will appear automatically.
    

---

#### 5. Reading the dashboard

|Metric|Meaning|Typical fix|
|---|---|---|
|**Bugs**|Code that can crash or give wrong results|Unit tests + logic fix|
|**Vulnerabilities**|Potential security issues|Validate inputs, apply least‑privilege|
|**Code Smells**|Maintainability problems (long methods, wildcard imports, etc.)|Refactor / follow clean‑code patterns|
|**Coverage**|% of lines/branches exercised by tests|Add/enable unit & integration tests|
|**Duplications**|Copy‑pasted blocks|Extract methods, reuse utilities|

These map to Sonar’s three issue types (Bug, Vulnerability, Code Smell). ([SonarQube Documentation](https://docs.sonarsource.com/sonarqube-server/9.8/user-guide/concepts/?utm_source=chatgpt.com "An overview of the key concepts used within SonarQube."))

_Quality Gate_ = a rule set (Sonar Way by default) that the build must pass to be marked **green**.

---

#### 6. Example issue from the transcript – _“Persistent entity exposed”_

> _“Replace this persistent entity with a simple POJO / DTO.”_

**Problem** – Controller accepts the **JPA entity** directly:

```java
@PostMapping("/users")
public ResponseEntity<User> create(@RequestBody User user) { … }
```

**Why it’s risky** 

- Exposes DB internals in the API.
    
- Validation constraints bleed into the entity.
    
- Harder to evolve schema without breaking clients.
    

**Refactor with a DTO**

```java
// DTO
public record UserDto(String username, String email) {}

// Mapper (could use MapStruct)
public interface UserMapper {
    User toEntity(UserDto dto);
    UserDto toDto(User entity);
}

// Controller
@PostMapping("/users")
public ResponseEntity<UserDto> create(@RequestBody UserDto dto) {
    User saved = service.save(mapper.toEntity(dto));
    return ResponseEntity.ok(mapper.toDto(saved));
}
```

---

#### 7. Fixing transcript‑highlighted smells

|Smell|Quick remedy|
|---|---|
|`System.out.println(...)` calls|Replace with **SLF4J** logger:|

````java
private static final Logger log = LoggerFactory.getLogger(UserService.class);
log.error("Creating user failed", e);
``` |
| Wildcard imports (`import java.util.*`) | Use explicit imports or IDE *optimize imports* |
| Duplicate code blocks | Factor common logic into utility/service methods |

---

#### 8. Installing **SonarLint** in IntelliJ  

1. `Settings / Plugins → Marketplace → SonarQube for IDE (SonarLint)` → *Install*. :contentReference[oaicite:3]{index=3}  
2. Restart IDE.  
3. Violations are underlined live with the same rule IDs you saw on the server.

---

#### 9. Using **SonarCloud** with GitHub  

1. **Sign in** at `sonarcloud.io` using GitHub OAuth.  
2. **Create an organisation & project** (free plan covers public repos).  
3. During project onboarding choose **“With GitHub Actions”**; the wizard shows exact YAML. :contentReference[oaicite:4]{index=4}  
4. Minimal workflow (current *sonarqube‑scan‑action*):

 ```yaml
 name: SonarCloud
 on: [push, pull_request]
 jobs:
   analyze:
     runs-on: ubuntu-latest
     steps:
       - uses: actions/checkout@v4
       - uses: sonarsource/sonarqube-scan-action@v2
         with:
           host-url: https://sonarcloud.io
           token: ${{ secrets.SONAR_TOKEN }}
````

_Add `SONAR_TOKEN` and, for private repos, `SONAR_ORG` & `SONAR_PROJECT_KEY` secrets._

---

#### 10. When to choose which tool

|Scenario|Best choice|
|---|---|
|Corporate network, no external traffic|**Self‑hosted SonarQube**|
|OSS / quick SaaS setup|**SonarCloud**|
|Tight IDE feedback loop|**SonarLint** (optionally _connected‑mode_ to share rules with SonarQube/Cloud)|

---

#### 11. Quick checklist you can copy‑paste into your README

```markdown
### Quality‑Gate Checklist
- [ ] JDK 11+ installed & JAVA_HOME set  
- [ ] SonarQube running (`localhost:9000`) or SonarCloud project created  
- [ ] `sonar-maven-plugin` in *pom.xml*  
- [ ] `mvn -DskipTests clean verify sonar:sonar` passes locally  
- [ ] No **Blocker/Critical** issues; Quality Gate = Passed  
- [ ] SonarLint enabled in IDE  
- [ ] GitHub Action (`sonarqube-scan-action@v2`) in CI
```

---

### Key take‑aways

_Treat Sonar’s findings like compiler errors:_ fix them early, keep the gate green, and your reviewers, auditors, and future‑you will thank you.

Happy (clean) coding! 🚀




#### 1 ▪ Installing Git for Windows

|Step|Action|Hint|
|---|---|---|
|1|Navigate to **git‑scm.com → Downloads** and click **“Download for Windows”**|macOS & Linux ship with Git; only Windows needs the installer|
|2|Run the installer → keep the **default options** (“Next → Next → Finish”)|Accept “Git Bash here” context‑menu for convenience|
|3|After install, open **Git Bash** from Start‑menu or right‑click “Git Bash Here” in your project folder|Git Bash gives a Linux‑like CLI on Windows|

---

#### 2 ▪ Initialising a local repository

```bash
ls                     # list files
git status             # “fatal: not a git repository…”
git init               # create .git folder
git status             # now shows untracked files
```

---

#### 3 ▪ Crafting a solid `.gitignore`

1. `nano .gitignore` (or use VS Code)
    
2. Typical exclusions for a **Maven / Spring Boot** project:
    

```
# Build outputs
/target/
/*.log

# IDE files
/.idea/
/*.iml

# Reports
/html-reports/

# Tools
/.mvn/wrapper/
```

3. Save → `git status` should now hide the ignored paths.
    

> **Tip:** Keep `.gitignore` committed so the whole team shares the same rules.

---

#### 4 ▪ First commit with correct author identity

```bash
# Only needed once per machine
git config --global user.email "you@example.com"
git config --global user.name  "Your Name"

git add .                 # stage everything except .gitignored files
git commit -m "First commit"
```

---

#### 5 ▪ Publishing to GitHub

|Step|Command / Action|
|---|---|
|Create a **new repo** on GitHub (public or private)||
|Copy the HTTPS URL (e.g. `https://github.com/you/journal-app.git`)||
|```bash||
|git remote add origin||
|git push -u origin master # or main||

````|
| Git prompts **Git Credential Manager** → click *“Sign‑in with browser”* and approve OAuth flow |

After the push, refreshing the GitHub page shows your source tree.

---

#### 6 ▪ Cloning for others

```bash
git clone https://github.com/you/journal-app.git
````

(Anyone can clone a public repo; private repos need auth.)

---

#### 7 ▪ Adding a **SonarCloud token** to GitHub Secrets

1. In the GitHub repo: **Settings → Secrets and variables → Actions → “New repository secret”**
    
2. Name it **`SONAR_TOKEN`**, paste the token you generated on SonarCloud → **Add secret**.
    

---

#### 8 ▪ Injecting the token into Maven

Inside your `pom.xml` `<properties>` block:

```xml
<properties>
    <java.version>17</java.version>
    <sonar.token>${env.SONAR_TOKEN}</sonar.token>
</properties>
```

(Using the environment variable keeps the token out of source control.)

---

#### 9 ▪ Creating a GitHub Actions workflow (`.github/workflows/build.yml`)

```bash
# From project root
mkdir -p .github/workflows
nano .github/workflows/build.yml
```

Paste (wizard‑generated) YAML:

```yaml
name: CI with SonarCloud
on: [push, pull_request]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: 17     # align with project
      - name: Build & SonarCloud scan
        env:
          SONAR_TOKEN: ${{ secrets.SONAR_TOKEN }}
        run: mvn -B -DskipTests clean verify sonar:sonar \
             -Dsonar.projectKey=journal-app \
             -Dsonar.organization=your-org \
             -Dsonar.host.url=https://sonarcloud.io
```

Commit & push:

```bash
git add .github/workflows/build.yml pom.xml
git commit -m "Add SonarCloud CI workflow"
git push
```

GitHub Actions will start a build; on success the scan appears in SonarCloud → your project.

---

#### 10 ▪ Troubleshooting quick‑ref

|Symptom|Fix|
|---|---|
|**`fatal: unable to access …`** when pushing|Check proxy/VPN; make sure Git Credential Manager authenticated|
|Workflow fails at _“No credentials provided”_|Ensure `SONAR_TOKEN` secret is created **and** referenced as env var|
|Workflow runs but **scan not visible**|Verify `sonar.organization` & `sonar.projectKey` match names in SonarCloud|
|`.github/workflows` ignored by Git|Remove any rule in `.gitignore` that matches `.github` or use `git add -f`|

---

### Mini‑Checklist to replicate the video flow

```text
☐ Git for Windows installed, Git Bash available
☐ git init  →  .gitignore crafted & committed
☐ user.name / user.email configured
☐ GitHub repo created & remote added
☐ First push succeeds
☐ SONAR_TOKEN secret added
☐ sonar.token property in pom.xml
☐ .github/workflows/build.yml committed
☐ GitHub Actions passes & SonarCloud dashboard shows new analysis
```

With these steps your Spring Boot project is now under version control **and** wired to a continuous code‑quality gate—mirroring exactly what the video demonstrated.