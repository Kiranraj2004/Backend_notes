---

## 🔐 **Spring Security in Spring Boot – Detailed Notes**

---
 
🔐 **Spring Security in Spring Boot – Detailed Notes**

### 🔸 Why Use Spring Security?

- Until now, the controllers only used the **username**, with **no password** — making the endpoints **completely insecure**.
    
- Anyone could access them via REST clients like Postman.
    
- **Current issue**: Username is being sent as a **path parameter**, which is **not secure**.
    
- ✅ The correct approach is to **avoid sending sensitive data in URLs or request parameters**.
    

---

## 🔹 What is Spring Security?

Spring Security is a **security framework** for:

- **Authentication**: Verifying _who_ the user is.
    
- **Authorization**: Determining _what_ the authenticated user is allowed to do.
    

### 🔍 Examples:

- ✅ _Authentication_: If you provide correct username/password, you are authenticated.
    
- ✅ _Authorization_: If authenticated, are you allowed to read/write/delete certain resources?
    

---

## 🔹 Adding Spring Security to a Spring Boot Project

### ✅ Step 1: Add Spring Security Dependency

```xml
<!-- Add this to your pom.xml -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

📌 **After adding this dependency**:

- All endpoints become **secured by default**.
    
- You can no longer access any API without **authentication**.
    

---

## 🔹 Default Behavior After Adding Spring Security

- Spring Boot applies **HTTP Basic Authentication** by default.
    
- This means:
    
    - Each API call must include an `Authorization` header with encoded `username:password`.
        

---

### 🔸 How HTTP Basic Authentication Works:

1. **Client (Postman)** sends a request to the server.
    
2. Server expects a header:
    
    ```
    Authorization: Basic <base64(username:password)>
    ```
    
3. If credentials are valid → ✅ Access is **granted**.  
    Else → ❌ HTTP 401 Unauthorized is returned.
    

---

### 🔑 Example in Postman:

1. Go to **Authorization** tab → Select **Basic Auth**
    
2. Enter username: `user`  
    Password: (check your Spring Boot console for random password on startup)
    
3. Postman will send a header like:
    
    ```
    Authorization: Basic dXNlcjpwYXNzd29yZA==
    ```
    
    (This is `user:password` base64 encoded)
    

---

### 🔸 Default Credentials

- Spring Boot generates:
    
    - **Default username**: `user`
        
    - **Default password**: Randomly generated and **printed in the console**
        

> ✅ You can override it in `application.properties`:

```properties
spring.security.user.name=admin
spring.security.user.password=admin123
```

---

## 🔹 Need for Customization

Problems with default setup:

- Only **one default user**
    
- All endpoints are secured equally
    
- You may want to:
    
    - Create **multiple users**
        
    - Set **roles** (admin, user, etc.)
        
    - Allow/deny access based on **URL patterns or roles**
        

---

## 🔸 Customizing Spring Security Configuration

### ✅ Step 1: Create a `@Configuration` Class

```java
package com.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
	public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Custom security configuration will go here
    }

    // You can also override configure(AuthenticationManagerBuilder auth) to set up users
}
```

---

### 🔍 Annotations Explained

- `@Configuration`: Marks the class as a Spring configuration.
    
- `@EnableWebSecurity`: Enables Spring Security and indicates that you want to **customize security behavior**.
    
- `extends WebSecurityConfigurerAdapter`:
    
    - This gives access to **override** default security behavior.
        

---

### 🔧 Overriding Methods

1. `configure(HttpSecurity http)`
    
    - Define **which endpoints** are secure and how.
        
2. `configure(AuthenticationManagerBuilder auth)`
    
    - Define **users, passwords, and roles** (in-memory or JDBC, etc.)
        

---

### 🛠 Example: Allow all requests to `/public`, secure others

```java
@Override
protected void configure(HttpSecurity http) throws Exception {
    http
        .authorizeRequests()
        .antMatchers("/public/**").permitAll() // No auth needed
        .anyRequest().authenticated()          // Auth required for others
        .and()
        .httpBasic(); // Enable HTTP Basic Auth
}
```

---

### 🧪 In IntelliJ (or any IDE):

- Create a new `config` package
    
- Inside, create `SecurityConfig.java`
    
- Extend `WebSecurityConfigurerAdapter`
    
- Annotate with `@EnableWebSecurity`
    
- Override `configure()` methods
    

---

### 🔄 Summary of Flow

|Step|Action|
|---|---|
|1|Add Spring Security dependency|
|2|All endpoints become secured|
|3|Use default user/pass or define your own|
|4|Add Basic Auth header in Postman|
|5|Customize security with `SecurityConfig`|
|6|Override methods to define roles, permissions, and endpoint security|

---



## Question 
---

## 🔐 Spring Security Config (Old vs New)

### 1. **Before Spring Security 6 (Boot ≤ 2.x)**

- Extend `WebSecurityConfigurerAdapter`
    
- Override `configure(HttpSecurity http)`
    

Example:

```java
@Override
protected void configure(HttpSecurity http) throws Exception {
    http
        .authorizeRequests()
        .antMatchers("/public/**").permitAll()
        .anyRequest().authenticated()
        .and()
        .httpBasic();
}
```

---

### 2. **Spring Security 6 (Boot 3.x)**

- `WebSecurityConfigurerAdapter` **removed**
    
- Use a `SecurityFilterChain` **@Bean**
    
- Replace `.antMatchers()` with `.requestMatchers()`
    

Example:

```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests()
            .requestMatchers("/public/**").permitAll()
            .requestMatchers("/user/**").hasRole("USER")
            .requestMatchers("/admin/**").hasRole("ADMIN")
            .anyRequest().authenticated()
        .and()
        .httpBasic(); // old style (works but deprecated in 6.1)
    return http.build();
}
```

---

### 3. **Spring Security 6.1+ (Latest DSL)**

- Old style (`httpBasic()`, `authorizeHttpRequests()`, method chaining) **deprecated**
    
- Use **lambda-based DSL**
    

Example:

```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/public/**", "/login", "/signup").permitAll()
            .requestMatchers("/user/**").hasRole("USER")
            .requestMatchers("/admin/**").hasRole("ADMIN")
            .anyRequest().authenticated()
        )
        .httpBasic(basic -> {})       // new lambda API
        .csrf(csrf -> csrf.disable()); // optional for APIs
    return http.build();
}
```

---

### 🔑 Key Changes Across Versions

|Version|API|Notes|
|---|---|---|
|≤ Spring Security 5|`WebSecurityConfigurerAdapter` + `antMatchers()` + `.httpBasic()`|Works in Boot 2.x|
|Spring Security 6.0|`SecurityFilterChain` + `requestMatchers()` + `.httpBasic()`|Adapter removed|
|Spring Security 6.1+|Lambda DSL → `.authorizeHttpRequests(auth -> …)` + `.httpBasic(basic -> {})`|Old style deprecated|

---

👉 So:

- If you’re on **Boot 2.x** → use `WebSecurityConfigurerAdapter`.
    
- If you’re on **Boot 3.0** → use `SecurityFilterChain` + `requestMatchers()`.
    
- If you’re on **Boot 3.1+** → always use **lambda DSL** to avoid deprecation warnings.
    


## question 2 ? how to check  spring boot version

Good question 👍 — knowing your **Spring Boot** and **Spring Security** versions is important since the APIs keep changing.

Here are the ways to check:

---

## 🔍 1. Check **Spring Boot Version**

### (a) From `pom.xml` (Maven project)

Look for the **parent** section:

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.2.1</version>  <!-- ✅ Spring Boot version -->
    <relativePath/> 
</parent>
```

or sometimes:

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-dependencies</artifactId>
            <version>3.1.4</version> <!-- ✅ Boot version -->
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

### (b) From Gradle (`build.gradle`)

```groovy
plugins {
    id 'org.springframework.boot' version '3.2.1' // ✅ Boot version
}
```

### (c) Run the app

When you start your Spring Boot app, the log shows:

```
:: Spring Boot :: (v3.2.1)
```

---

## 🔍 2. Check **Spring Security Version**

Spring Security is pulled in **via Spring Boot starters** (transitively).  
You can check in these ways:

### (a) Maven command

Run:

```bash
mvn dependency:list | grep spring-security
```

You’ll see something like:

```
org.springframework.security:spring-security-core:6.2.0
org.springframework.security:spring-security-web:6.2.0
org.springframework.security:spring-security-config:6.2.0
```

### (b) Gradle command

```bash
./gradlew dependencies | grep spring-security
```

### (c) IntelliJ / IDE

- Open **External Libraries** section → search for `spring-security-*`.
    
- Versions will be displayed.
    

---

## 🔑 Version Mapping (Boot ↔ Security)

Spring Boot manages Spring Security version for you:

- **Spring Boot 2.7.x** → Spring Security 5.8.x
    
- **Spring Boot 3.0.x** → Spring Security 6.0.x
    
- **Spring Boot 3.1.x** → Spring Security 6.1.x
    
- **Spring Boot 3.2.x** → Spring Security 6.2.x
    

---

👉 So the easiest check is:

- **Check Boot version in `pom.xml` / logs**
    
- Then map it to the corresponding **Security version**




## question what that auth in lambada function 


Great catch 👌 — let’s unpack what that `auth` in

```java
.authorizeHttpRequests(auth -> auth
    .requestMatchers("/public/**").permitAll()
    .requestMatchers("/user/**").hasRole("USER")
    .anyRequest().authenticated()
)
```

actually means.

---

## 🔑 What is `auth` here?

- `authorizeHttpRequests()` takes a **lambda expression**.
    
- That lambda receives an argument (commonly called `auth`).
    
- `auth` is just a variable name (you can call it anything, e.g., `config`, `reg`, etc.).
    

Technically:

- `auth` is of type **`AuthorizeHttpRequestsConfigurer.AuthorizationManagerRequestMatcherRegistry`**.
    
- It provides the **fluent API methods** like:
    
    - `.requestMatchers(...)`
        
    - `.permitAll()`
        
    - `.authenticated()`
        
    - `.hasRole("...")`
        
    - `.hasAuthority("...")`
        

---

## ✅ Example (with different variable names)

```java
http.authorizeHttpRequests(auth -> auth
    .requestMatchers("/public/**").permitAll()
    .requestMatchers("/admin/**").hasRole("ADMIN")
    .anyRequest().authenticated()
);
```

is the same as:

```java
http.authorizeHttpRequests(registry -> registry
    .requestMatchers("/public/**").permitAll()
    .requestMatchers("/admin/**").hasRole("ADMIN")
    .anyRequest().authenticated()
);
```

or even:

```java
http.authorizeHttpRequests(config -> config
    .requestMatchers("/public/**").permitAll()
    .requestMatchers("/admin/**").hasRole("ADMIN")
    .anyRequest().authenticated()
);
```

---

## 🔎 Analogy

Think of it like:

```java
list.forEach(item -> System.out.println(item));
```

Here `item` is just a variable name for each element in the list.  
Similarly, `auth` is just the variable name for the **configurer object** you’re customizing.

---

👉 In short:

- `auth` is a **configurer object** passed by Spring Security inside the lambda.
    
- You use it to define which requests need authentication, which are public, and which need specific roles/authorities.
    