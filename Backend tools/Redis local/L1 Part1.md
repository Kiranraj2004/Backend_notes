
## **1. What is Redis?**

- **Definition**:  
    Redis is an **open-source, in-memory data store** used by millions of developers.
    
- **Possible Uses**:
    
    - Cache
        
    - Vector database
        
    - Document database
        
    - Streaming engine
        
    - Message broker
        
- **In-memory**:
    
    - Data is stored in **RAM** (main memory), not on disk (HDD/SSD).
        
    - **Speed**:
        
        - RAM: nanoseconds access time.
            
        - Disk: milliseconds access time.
            

---

## **2. Why Use Redis for Caching?**

### **Without Cache**

- Every request to the API triggers:
    
    - Code execution
        
    - Database calls
        
    - Calculations (e.g., detect IP, location)
        
    - Response creation
        
- If **1 crore (10M)** people hit the same API:
    
    - All go through the full database + calculation process.
        
    - **Problems**:
        
        - Wastes server resources.
            
        - Increases latency (delays).
            
        - Poor customer experience (even milliseconds matter).
            

### **With Cache**

- **Flow**:
    
    1. Client hits API.
        
    2. Server **first checks Redis** for existing response.
        
    3. If found:
        
        - Return from Redis (very fast).
            
    4. If not found:
        
        - Query DB + perform calculations.
            
        - Store result in Redis for future requests.
            
- **Benefits**:
    
    - Reduces database load.
        
    - Low latency responses.
        
    - Saves computation cost.
        

---

## **3. Redis vs In-Memory Cache in JVM**

- You can cache data inside your Spring Boot app using a **HashMap** or **in-memory cache** inside the JVM.
    
- **Limitations of JVM cache**:
    
    - Memory belongs to the single running application instance.
        
    - If you have **many APIs** or **multiple services**, memory fills up quickly.
        
    - No advanced features (just a key-value store).
        
- **Advantages of Redis** over simple JVM cache:
    
    - Shared across applications/instances.
        
    - Rich data structures:
        
        - Lists
            
        - Sets
            
        - Sorted Sets
            
        - Hashes
            
    - Cache eviction strategies.
        
    - **TTL (Time-To-Live)** support:
        
        - Auto-expire cache after given time.
            
        - Example: Cache “plans” API for 4 hours → after expiry, DB is queried again.
            

---

## **4. Redis Expiry Example**

- Store response in Redis with **TTL = 4 hours**.
    
- First request → DB call → store in Redis.
    
- Subsequent requests → served from Redis.
    
- After 4 hours → cache expires → DB call happens again → new value stored in Redis.
    

---

## **5. Integrating Redis in Spring Boot**

### **Step 1: Add Dependency**

In `pom.xml`:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

### **Step 2: Configure Redis in `application.properties`**

```properties
spring.redis.host=localhost
spring.redis.port=6379
spring.redis.password= # optional if no password
```

---

## **6. Installing Redis on Windows (via WSL)**

### **Reason**:

- Redis is **not officially supported** on Windows.
    
- Use **WSL (Windows Subsystem for Linux)** for development.
    

### **WSL Setup**:

1. **Enable WSL**:
    
    - Run in PowerShell (Admin):
        
        ```powershell
        wsl --install
        ```
        
    - Accept prompts → Restart PC.
        
2. **Check WSL**:
    
    ```powershell
    wsl --list --verbose
    ```
    
    - If it shows running distributions → WSL installed successfully.
        
3. **Open WSL**:
    
    - Search “wsl” in Windows search.
        
    - Run Linux terminal.
        

---

## **7. Installing Redis on WSL**

Run these commands **inside WSL terminal**:

```bash
sudo apt update
sudo apt install redis-server
```

---

## **8. Running Redis**

1. Start Redis:
    
    ```bash
    redis-server
    ```
    
2. Open another terminal & connect:
    
    ```bash
    redis-cli
    ```
    
3. Test if running:
    
    ```bash
    ping
    ```
    
    Output:
    
    ```
    PONG
    ```
    
4. Redis default port: **6379**.
    

---

## **9. Redis CLI Basics**

- **Set a key-value**:
    
    ```bash
    set name "Bhoaraja"
    ```
    
- **Get a value**:
    
    ```bash
    get name
    ```
    
- **Set with expiry (seconds)**:
    
    ```bash
    setex plan 14400 "some data"  # 4 hours = 14400 seconds
    ```
    
- **Delete a key**:
    
    ```bash
    del name
    ```
    

---

## **10. Key Takeaways**

- Redis is ideal for **caching** frequently accessed data.
    
- In-memory → extremely fast.
    
- Reduces DB load & computation.
    
- TTL ensures data stays fresh.
    
- Spring Boot integration is simple (dependency + properties).
    
- On Windows, use **WSL** for Redis installation.
    

---

