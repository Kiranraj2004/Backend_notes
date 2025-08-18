
## **1. Basic Redis CLI Usage (Recap)**

- **Set a key**:
    
    ```bash
    set name "Akshit"
    ```
    
    Output: `"OK"` → Key saved.
    
- **Get a key**:
    
    ```bash
    get name
    ```
    
    Output: `"Akshit"`.
    
- Exiting CLI and restarting Redis server:
    
    - Open WSL.
        
    - Start Redis server.
        
    - Connect again using:
        
        ```bash
        redis-cli
        ```
        

---

## **2. Spring Boot Configuration for Redis**

- **Dependency**:  
    Already added in `pom.xml`:
    
    ```xml
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-redis</artifactId>
    </dependency>
    ```
    
- **`application-dev.properties`**:
    
    ``properties
    spring.redis.host=localhost
    spring.redis.port=6379
    ```
    
    (Password property optional if no password.)
    
- **Spring Boot Auto-Configuration**:
    
    - As soon as you run the app, Spring Boot auto-configures a **RedisTemplate** bean using the given host & port.
        

---

## **3. Using RedisTemplate in Service**

- Example in a test service:
    
    ```java
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    
    public void testRedis() {
        // Set a value
        redisTemplate.opsForValue().set("email", "will@gmail.com");
    
        // Get the value
        String value = redisTemplate.opsForValue().get("email");
        System.out.println(value); // "will@gmail.com"
    }
    ```
    
- **Explanation**:
    
    - `opsForValue()` returns an interface for **simple key-value operations**.
        
    - Supports `set`, `get`, `increment`, `decrement`, etc.
        
- Result:
    
    - Shows connection is working and Spring Boot can set/get data from Redis.
        

---

## **4. Data Persistence Test**

- After stopping and restarting the app, the key was still retrievable.
    
- Redis was running locally at port **6379**.
    

---

## **5. Problem: Data Not Visible Between Spring Boot & Redis CLI**

- Setting key via Spring Boot:
    
    - Accessible inside app, but **not visible** in `redis-cli`.
        
- Setting key via `redis-cli`:
    
    - Not visible inside Spring Boot.
        

**Reason**:

- **Serialization mismatch**:
    
    - Spring Boot's default RedisTemplate uses **Java Serialization** for keys/values.
        
    - Redis CLI stores data as **plain strings**.
        
    - Serialized keys from Spring Boot look like binary data to `redis-cli`, so CLI can’t read them as expected.
        
- Solution:
    
    - Both need to use **the same serializer/deserializer** (String serializer for human-readable).
        

---

## **6. Fix: Configure Custom RedisTemplate Bean**

- Create a **RedisConfig** class:
    
    ```java
    @Configuration
    public class RedisConfig {
    
        @Bean
        public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
            RedisTemplate<String, Object> template = new RedisTemplate<>();
            template.setConnectionFactory(connectionFactory);
    
            // Use String serialization for keys & values
            StringRedisSerializer stringSerializer = new StringRedisSerializer();
            template.setKeySerializer(stringSerializer);
            template.setValueSerializer(stringSerializer);
            template.setHashKeySerializer(stringSerializer);
            template.setHashValueSerializer(stringSerializer);
    
            template.afterPropertiesSet();
            return template;
        }
    }
    ```
    
- **Key Points**:
    
    - `@Configuration`: Marks class as Spring config.
        
    - `@Bean`: Creates and registers the bean in Spring context.
        
    - `RedisConnectionFactory`: Auto-injected by Spring Boot; manages connection to Redis.
        
    - `StringRedisSerializer`: Ensures keys & values are stored as human-readable strings (compatible with CLI).
        
    - `afterPropertiesSet()`: Initializes the template after setting properties.
        

---

## **7. Updated Service Usage After Config**

- Now both Spring Boot & CLI share the same serialization method:
    
    ```java
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    public void test() {
        redisTemplate.opsForValue().set("salary", "10000");
        String value = (String) redisTemplate.opsForValue().get("salary");
        System.out.println(value); // "10000"
    }
    ```
    
- **Now**:
    
    - `redis-cli get salary` → `"10000"`.
        
    - Keys set via CLI → retrievable in Spring Boot.
        

---

## **8. Understanding Serialization/Deserialization**

- **Serialization**: Converting data into a format for storage or transfer.
    
- **Deserialization**: Converting stored/transferred data back into usable form.
    
- Default Spring Boot RedisTemplate uses:
    
    - **JDK Serialization** (`JdkSerializationRedisSerializer`).
        
- CLI expects plain strings, so mismatch occurs unless explicitly configured.
    

---

## **9. Final Flow**

1. **Start Redis Server** (via WSL):
    
    ```bash
    redis-server
    ```
    
2. **Start Spring Boot App**.
    
3. Spring Boot connects to Redis via `RedisConnectionFactory`.
    
4. Custom `RedisTemplate` ensures same string serialization between app & CLI.
    
5. Now data is visible & accessible in both.
    

---

## **Key Takeaways**

- Always configure **serializers** if you want to share data between Spring Boot and Redis CLI.
    
- `RedisTemplate` is the primary way to interact with Redis from Spring Boot.
    
- Default Java serialization makes keys unreadable to CLI.
    
- Using `StringRedisSerializer` makes debugging and manual inspection easier.
    