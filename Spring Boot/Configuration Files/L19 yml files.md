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

``` java
use this
mvnw.cmd clean
and then 
mvnw.cmd package
 this will help to create jar file 


java -jar target/journalApp-0.0.1-SNAPSHOT.jar
use this to runv

```
    
    ``` properties
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






The command:

```bash
./mvnw clean package
```


is used in **Maven-based** projects (especially in Spring Boot) and it performs the following:

---

### 🔹 `./mvnw`

- This runs the **Maven Wrapper** (`mvnw` is short for Maven Wrapper).
    
- It ensures that the correct version of Maven is used **even if it's not installed** globally on your system.
    
- The `./` at the start means "run the file from the current directory".
    

---

### 🔹 `clean`

- Deletes the `target/` directory.
    
- This removes **previous build files**, ensuring a **fresh build** without leftovers.
    
- Prevents issues caused by old compiled classes, resources, etc.
    

---

### 🔹 `package`

- This tells Maven to **compile the code**, **run the tests**, and **create a JAR (or WAR)** file in the `target/` folder.
    
- It uses your `pom.xml` file to understand how to build your project.
    

---

### ✅ Summary

So overall:

```bash
./mvnw clean package
use this 
mvnw.cmd clean
and then 
mvnw.cmd package
 this will help to create jar file 


java -jar target/journalApp-0.0.1-SNAPSHOT.jar
use this to run

```

➡️ Runs Maven using the wrapper  
➡️ Deletes the old `target/` directory  
➡️ Compiles your project  
➡️ Runs tests  
➡️ Packages the output into a JAR/WAR file (`target/your-app.jar`)

---

### 🛠 Example Use in Spring Boot

If you're building a Spring Boot app, after this command, you can usually run the app like this:

```bash
java -jar target/your-app-name.jar
```



---

### ✅ Solution: Set `JAVA_HOME`

You need to point `JAVA_HOME` to the folder where Java (JDK) is installed on your system.

---

### 🪟 For Windows (Step-by-step)

1. **Find your JDK installation path**  
    It usually looks like this:
    
    ```
    C:\Program Files\Java\jdk-17
    ```
    
    (or `jdk-21`, `jdk-11`, etc. — depending on your installed version)
    
2. **Set JAVA_HOME Environment Variable**
    
    - Press `Windows Key` → type `Environment Variables` → click **"Edit the system environment variables"**
        
    - In the **System Properties** window, click **"Environment Variables…"**
        
    - Under **System variables**, click **"New…"**:
        
        - **Variable name:** `JAVA_HOME`
            
        - **Variable value:** your JDK path (e.g., `C:\Program Files\Java\jdk-17`)
            
    - Click **OK**
        
3. **Add Java to PATH**
    
    - In the same **Environment Variables** window, under **System variables**, find `Path`, then click **Edit**
        
    - Click **New**, and add:
        
        ```
        %JAVA_HOME%\bin
        ```
        
    - Click **OK** on all dialogs
        
4. **Restart Terminal**
    
    - Close and reopen your PowerShell or Command Prompt
        
    - Then test:
        
        ```bash
        echo $env:JAVA_HOME
        java -version
        ```
        

---

### ✅ Now run again:

```bash
./mvnw clean package
```

