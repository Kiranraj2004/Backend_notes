
## ✅ Objectives Covered

- Make authentication **stateless** using `SessionCreationPolicy.STATELESS`.
    
- Create secure **update** and **delete** user endpoints.
    
- **Fetch the logged-in user’s username** using Spring Security's `SecurityContext`.
    
- Understand how Spring handles **username/password authentication** internally.
    
- Secure endpoints by roles or authenticated users only.
    
- Avoid exposing sensitive or unnecessary endpoints (e.g., get all users).
    

---

## 🔐 Stateless Authentication Setup

By default, **Spring Security maintains a session**. To make your application stateless (ideal for APIs):

```java
@Override
protected void configure(HttpSecurity http) throws Exception {
    http
        .csrf().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeRequests()
            .antMatchers("/journal/**").authenticated()
            .anyRequest().permitAll()
        .and()
        .httpBasic(); // Enable Basic Authentication
}
```

### 🔍 Why `STATELESS`?

- Every request must carry credentials.
    
- No session is stored on the server.
    
- Better suited for REST APIs and tools like Postman.
    

---

## ✅ Authentication Recap (Request Lifecycle)

1. Client (e.g., Postman) sends credentials via **Basic Auth header**.
    
2. Spring Security:
    
    - Looks up the user in the DB via `UserDetailsService`.
        
    - Validates the password using `BCryptPasswordEncoder`.
        
3. If valid → user is authenticated, and the username is stored in `SecurityContext`.
    

---

## 📦 Public vs. Secured Endpoints

### 🔓 Public Controller

Endpoints that do **not require authentication**, like:

```java
@RequestMapping("/public")
@RestController
public class PublicController {
    @PostMapping("/user")
    public ResponseEntity<User> createUser(...) {
        // Register new user
    }

    @GetMapping("/health")
    public String healthCheck() {
        return "OK";
    }
}
```

These are accessible to anyone, including anonymous users.

---

### 🔒 Secured Endpoints

Move update/delete into authenticated controller:

```java
@RequestMapping("/user")
@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody User user, Authentication auth) {
        String username = auth.getName(); // Authenticated username
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Collections.singletonList("ROLE_USER")); // Optional
        userRepository.save(user);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser(Authentication auth) {
        String username = auth.getName();
        userRepository.deleteByUsername(username);
        return ResponseEntity.noContent().build();
    }
}
```

---

## 💡 Key Concepts Used

### 🧾 Get Authenticated User Details

```java
Authentication auth = SecurityContextHolder.getContext().getAuthentication();
String username = auth.getName(); // Retrieves currently logged-in user's username
```

Or inject `Authentication` directly into controller method:

```java
@PutMapping
public ResponseEntity<?> updateUser(@RequestBody User user, Authentication auth) { ... }
```

### 🔒 Secure Only Specific Endpoints

You can specify which paths need authentication:

```java
http.authorizeRequests()
    .antMatchers("/journal/**", "/user/**").authenticated()
    .anyRequest().permitAll();
```

---

## ⚙️ Changes to User Repository

Add method to support deleting user:

```java
void deleteByUsername(String username);
```

---

## 📌 Test Cases with Postman

### 🟢 Register (public)

- Endpoint: `POST /public/user`
    
- Body: `{ "username": "ram", "password": "ram" }`
    

### 🟢 Update (secured)

- Endpoint: `PUT /user`
    
- Authorization: Basic Auth (username: `ram`, password: `ram`)
    
- Body: `{ "password": "abc" }`
    

### 🔁 Update Result

- After updating password to `abc`, use new credentials to test again.
    
- Using old password → returns `401 Unauthorized`.
    

### 🔴 Security Demo

- Changing only request body doesn't affect authorization.
    
- Only the user with correct credentials can update/delete themselves.
    

---

## 🧠 Important Learnings

|Concept|Explanation|
|---|---|
|**Session Creation Policy**|`STATELESS` disables sessions. Each request must re-authenticate.|
|**Basic Auth + SecurityContext**|Once authenticated, Spring stores username in the context, accessible in controller.|
|**Password Hashing**|Passwords are encoded using `BCryptPasswordEncoder`, never stored as plain text.|
|**CSRF Protection**|Disabled for stateless APIs, since CSRF tokens are form-based (not needed in Postman/API calls).|
|**Role-based Access**|Roles can be used later to restrict admin/user access (e.g., only admin can get all users).|

---

## ✅ Summary

You’ve now configured:

- Stateless authentication with Spring Security.
    
- Password hashing during user registration and update.
    
- Secure update and delete endpoints using `Authentication` object.
    
- Avoided exposing sensitive data (like listing all users).
    
- Used `SecurityContextHolder` to get the current user.
    
