Got it 👍  
This section of the transcript is where the **Redis Service becomes complete**:

- We add **generics properly**.
    
- We implement **get + set with TTL (Time-To-Live)**.
    
- We fix **serialization issue** using **ObjectMapper**.
    
- Finally, we run tests with **Postman** and verify data is cached/retrieved correctly.
    

Here are the **detailed structured notes**:

---

# 🚀 Redis in Spring Boot (Part 4: Generic Redis Service with TTL, JSON Serialization, and Cache Validation)

---

## **1. Problem: Making Redis Service Generic**

- Earlier:
    
    - `get()` method was returning `WeatherResponse` (specific).
        
    - Not reusable for other objects (e.g., `User`, `Cart`).
        
- Goal:
    
    - Make methods **generic** using `<T>`.
        
    - This allows storing and retrieving **any type of POJO**.
        

---

### **a) Generic `get` Method**

```java
public <T> T get(String key, Class<T> clazz) {
    try {
        Object value = redisTemplate.opsForValue().get(key);
        if (value == null) return null;

        // Convert JSON string → Object of type T
        return objectMapper.readValue(value.toString(), clazz);

    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
}
```

✅ Now `get("weather_mumbai", WeatherResponse.class)` works,  
and also `get("user_101", User.class)` will work.

---

### **b) Generic `set` Method (with TTL)**

- Redis supports **expiry times** for keys → Time-To-Live (TTL).
    
- We implement `set` with TTL support.
    

```java
public <T> void set(String key, T value, long ttlInSeconds) {
    try {
        // Convert Object → JSON string
        String jsonValue = objectMapper.writeValueAsString(value);

        redisTemplate.opsForValue().set(key, jsonValue, ttlInSeconds, TimeUnit.SECONDS);

    } catch (Exception e) {
        e.printStackTrace();
    }
}
```

✅ Any object can be stored in Redis as JSON, with expiry.

---

## **2. TTL (Time-To-Live)**

- TTL ensures cached values **expire automatically** after given time.
    
- Example:
    
    - Store `WeatherResponse` with TTL = 300 seconds (5 min).
        
    - After 5 minutes, Redis will **remove the key** automatically.
        
- Special case:
    
    - If `ttl = -1` → Key never expires (permanent storage in Redis).
        

---

## **3. Usage in Weather API**

### **a) Fetch with Cache**

```java
public WeatherResponse getWeather(String city) {
    String key = "weather_" + city;

    // 1. Try cache
    WeatherResponse cached = redisService.get(key, WeatherResponse.class);
    if (cached != null) {
        return cached; // ✅ Return cached result
    }

    // 2. If not cached, call external API
    WeatherResponse response = weatherApiClient.getWeather(city);

    // 3. Save response in Redis for 5 minutes
    if (response != null) {
        redisService.set(key, response, 300L); // 300 sec = 5 min
    }

    return response;
}
```

---

## **4. Fixing Serialization Issue**

- Problem faced:
    
    - Spring Data Redis with `StringRedisSerializer` → expects **String values**.
        
    - Directly saving `WeatherResponse` (POJO) → caused error:
        
        ```
        WeatherResponse cannot be cast into class String
        ```
        
- Solution:
    
    - Use `ObjectMapper`:
        
        - `writeValueAsString(object)` → converts POJO → JSON String (safe to store).
            
        - `readValue(json, clazz)` → converts JSON String → POJO (safe to retrieve).
            

---

## **5. Verification with Postman**

### **First Request**

- Cache is empty.
    
- API call hits **external weather API**.
    
- Response:
    
    ```json
    {
      "current": {
        "temperature": 33,
        "feels_like": 33
      }
    }
    ```
    
- Redis stores this JSON:
    
    ```bash
    GET weather_mumbai
    ```
    
    Returns:
    
    ```json
    {"current":{"temperature":33,"feels_like":33}}
    ```
    

---

### **Second Request**

- Cache is **already populated**.
    
- Redis returns value immediately.
    
- External API **is not called again** → saves time & money.
    
- User sees the **same weather response**, but doesn’t know it came from cache.
    

---

## **6. Additional Notes**

- **Key Naming Strategy**:
    
    - Use descriptive keys to avoid conflicts.
        
    - Example:
        
        - `"weather_mumbai"` (city-based caching).
            
        - `"user_1234"` (user data caching).
            
- **Deleting Keys**:
    
    ```java
    redisTemplate.delete("weather_mumbai");
    ```
    
- **Redis Config**:
    
    - Currently using **local Redis**.
        
    - For production:
        
        - Use **cloud Redis instance** (like AWS ElastiCache, RedisLabs, Azure Redis).
            
        - Configure host, port, username, password in `application.properties`.
            

---

## **7. Redis Concepts Mentioned**

- **Basic CRUD**: set, get, delete.
    
- **TTL**: auto-expiry for cache entries.
    
- **-1 TTL**: never expires (permanent data).
    
- **Serialization**:
    
    - Keys + values stored as Strings (via `StringRedisSerializer`).
        
    - POJOs stored as JSON (via `ObjectMapper`).
        
- **Advanced features (not covered in detail here)**:
    
    - Sorted Sets (ZSETs).
        
    - Key pattern queries (prefix-based fetch).
        
    - Pub/Sub messaging.
        
    - Streams, Lists, Hashes, etc.
        

---

## **8. Key Takeaways**

1. Redis service is now **generic** using `<T>`.
    
2. Values stored as **JSON strings** (solves serialization issues).
    
3. Cache supports **TTL (expiry in seconds)**.
    
4. External API results are cached → improves performance & reduces cost.
    
5. Users see the same result, whether from **cache** or **API**.
    