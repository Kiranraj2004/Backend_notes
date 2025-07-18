
## 🌱 **Spring Boot Profiles - Introduction**

### 🔹 What are Profiles?

- **Profiles** in Spring Boot allow you to define different configurations for different environments like:
    
    - `development` (dev)
        
    - `production` (prod)
        
    - `staging`, `local`, `test`, etc.
        

### 🔹 Why use Profiles?

- You don't want the same database, server, or sensitive configuration for development and production.
    
- Helps avoid using real user data in development.
    
- Enables environment-specific behavior.
    

---

## 🧪 **Example Scenario: Journal App**

- In development:
    
    - You can use mock data (e.g., users like Ram, Shyam, Ghanshyam).
        
    - You are free to experiment.
        
- In production:
    
    - Real users interact with the app.
        
    - You should have **read-only** access to prevent data corruption.
        
    - A different DB and credentials should be used.
        

---

## ⚙️ **Setting Up Profiles with `.yml` Files**

### 📁 Create Profile Files:

- By default, Spring Boot uses `application.yml` or `application.properties`.
    
- You can create separate profile-specific files:
    
    ```bash
    application.yml              # Default profile (optional)
    application-dev.yml         # For development
    application-prod.yml        # For production
    ```
    

### 📝 How to Activate a Profile:

In `application.yml`, set the active profile like this:

```yaml
spring:
  profiles:
    active: dev
```

This means Spring Boot will load `application-dev.yml`.

---

## 📁 **Directory Structure Example:**

```plaintext
src/
└── main/
    └── resources/
        ├── application.yml                 # Default
        ├── application-dev.yml            # Dev profile
        └── application-prod.yml           # Prod profile
```

---

## 🔁 **Profile Switching**

You can switch profiles in 3 ways:

### 1. **From `application.yml`**

```yaml
spring:
  profiles:
    active: prod
```

### 2. **From IntelliJ (or IDE)**

- Go to **Run > Edit Configurations**
    
- Add under "Environment Variables":
    
    ```
    SPRING_PROFILES_ACTIVE=dev
    ```
    

### 3. **From Command Line (while running the JAR)**:

```bash
java -jar target/app.jar --spring.profiles.active=prod
```

or

```bash
java -Dspring.profiles.active=prod -jar target/app.jar
```

✅ This is **best practice for production** (do not hard-code the active profile in `application.yml`).

---

## 🧪 **Sample Configuration Difference**

### application-dev.yml

```yaml
server:
  port: 8080
spring:
  datasource:
    url: jdbc:mysql://localhost/devdb
    username: devuser
    password: devpass
```

### application-prod.yml

```yaml
server:
  port: 8081
spring:
  datasource:
    url: jdbc:mysql://prod-db-server/proddb
    username: produser
    password: prodpass
```

---

## 📦 **Running & Packaging**

### During Build (Maven):

```bash
mvn clean package
```

- If you have tests using `@SpringBootTest`, the context will load using the active profile.
    

### During Run (JAR):

```bash
java -jar target/app.jar --spring.profiles.active=prod
```

---

## 📌 **Best Practices**

- Use `application.yml` for common/shared config.
    
- Use profile-specific files for environment-specific overrides.
    
- Avoid committing production secrets/configs into version control.
    
- Prefer using command-line args or environment variables to activate profiles in production.
    

---

## ✅ Summary

|Environment|File|Profile Name|
|---|---|---|
|Development|`application-dev.yml`|`dev`|
|Production|`application-prod.yml`|`prod`|
|Common|`application.yml`|Default|

- Profile activation priority: `Command line` > `Environment Variable` > `application.yml`
    
- Always isolate sensitive configuration per environment.
    

# part 2


- Differences between JVM system properties and application arguments
    
- Real-world deployment behavior
    
- Profile-based bean loading
    
- Testing with profiles
    
- Externalizing sensitive properties
    
- Reading active profiles programmatically
    

---

## 🌐 **1. JVM System Properties vs. Application Arguments**

### ➤ Two Ways to Pass Profiles:

#### ✅ **Application Argument (Recommended for running JARs)**

```bash
java -jar app.jar --spring.profiles.active=dev
```

- This passes the profile to the Spring Boot application directly.
    
- Commonly used in deployment (e.g., staging, production).
    
- Spring interprets it as a **command-line argument**, not as a JVM system property.
    

#### ✅ **JVM System Property (Used with Maven or other tools)**

```bash
mvn clean package -Dspring.profiles.active=dev
```

- `-D` sets a **JVM system property**.
    
- Mainly used during **build time** with Maven.
    

### ⚠️ Difference:

- `--spring.profiles.active=dev` → passed **to the application**
    
- `-Dspring.profiles.active=dev` → passed **to the JVM**
    

---

## 🏢 **2. Real-World Production Setup**

### 🧩 Local Development:

- You usually set the profile in IntelliJ under:
    
    - **Run > Edit Configurations > Environment variables:**
        
        ```env
        SPRING_PROFILES_ACTIVE=dev
        ```
        

### 🚫 Access to Production DB:

- Local systems usually cannot access production DB.
    
- **Production DB servers are whitelisted** to allow only specific servers to connect.
    
- Avoid hardcoding `prod` profile in local machine's configuration.
    

---

## 🤖 **3. Automated Deployment with Jenkins**

- Jenkins automates build, test, and deployment tasks.
    
- Button-based execution of predefined commands (e.g., `java -jar app.jar --spring.profiles.active=prod`).
    
- May be set up for:
    
    - Staging
        
    - Production
        
    - Or not used at all (depends on the company)
        

---

## 🔐 **4. Profile-Based Bean Loading**

You can create **beans and configurations specific to profiles** using the `@Profile` annotation.

### ✅ Example:

```java
@Configuration
@Profile("dev")
public class DevSecurityConfig {
    // permitAll() and dev-friendly settings
}
```

```java
@Configuration
@Profile("prod")
public class ProdSecurityConfig {
    // stricter authentication, secure settings
}
```

➡️ When `dev` profile is active, only `DevSecurityConfig` beans will load.  
➡️ When `prod` is active, `ProdSecurityConfig` will take over.

---

## 🧪 **5. Using Profiles in Tests**

Spring allows specifying active profiles in test classes:

```java
@ActiveProfiles("dev")
@SpringBootTest
public class UserServiceTest {
    // Only dev profile beans/configurations will load
}
```

You can test different environments (e.g., `prod`) by changing the value of `@ActiveProfiles`.

---

## 🔒 **6. Externalizing Sensitive Properties**

### 🔹 Problem:

- Properties files (`application-dev.yml`, `application-prod.yml`) contain **DB credentials** and **sensitive information**.
    
- If pushed to GitHub, they can expose secrets.
    

### 🔹 Solution: External Configuration

- Keep sensitive files **outside the JAR**.
    
- During run time, pass the location like this:
    

```bash
java -jar app.jar --spring.config.location=file:/path/to/external/application-prod.yml
```

- Ensure the external path exists on the **deployment server**.
    

---

## 🔎 **7. Programmatically Reading Active Profiles**

Spring provides ways to access the currently active profile(s) in your code:

```java
@Autowired
private Environment environment;

@PostConstruct
public void checkProfile() {
    String[] activeProfiles = environment.getActiveProfiles();
    System.out.println("Active Profile: " + activeProfiles[0]);
}
```

- `environment.getActiveProfiles()` returns a **String array**, hence multiple profiles can be active.
    

---

## 🧩 **8. Activating Multiple Profiles Simultaneously**

You can **activate more than one profile**:

```yaml
spring:
  profiles:
    active: dev,db
```

- Configurations from both `application-dev.yml` and `application-db.yml` will be loaded.
    
- Useful for modular config management (e.g., separating DB and web configs).
    

---

## 📝 **9. Recap of Key Points**

|Concept|Command/Method|
|---|---|
|Maven build with profile|`mvn clean package -Dspring.profiles.active=dev`|
|Run JAR with profile|`java -jar app.jar --spring.profiles.active=prod`|
|JVM property (for build)|`-Dspring.profiles.active=dev`|
|Application argument (runtime)|`--spring.profiles.active=dev`|
|IntelliJ Run Config|Environment variable: `SPRING_PROFILES_ACTIVE=dev`|
|External properties location|`--spring.config.location=file:/path/to/app-prod.yml`|
|Profile annotation|`@Profile("dev")`, `@Profile("prod")`|
|Reading active profile|`environment.getActiveProfiles()`|
|Multiple profiles|`spring.profiles.active=dev,db`|

---

Let me know if you'd like a working codebase or a practice project with dev and prod profiles implemented!