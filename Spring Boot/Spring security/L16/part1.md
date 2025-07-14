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

