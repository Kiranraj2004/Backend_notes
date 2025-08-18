
# 🚀 Redis in Spring Boot (Part 4: Using Redis Cloud Instead of Local Redis)

---

## **1. Recap of Progress So Far**

- MongoDB:
    
    - Started with local MongoDB.
        
    - Then moved to **MongoDB Atlas** (cloud-managed DB).
        
    - Benefit: Atlas handles backups, upgrades, high availability → no manual management required.
        
- Mail service also set up successfully.
    
- Redis:
    
    - Until now, Redis was running **locally** (`localhost:6379`).
        
    - That’s still **unmanaged** → if it crashes, we must fix it ourselves.
        

---

## **2. Managed vs Unmanaged Database**

- **Unmanaged (local/self-hosted):**
    
    - Example: Running Redis on your own machine (even on a server in your balcony with its IP address 😅).
        
    - Problems:
        
        - Manual upgrades.
            
        - Risk of data corruption.
            
        - Downtime not automatically handled.
            
        - High availability not guaranteed.
            
- **Managed (cloud-hosted):**
    
    - Example: MongoDB Atlas, Redis Cloud.
        
    - Benefits:
        
        - Cloud provider takes care of:
            
            - Backups
                
            - Auto-upgrades
                
            - Scaling
                
            - High availability
                
        - You only focus on using the DB, not managing it.
            
    - Perfect for **production**, where reliability is critical.
        

---

## **3. Setting up Redis Cloud**

- Steps:
    
    1. Go to **Redis Cloud** → Click **Get Started** → Sign up.
        
    2. Confirm email → Create free database.
        
    3. Choose **cloud vendor** (AWS, GCP, Azure).
        
        - Choose **nearest region** for lower latency (e.g., Mumbai if you’re in India).
            
    4. Free tier:
        
        - Provides ~30MB RAM (sufficient for small apps & testing).
            
        - Up to 30 concurrent connections.
            
    5. Name database (e.g., `journal-app`).
        
    6. Copy the **public endpoint** (Redis URI).
        

---

## **4. Connecting with Redis CLI**

- Normally, for local:
    
    ```bash
    redis-cli
    ```
    
- For Redis Cloud:
    
    ```bash
    redis-cli -u redis://<username>:<password>@<host>:<port>
    ```
    
- Example session:
    
    ```bash
    ping
    PONG
    
    set name Vipul
    OK
    
    get name
    "Vipul"
    ```
    
- ✅ Connection successful → Redis Cloud is working.
    

---

## **5. Integrating Redis Cloud in Spring Boot**

- Update `application.yml`:
    
    ```yaml
    spring:
      data:
        redis:
          host: <your-cloud-host>
          port: <your-cloud-port>
          username: <your-username>
          password: <your-password>
    ```
    
- Note:
    
    - Earlier we used **localhost:6379**.
        
    - Now we replace with **Redis Cloud host/port/credentials**.
        

---

## **6. Running the Application with Redis Cloud**

- Restart Spring Boot app.
    
- Debug flow (Weather API with Redis caching):
    
    1. First call:
        
        - Cache miss.
            
        - External weather API is called.
            
        - Response stored in Redis Cloud.
            
    2. Second call (same city):
        
        - Cache hit.
            
        - Response returned from Redis Cloud → No external API call needed.
            

---

## **7. Latency Difference (Local vs Cloud)**

- Local Redis:
    
    - Very fast (same machine, no network latency).
        
- Redis Cloud:
    
    - Adds **network latency** because it’s hosted remotely.
        
    - First call may feel slower (due to extra network hop).
        
    - But caching still saves time from repeated external API calls.
        
- So overall performance is still **much better with Redis cache**.
    

---

## **8. Issues Faced**

- Tried using **Redis URI** directly in Spring Boot → didn’t work.
    
    - Likely version incompatibility between Spring Boot Redis client and Redis Cloud.
        
- Workaround:
    
    - Used **host, port, username, password** separately instead of full URI.
        
    - That worked fine.
        

---

## **9. Outcome**

- Redis Cloud fully integrated.
    
- Now:
    
    - MongoDB → Cloud (Atlas).
        
    - Redis → Cloud (Redis Cloud).
        
    - Mail → Working.
        
- Local dependencies removed ✅ → app is **cloud-ready**.
    

---

## **10. Key Takeaways**

- **Always prefer managed databases in production** for high availability, backups, and less maintenance.
    
- **Redis Cloud free tier** is sufficient for small apps/testing.
    
- **Caching flow**:
    
    - 1st request → Cache miss → External API → Store in Redis.
        
    - 2nd request → Cache hit → Faster response, no API call.
        
- **Latency tradeoff**:
    
    - Local Redis is faster but unmanaged.
        
    - Redis Cloud is slightly slower but reliable & scalable.
        

---

✅ With this, Redis is now **cloud-hosted and production-ready**.  
The next natural topic (if the course continues) would be adding **TTL (expiration) on cached data**, so weather data refreshes automatically after some minutes.

---

Would you like me to also prepare a **side-by-side comparison table** of Local Redis vs Redis Cloud (advantages, disadvantages, when to use which)? That would make these notes even more practical.