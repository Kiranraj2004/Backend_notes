
# 📘 Detailed Notes on JWT in Spring Boot (based on transcript)

---

## 1. Dependencies Required

We need **three main dependencies** for JWT:

1. **jjwt-api** → provides the interfaces/classes for JWT handling.
    
2. **jjwt-impl** → the runtime implementation of JWT logic.
    
3. **jjwt-jackson** → integrates Jackson for JSON parsing in JWT (encoding/decoding claims, etc.).
    

👉 Together, these allow us to generate, sign, and validate JWT tokens.

**Maven example:**

```xml
<dependency>
  <groupId>io.jsonwebtoken</groupId>
  <artifactId>jjwt-api</artifactId>
  <version>0.11.5</version>
</dependency>
<dependency>
  <groupId>io.jsonwebtoken</groupId>
  <artifactId>jjwt-impl</artifactId>
  <version>0.11.5</version>
  <scope>runtime</scope>
</dependency>
<dependency>
  <groupId>io.jsonwebtoken</groupId>
  <artifactId>jjwt-jackson</artifactId>
  <version>0.11.5</version>
  <scope>runtime</scope>
</dependency>
```

---

## 2. Utility Class (`JwtUtil`)

- Create a new **package** (e.g., `utils`).
    
- Inside it, create a `JwtUtil` class.
    
- Mark it as a `@Component` so it can be injected.
    
- This class will contain:
    
    - Method to generate tokens.
        
    - Method to extract username/claims.
        
    - Method to validate tokens.
        

**Example (simplified):**

```java
@Component
public class JwtUtil {

    private String secret = "mySecretKey"; // should come from application.properties

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)              // custom claims (extra info)
                .setSubject(subject)            // main identity (username/email)
                .setIssuedAt(new Date(System.currentTimeMillis())) // created time
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour
                .signWith(SignatureAlgorithm.HS256, secret) // sign with key
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        final Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        return resolver.apply(claims);
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !extractExpiration(token).before(new Date()));
    }
}
```

---

## 3. Flow of Authentication with JWT

### Step 1 — **Signup**

- User provides `username` + `password`.
    
- Save it to DB with **password encoded** (`BCryptPasswordEncoder`).
    
- This is same as your existing user registration.
    

### Step 2 — **Login**

- User submits `username` + `password`.
    
- Use `AuthenticationManager` to verify credentials:
    
    ```java
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(username, password)
    );
    ```
    
- Internally:
    
    - Calls your **UserDetailsService** (`loadUserByUsername`).
        
    - Matches password using **PasswordEncoder**.
        
    - If mismatch → exception.
        
    - If correct → authenticated.
        
- On success → generate a **JWT token** using `JwtUtil`.
    
- Return token in the response.
    

### Step 3 — **Using Token**

- For future requests, instead of username/password, user sends JWT token.
    
- Usually in **Authorization header**:
    
    ```
    Authorization: Bearer <jwt-token>
    ```
    
- The token is validated before allowing access.
    

---

## 4. AuthenticationManager Flow (Important)

- In login:
    
    ```java
    try {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(username, password));
    } catch (Exception e) {
        throw new RuntimeException("Invalid username/password");
    }
    ```
    
- Internally:
    
    - Calls `UserDetailsService.loadUserByUsername(username)`.
        
    - Compares DB-stored password (encoded) with provided password (raw).
        
    - Uses `PasswordEncoder.matches(raw, encoded)`.
        
    - If valid → authenticated object returned.
        
    - Else → exception thrown.
        

---

## 5. Example Controller

```java
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody User user) {
        // encode password and save user
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return ResponseEntity.ok("User registered");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(), request.getPassword()));

            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
            String token = jwtUtil.generateToken(userDetails.getUsername());

            return ResponseEntity.ok(Map.of("jwt", token));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }
}
```

---

## 6. JWT Token Breakdown

A JWT consists of three parts:

1. **Header** → algorithm + token type.  
    Example: `{ "alg": "HS256", "typ": "JWT" }`
    
2. **Payload** → data (claims).  
    Example: `{ "sub": "john", "roles": ["ADMIN"], "exp": 1699870832 }`
    
3. **Signature** → generated using secret key.
    

👉 Together:

```
header.payload.signature
```

---

## 7. Key Points from Transcript

- **First login**: user sends username + password.
    
- **Server validates** using `UserDetailsService` + `PasswordEncoder`.
    
- **If valid**: generate JWT with expiry (e.g., 1 hour).
    
- **Subsequent requests**: send JWT, not credentials.
    
- **JWT Util**: handles token creation, parsing, validation.
    
- **Subject** in token → unique identifier (usually username).
    
- **Claims** → extra data (e.g., roles, email).
    
- **Expiration** → ensures token validity is limited.
    

---

✅ So the **whole story** is:

- User registers → stored in DB (password encoded).
    
- User logs in → validated → JWT issued.
    
- Client stores JWT → sends with each request.
    
- Server validates JWT (signature + expiry) → if valid, allows access.
    

---

Would you like me to also make a **visual diagram** (Signup → Login → Token Issued → Request with JWT → Validation → Response) so that the flow is crystal clear for quick revision?