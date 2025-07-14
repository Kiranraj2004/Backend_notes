
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

