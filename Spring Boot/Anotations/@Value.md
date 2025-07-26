Here are **detailed notes** from the video transcript, which explains how to securely store and use an **API key** in a Spring Boot application using the `application.yml` file and the `@Value` annotation:

---

## 🔐 Securely Handling API Keys in Spring Boot

### 🧩 1. Problem: **Hardcoded API Keys**

- Initially, the API key was **hardcoded** in the Java class.
    
- Problem:
    
    - If committed to GitHub, this key becomes public and **can be misused**.
        
    - This is a **bad security practice**.
        

---

## ✅ 2. Solution: Store API Key in `application.yml`

Spring Boot allows storing custom configuration properties in:

- `application.properties`
    
- `application.yml`
    

#### ➕ Example in `application.yml`

```yaml
weather:
  api:
    key: your_actual_api_key_here
```

> 🔹 You can name the property anything (e.g., `weather.api.key`, or even a flat key like `weather_api_key`), but it's good practice to keep it structured.

---

## 💡 3. Reading Property Using `@Value` Annotation

Use Spring's `@Value` annotation to read the property from the `.yml` file.

#### ➕ Correct Syntax:

```java
@Value("${weather.api.key}")
private String apiKey;
```

#### 🔥 Common Mistakes to Avoid:

- ❌ `@Value("weather.api.key")` → _missing `${}`_
    
- ❌ `@Value("${weather.api.key}") static String apiKey;` → _cannot inject into static fields_
    

---

## 🔎 4. Why Not Use Static Variables with `@Value`?

- Spring **does not inject values** into static fields because:
    
    - Static variables belong to the **class**, not an instance.
        
    - Spring manages **instances (beans)**, not static contexts.
        
    - All instances share static variables → can lead to conflicts or inconsistencies.
        

#### ✅ Solution:

Make it an **instance variable** (non-static):

```java
@Value("${weather.api.key}")
private String apiKey;
```

---

## 🧪 5. Testing the API Key Injection

- After storing the key in `application.yml` and injecting it with `@Value`, you can:
    
    - Run your application.
        
    - Use Postman to hit your controller endpoint (e.g., `/greetings`).
        
    - Debug and check if the `apiKey` is correctly populated.
        

---

## 🛠 6. Common Troubleshooting Steps

If you're getting `null` in `apiKey`, check:

|✅ Checkpoint|🧾 Details|
|---|---|
|`application.yml` format|Make sure indentation is correct|
|`@Value` syntax|Use `@Value("${...}")` with `${}`|
|Not static|Field must **not be static**|
|Bean is scanned|Ensure the class is a Spring-managed bean (e.g., annotated with `@Service`, `@Component`)|
|Correct Spring profile|If using multiple profiles, ensure the right one is active|

---

## 🧠 7. Summary

|❌ Bad Practice|✅ Best Practice|
|---|---|
|Hardcoding API keys in Java files|Externalize configuration to `application.yml`|
|Using static fields with `@Value`|Use instance fields for proper injection|
|Committing keys to GitHub|Add to `.gitignore` or use `.env` or secrets manager|

---

## 🔄 8. Future Improvement (Teased in Video)

- Instead of keeping the API key in the `.yml` file, you can **store it in a database** and fetch it dynamically.
    
- This improves **security and flexibility**, especially for multi-tenant systems or environment-specific configs.
    

---

## 🔚 Conclusion

Now you know how to:

- Avoid hardcoding API keys.
    
- Store and access properties using `application.yml`.
    
- Use `@Value` correctly.
    
- Avoid common pitfalls like using `static` or incorrect syntax.
    
