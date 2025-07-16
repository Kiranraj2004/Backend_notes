Here are **detailed notes** based on the transcript you provided. These notes cover the **ways to configure properties in Spring Boot**, **property file formats**, **priority/order of resolution**, and **real-world application/deployment relevance**.

---

## 📝 Spring Boot Configuration: `application.properties`, `application.yml`, and Command-Line Args

---

### ✅ **1. How Spring Boot Reads Configuration Files**

- **Default Configuration File:**  
    Spring Boot automatically searches for configuration files like `application.properties` or `application.yml` from the classpath.
    
- **Classpath Location:**
    
    - Typical path: `src/main/resources/`
        
    - Contents of `resources/` get added to the classpath by default.
        
    - No additional configuration needed if the file is in the classpath.
        
- **If Placed Outside Classpath:**
    
    - You must explicitly specify the path using command-line or other mechanisms.
        

---

### 📁 **2. Classpath in Java**

- **Definition:**  
    Classpath is a list of locations (JARs, directories, config files) where Java looks for classes and resources during runtime.
    
- **Includes:**
    
    - `.class` files
        
    - `.jar` files
        
    - `.properties`, `.yml` files (e.g., `application.properties`)
        

---

### 🧾 **3. Configuration File Types in Spring Boot**

#### A. `application.properties`

- **Syntax:** `key=value`
    
- **Example:**
    
    ```properties
    server.port=8080
    spring.data.mongodb.uri=mongodb://localhost:27017
    ```
    

#### B. `application.yml` (YAML Format)

- **Extension:** `.yml` or `.yaml`
    
- **Full Form:** YAML Ain’t Markup Language (recursive acronym)
    
- **Syntax:**
    
    - Indentation-based (spaces only, no tabs)
        
    - Uses `:` instead of `=`
        
- **Example:**
    
    ```yaml
    server:
      port: 8081
    
    spring:
      data:
        mongodb:
          uri: mongodb://localhost:27017
          database: journaldb
          auto-index-creation: true
    ```
    

---


### ⚖️ **4. Order of Property Resolution in Spring Boot**

If the **same property** is defined in multiple places, Spring Boot resolves them in the following **order of precedence** (highest to lowest):

|Priority|Source|Example|
|---|---|---|
|1️⃣|**Command-line arguments**|`--server.port=9090`|
|2️⃣|`application.properties`|Located in `src/main/resources/`|
|3️⃣|`application.yml`|Also in `src/main/resources/`|

- **So, Command-line > Properties > YAML**
    
- If both `application.properties` and `application.yml` define `server.port`, then `application.properties` wins unless overridden via command-line.
    

---

### 💻 **5. Running Spring Boot with Config Files**

#### A. From IntelliJ

- Add arguments in **Run Configurations**:
    
    - Go to `Edit Configurations > Program Arguments`
        
    - Example: `--server.port=9092`
        

#### B. From Terminal

- Package the app using Maven:
    
    ```bash
    ./mvnw clean package
    ```
    
- Run the jar file with arguments:
    
    ```bash
    java -jar target/journal-app.jar --server.port=9090
    ```
    

---

### 🧪 **6. Test Context Path**

- Change context path in YAML:
    
    ```yaml
    server:
      servlet:
        context-path: /journal
    ```
    
- Or in `.properties`:
    
    ```properties
    server.servlet.context-path=/journal
    ```
    
- This affects the base URL:  
    Default: `http://localhost:8080/`  
    With context: `http://localhost:8080/journal/`
    

---

### ❗ Important Notes

- Do not keep both `application.properties` and `application.yml` with overlapping keys unless necessary.
    
- Spring Boot throws a **duplicate configuration warning**, but both files can technically exist.
    
- **Best Practice:** Use **only one** format consistently unless a use-case requires both.
    

---

### 💼 Real-World Relevance

- In **enterprise environments**, these configurations are critical for:
    
    - Different environments (dev, staging, prod)
        
    - Tools like **Jenkins**, **Docker**, **Kubernetes**, etc.
        
    - Externalization of configs using **environment variables**, **command-line**, or **cloud config servers**
        

---

## 🧠 Summary

|Concept|Description|
|---|---|
|`application.properties`|Key-value format, traditional configuration|
|`application.yml`|YAML format, structured and human-readable|
|Command-line args|Highest priority; override any file-based config|
|Classpath|Spring Boot auto-loads from `src/main/resources`|
|Real-world use|Understanding this is crucial for deployment, Jenkins, Docker, etc.|
|Recommendation|Use `application.yml` for better readability, but be consistent|
