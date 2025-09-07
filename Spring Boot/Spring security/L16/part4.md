
## ✅ Goal

To configure a **Spring Boot application** so that:

- Users are authenticated based on credentials stored in **MongoDB**.
    
- Passwords are stored in **hashed (encoded)** form using **BCrypt**.
    
- Secure endpoints using **Spring Security**.
    
- Disable **CSRF** where necessary for stateless REST APIs.
    
- Different users (e.g., Ram and Shyam) only access their own journal entries.
    

---

## 📌 Steps to Enable Custom Authentication in Spring Boot

---

### **1. Create the User Entity**

```java
@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String username;
    private String password;
    private List<String> roles; // E.g., ROLE_USER, ROLE_ADMIN
}
```

🧠 **Roles** are used for **authorization**. A user can have multiple roles (e.g., normal user, admin).

---

### **2. Create the User Repository**

```java
public interface UserRepository extends MongoRepository<User, String> {
    User findByUsername(String username);
}
```

---

### **3. Implement UserDetailsService**

Spring Security provides `UserDetailsService` interface. You must override:

```java
@Override
public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username);
    if (user == null) {
        throw new UsernameNotFoundException("User not found");
    }

    return org.springframework.security.core.userdetails.User
            .withUsername(user.getUsername())
            .password(user.getPassword()) // Already hashed
            .roles(user.getRoles().toArray(new String[0]))
            .build();
}
```

📌 This method is used by Spring Security to:

- Fetch user data from DB.
    
- Build a `UserDetails` object needed for authentication.
    

---

### **4. Configure Spring Security**

In your `SecurityConfig` class (extending `WebSecurityConfigurerAdapter`):

#### a. Register your `UserDetailsService` and `PasswordEncoder`

```java
@Autowired
private CustomUserDetailsService userDetailsService;

@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}

@Override
protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService)
        .passwordEncoder(passwordEncoder());
}
```

#### b. Configure HTTP Security

```java
@Override
protected void configure(HttpSecurity http) throws Exception {
    http
        .csrf().disable() // Disable CSRF for REST APIs (stateless)
        .authorizeRequests()
            .antMatchers("/journal/**").authenticated() // Secure journal endpoints
            .anyRequest().permitAll()
        .and()
        .httpBasic(); // Use HTTP Basic Auth
}
```

---

## 🔒 Why Disable CSRF?

- CSRF (Cross Site Request Forgery) is a protection mechanism for **stateful** web forms (e.g., browser-based login).
    
- For **stateless APIs** (e.g., Postman or frontend frameworks like React), you **disable CSRF**.
    
- If enabled, you'd need to pass a CSRF token with each request, which isn't practical for APIs.
    

🔐 CSRF protection prevents:

- Malicious sites from making unauthorized requests on behalf of logged-in users.
    
- It requires a **CSRF token** embedded in HTML forms (e.g., bank websites).
    

---

## 🧪 Creating Users with Encrypted Passwords

In your `UserController`:

```java
@PostMapping("/saveNewUser")
public ResponseEntity<User> saveNewUser(@RequestBody User user) {
    PasswordEncoder encoder = new BCryptPasswordEncoder();
    user.setPassword(encoder.encode(user.getPassword()));
    user.setRoles(Collections.singletonList("ROLE_USER")); // Assign default role
    return ResponseEntity.ok(userRepository.save(user));
}
```

📌 Now:

- Passwords are stored in hashed format.
    
- You can verify hashed passwords using `BCryptPasswordEncoder.matches(raw, encoded)` (done internally by Spring Security).
    

---

## 🔐 Authentication Flow Recap

1. **User sends credentials (username, plain-text password)** via HTTP Basic Auth.
    
2. Spring Security:
    
    - Fetches user from DB using `loadUserByUsername()`.
        
    - Hashes incoming password using the same encoder (`BCrypt`).
        
    - Compares the hash with the one stored in DB.
        
3. If match → Authentication successful, else → 401 Unauthorized.
    

---

## 🧪 Testing with Postman

- Create users by sending POST request to `/saveNewUser`.
    
- Login-protected endpoints like `/journal/**` will require authentication.
    
- Provide credentials in Postman under **Authorization → Basic Auth**.
    

---

## ✅ Summary of Features Implemented

|Feature|Description|
|---|---|
|User Entity with roles|For authentication & authorization|
|User Repository|To query MongoDB for users|
|Custom `UserDetailsService`|Tells Spring how to load user from DB|
|Password Encoding with BCrypt|Secure password storage|
|Secure endpoints|Only authenticated users can access `/journal/**`|
|CSRF disabled|For stateless server-to-server or API calls|
|Default role assignment|On user creation (`ROLE_USER`)|

---



## how to configure authentication in version 6.5 security

Got it 👍 let’s debug carefully.  
Since you’re migrating from `configure(AuthenticationManagerBuilder auth)` to Spring Security 6.5+, the **most common errors** are:

1. **`No qualifying bean of type 'AuthenticationManager' available`**  
    → You must explicitly declare it as a `@Bean`.
    
2. **Password mismatch (`Bad credentials`)**  
    → Because the password in DB is not encoded with `BCryptPasswordEncoder`.
    
3. **Role mismatch (`AccessDeniedException`)**  
    → Because Spring expects `ROLE_USER` instead of just `USER`.
    

---

Here’s the **clean working version** (minimal setup with MongoDB, Spring Security 6.5+):

### 🔹 `UserDetailsService`

```java
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword()) // must already be BCrypt encoded
                .authorities(user.getRoles().stream()
                        .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role) // normalize roles
                        .map(SimpleGrantedAuthority::new)
                        .toList())
                .build();
    }
}
```

---

### 🔹 `SecurityConfig`

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    // ✅ Security rules
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/public/**").permitAll()
                        .requestMatchers("/user/**").hasRole("USER")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults()); // Basic Auth
        return http.build();
    }

    // ✅ AuthenticationManager bean (replacement for configure(AuthenticationManagerBuilder))
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }

    // ✅ Password encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

---

### 🔹 Register User Correctly

When inserting into MongoDB, always encode the password:

```java
AppUser user = new AppUser();
user.setUsername("john");
user.setPassword(passwordEncoder.encode("password123")); // must be encoded
user.setRoles(Set.of("USER")); // will automatically become ROLE_USER
userRepository.save(user);
```

---

✅ With this setup:

- `/public/**` is open
    
- `/user/**` requires `ROLE_USER`
    
- `/admin/**` requires `ROLE_ADMIN`
    
- All other requests require authentication
    

---

👉 Can you paste the **exact error message** you’re getting?  
That way I can pinpoint whether it’s a **bean definition issue**, **password encoding**, or **role mismatch**.






##  Q1 )in modern  version you can't use and()

Ah, yes 👍 you’re absolutely right.  
Starting with **Spring Security 6.1**, the old `.and()` chaining style is **deprecated** and will be removed.

That’s why you’re seeing the warning on:

```java
http.getSharedObject(AuthenticationManagerBuilder.class)
    .userDetailsService(customUserDetailsService)
    .passwordEncoder(passwordEncoder())
    .and()   // 🚨 deprecated
    .build();
```

---

### ✅ The modern way (Spring Security 6.1+)

Instead of chaining with `.and()`, you configure the `AuthenticationManagerBuilder` directly:

```java
@Bean
public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
    AuthenticationManagerBuilder authBuilder =
            http.getSharedObject(AuthenticationManagerBuilder.class);

    authBuilder
            .userDetailsService(customUserDetailsService)
            .passwordEncoder(passwordEncoder());

    return authBuilder.build(); // no .and()
}
```

---

### Full Updated Config (Clean)

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/public/**").permitAll()
                        .requestMatchers("/user/**").hasRole("USER")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        authBuilder
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder());

        return authBuilder.build(); // ✅ no .and()
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

---
