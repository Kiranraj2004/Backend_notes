

- Authenticating users from MongoDB
    
- Creating a `UserDetailsService` implementation
    
- Setting up user roles for authorization
    
- Preparing Spring Security configuration for custom login and access control
    

---

## 🔐 **Spring Security – Authenticating Users from MongoDB (Custom UserDetailsService)**

---

### 🎯 **Goal**

Allow Spring Boot to authenticate users using credentials stored in **MongoDB**, not the default in-memory user.

---

### 🪜 **Steps to Implement Custom Authentication**

#### ✅ 1. **Create a User Entity (`User.java`)**

Fields required:

```java
@Document
public class User {
    @Id
    private String id;
    private String username;
    private String password; // Stored as encoded (hashed) string
    private List<String> roles; // e.g., ["ROLE_USER"], ["ROLE_ADMIN"]
    // Getters, Setters, Constructors
}
```

✅ `@Document` ensures the class maps to a MongoDB collection.

✅ `roles` field helps define **authorization** (what access the user has).

---

#### ✅ 2. **Create a User Repository (`UserRepository.java`)**

Extend MongoRepository:

```java
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
}
```

Purpose:

- To query user details by username when authentication is attempted.
    

---

#### ✅ 3. **Implement `UserDetailsService` Interface**

🔹 Spring Security uses `UserDetailsService` to **load user details by username** for authentication.

Create `CustomUserDetailsService.java`:

```java
@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository
                        .findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return org.springframework.security.core.userdetails.User
                .builder()
                .username(user.getUsername())
                .password(user.getPassword()) // Must be already encoded!
                .roles(user.getRoles().toArray(new String[0]))
                .build();
    }
}
```

---

### ⚙️ Explanation:

- `UserDetailsService` is part of Spring Security.
    
- Its only method, `loadUserByUsername()`, must be implemented.
    
- We use the repository to fetch a user by username.
    
- The result is mapped to Spring's built-in `User` class using `.builder()`.

for more details click here for and go for q3 about building userdetails
[[Questions]][[]]
---

#### 🔸 `roles.toArray(new String[0])`

- Converts `List<String>` to `String[]` because `User.builder().roles()` expects an array.
    
- This allows assigning multiple roles like `"USER"` or `"ADMIN"` to the authenticated user.
    

---

### ⚠️ Error Handling

If user is not found:

```java
throw new UsernameNotFoundException("User not found");
```

Spring Security will automatically respond with **401 Unauthorized**.

---

### ✨ Quick Recap of What’s Done So Far:

|Step|Component|Status|
|---|---|---|
|1|User Entity|✅ Created with username, password, roles|
|2|User Repository|✅ Interface to interact with MongoDB|
|3|UserDetailsService|✅ Implemented custom logic to fetch user|

---

### 📌 Additional Key Points:

#### 🔒 Password Security

- Passwords in the DB **must be encoded** using something like `BCryptPasswordEncoder`.
    
- Storing plain-text passwords is **insecure**.
    

#### 🔑 Authentication Flow with Custom User:

1. User sends request with **username and password** (via basic auth header).
    
2. Spring Security calls `loadUserByUsername(username)`.
    
3. This fetches the user from MongoDB.
    
4. Credentials are validated.
    
5. If matched, a session or basic auth state is established.
    

---

### 🔜 Next Steps (Coming in the Video / To Be Implemented):

- Encode passwords before saving using `BCryptPasswordEncoder`.
    
- Define a security configuration class and inject the custom `UserDetailsService`.
    
- Set up authorization logic to restrict access:
    
    - Users can only access their own journal entries.
        
    - Admins can have separate roles and limited visibility.
        
- Optionally implement `Remember Me`, CSRF handling, and custom login page.
    

---

Let me know if you'd like detailed code examples for:

- Password encoding with `BCrypt`
    
- Security configuration class
    
- Role-based access to endpoints using `@PreAuthorize` or `.hasRole("USER")` style restrictions



# flow  of spring security


## 🔹 Step-by-Step Flow

### 1. **Request comes in**

- The user hits an endpoint, e.g. `/user/profile`.
    
- Your `SecurityFilterChain` checks the request:
    

```java
.authorizeHttpRequests(auth -> auth
    .requestMatchers("/public/**").permitAll()   // ✅ skip security
    .requestMatchers("/user/**").hasRole("User") // ✅ must be authenticated + have role
    .requestMatchers("/admin/**").hasRole("Admin")
    .anyRequest().authenticated()
)
```

👉 If `/public/**`, it goes **straight to controller**.  
👉 Otherwise, Spring says: “This needs authentication.”

---

### 2. **Authentication process starts**

- Since you enabled `.httpBasic()`, the client must send credentials (`username`, `password`) in the **Authorization header**:
    

```
Authorization: Basic base64(username:password)
```

- Spring extracts those credentials and creates a **`UsernamePasswordAuthenticationToken`** with them.
    

---

### 3. **AuthenticationManager kicks in**

Your bean:

```java
@Bean
public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
    AuthenticationManagerBuilder authBuilder =
            http.getSharedObject(AuthenticationManagerBuilder.class);

    authBuilder
            .userDetailsService(customUserDetailsService)
            .passwordEncoder(passwordEncoder());

    return authBuilder.build();
}
```

- This tells Spring:  
    “When you authenticate, use `customUserDetailsService` to load users and `BCryptPasswordEncoder` to check passwords.”
    

---

### 4. **CustomUserDetailsService is called**

Spring passes the username into your method:

```java
@Override
public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username); // 🔍 DB lookup
    if(user == null) throw new UsernameNotFoundException("User not found");

    return org.springframework.security.core.userdetails.User.builder()
            .username(user.getUsername())
            .password(user.getPassword())   // (already hashed in DB)
            .roles(user.getRoles().toArray(new String[0]))
            .build();
}
```

👉 This does **not yet verify the password**.  
It only **loads the user** and returns a `UserDetails` object with username, encoded password, and roles.

---

### 5. **Password verification**

- Spring Security takes the raw password from the login attempt.
    
- It calls your configured `PasswordEncoder` (`BCryptPasswordEncoder`) to check if:
    

```
matches(rawPasswordFromRequest, encodedPasswordFromDB)
```

👉 If it matches → authentication success.  
👉 If not → `BadCredentialsException`.

---

### 6. **SecurityContext is updated**

On success:

- Spring creates a new **`Authentication` object** (with username, authorities, etc.)
    
- It stores it inside `SecurityContextHolder`.
    

Now, anywhere in your app, you can access the current user:

```java
Authentication auth = SecurityContextHolder.getContext().getAuthentication();
String username = auth.getName();                  // logged-in username
Collection<? extends GrantedAuthority> roles = auth.getAuthorities(); // roles
```

---

## 🔹 Your Understanding (checked ✅)

- ✅ Public endpoints skip authentication → directly hit controller
    
- ✅ Other endpoints → Spring calls `AuthenticationManager`
    
- ✅ `AuthenticationManager` uses `UserDetailsService` to fetch user by username
    
- ✅ Password is verified by Spring (using your `PasswordEncoder`)
    
- ✅ On success → user details are stored in `SecurityContext`
    

So your mental model is correct ✔️.
