
# 📒 Notes: JWT Basics & Spring Boot Integration

## 1. 🔑 What is JWT?

- **Full Form** → JSON Web Token
    
- **Definition** → A compact, URL-safe token format used to securely transmit information between two parties (e.g., client ↔ server).
    
- **Nature** → Just a **string**, composed of **3 parts separated by dots (`.`)**:
    
    ```
    <header>.<payload>.<signature>
    ```
    

---

## 2. 📌 Why not Basic Authentication?

- **Basic Auth Process**
    
    - Sends `username:password` in every request (base64 encoded).
        
    - Example:
        
        ```
        Authorization: Basic dmlwdWw6dmlwdWw=   // vipul:vipul encoded
        ```
        
- **Problems**
    
    - Only encoded, not encrypted → vulnerable if not using HTTPS.
        
    - Credentials exposed in every request.
        
    - No expiration mechanism.
        
    - Can’t easily attach extra metadata.
        

✅ **JWT solves these problems**:

- No need to send username/password repeatedly.
    
- Token can have **expiry**.
    
- You can attach **custom claims** (extra info like roles, metadata).
    

---

## 3. 📦 Structure of JWT

JWT = **Header + Payload + Signature**

1. **Header**
    
    - Contains metadata about the token.
        
    - Typically has:
        
        - `alg` → Algorithm used (e.g., HS256 = HMAC SHA-256).
            
        - `typ` → Type of token (`JWT`).
            
    - Example (before encoding):
        
        ```json
        {
          "alg": "HS256",
          "typ": "JWT"
        }
        ```
        
2. **Payload**
    
    - Contains **claims** → information about the user/entity.
        
    - Example claims:
        
        ```json
        {
          "sub": "user123",
          "name": "Vipul",
          "role": "admin",
          "exp": 1733700000
        }
        ```
        
    - Can store both **standard claims** (like `sub`, `exp`, `iat`) and **custom claims**.
        
3. **Signature**
    
    - Ensures the token is **not tampered with**.
        
    - Created using:
        
        ```
        HMACSHA256(
          base64UrlEncode(header) + "." + base64UrlEncode(payload),
          secretKey
        )
        ```
        
    - Needs a **secret key** known only to the server.
        

---

## 4. ⚙️ How JWT is Encoded

- Uses **Base64 URL Encoding** (not plain Base64).
    
- Difference:
    
    - Replaces unsafe characters (`+`, `/`, `=`) with URL-safe alternatives.
        
    - Avoids issues when tokens are sent in URLs or headers.
        

---

## 5. 🔄 Flow: JWT Authentication vs Basic Auth

### Basic Auth:

1. Client → sends `username:password` in every request.
    
2. Server → validates credentials each time.
    

### JWT Auth:

1. Client logs in (username/password once).
    
2. Server verifies credentials → generates JWT.
    
3. Client stores JWT (e.g., in local storage or cookie).
    
4. For every request:
    
    - Client sends:
        
        ```
        Authorization: Bearer <JWT>
        ```
        
    - Server verifies signature + expiry.
        
    - If valid → processes request.
        

---

## 6. 📚 Spring Boot Setup (Dependencies)

To implement JWT in Spring Boot (example shown in video):

In `pom.xml`, add:

```xml
<!-- JWT library -->
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
    <artifactId>jjwt-jackson</artifactId> <!-- or jjwt-gson -->
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>
```

Then reload Maven.  
(These dependencies provide JWT creation & validation utilities.)

---

## 7. 🛡 Key Points Recap

- JWT = Header + Payload + Signature.
    
- Header → token type + algorithm.
    
- Payload → actual user data (claims).
    
- Signature → secures the token (uses secret + algorithm).
    
- Advantage over Basic Auth → no password repetition, tokens expire, can carry extra info, safer when used with HTTPS.
    
- Uses **Base64 URL-safe encoding**.
    
- In Spring Boot → use `jjwt` dependencies.
    

---

👉 Next steps (probably in the next part of the video):

- Writing code to **generate JWT** after login.
    
- Writing code to **validate JWT** for secured endpoints.
    
- Configuring Spring Security to use JWT instead of Basic Auth.
    

-

## ❓ Question

_“If the data in a JWT is encoded with some algorithm, then if a hacker knows the algorithm, can he also decode the data?”_


## 1. JWT **Encoding ≠ Encryption**

- The **header** and **payload** of a JWT are just **Base64URL-encoded**.  
    👉 This means **anyone** can decode them using a simple Base64 decoder.
    
- Example: If you paste a JWT into [jwt.io](https://jwt.io/), you can see the payload in plain JSON.
    

✅ So yes — if a hacker has the token, they can **see the data** inside the payload.

---

## 2. What protects JWT?

The **signature**.

- Signature = hash of (`header + payload + secretKey`) using the specified algorithm (e.g., HMAC SHA-256).
    
- Even if someone changes one character in the payload (say role: "user" → "admin"), the signature will no longer match.
    
- The server **verifies** the signature with the secret key. If it doesn’t match → request is rejected.
    

👉 So the **algorithm is not secret**.  
What’s secret is the **key** used for signing. Without the secret, the hacker cannot forge a valid signature.

---

## 3. Then is JWT secure?

- **Confidentiality** ❌: JWT does **not hide data** (not encrypted). Payload is visible.
    
- **Integrity** ✅: JWT ensures the payload hasn’t been tampered with.
    

That’s why:

- You should **never put sensitive data** (like passwords, bank details, SSNs) inside a JWT payload.
    
- Instead, put only **non-sensitive claims** (e.g., userId, role, username).
    

---

## 4. How to make JWT data confidential?

If you really need to hide the data:

- Use **JWE (JSON Web Encryption)** → encrypted JWTs.
    
- Or encrypt the sensitive data before putting it into the payload.
    
- But for most cases (auth/authorization), **JWS (signed JWT)** is enough.
    

---

✅ **Summary**

- Yes, the data part (header + payload) can be decoded by anyone — algorithm doesn’t matter.
    
- The **signature** prevents tampering.
    
- The **secret key** must remain secure on the server.
    
- Don’t put sensitive data in JWT payload.
    
