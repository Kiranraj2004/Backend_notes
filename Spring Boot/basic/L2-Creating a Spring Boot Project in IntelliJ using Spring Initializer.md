
## 🌱 **Video Topic:** Creating a Spring Boot Project in IntelliJ using Spring Initializer

---

## 🧠 **Key Concepts Covered:**

### 1. **Spring Boot & IntelliJ Overview**

- **Spring Boot** simplifies Spring application development by reducing configuration effort.
    
- **IntelliJ IDEA** is chosen for this tutorial because:
    
    - Frequent updates by JetBrains team.
        
    - Smooth, modern user interface.
        

---

## 🔧 **Using Spring Initializer**

### 🔍 What is Spring Initializer?

- A web tool to generate a pre-configured Spring Boot project.
    
- Website: [https://start.spring.io](https://start.spring.io/)
    
- Allows:
    
    - Selection of language (Java, Kotlin, etc.)
        
    - Build tool (Maven or Gradle)
        
    - Spring Boot version
        
    - Java version
        
    - Dependencies like Spring Web, JPA, etc.
        

---

## ⚙️ **Spring Initializer Configuration Example**

|Option|Value|
|---|---|
|**Language**|Java|
|**Build Tool**|Maven|
|**Spring Boot**|2.7.x (Stable release)|
|**Group**|`eDigest`|
|**Artifact**|`my-first-project`|
|**Description**|First project for Spring Boot|
|**Package Name**|`eDigest.myfirstproject`|
|**Java Version**|8|
|**Packaging**|JAR|
|**Dependency Added**|Spring Web|

> 📁 Output: A `.zip` file is downloaded with the project setup.

---

## 🧳 **Extracting and Opening the Project**

1. **Extract the zip file**.
    
2. **Open IntelliJ** → Drag and drop the extracted folder into the IDE.
    
3. If you see errors:
    
    - Go to **File → Project Structure** and make sure the **Java version** is set to **8 or higher** (matches initializer config).
        

---

## 🚀 **Understanding the Project Structure**

```
src/
└── main/
    ├── java/
    │   └── eDigest/myfirstproject/
    │       └── MyFirstProjectApplication.java
    └── resources/
        ├── application.properties
        └── static/
```

---

## 🏃‍♂️ **Running the Application**

- Right-click `MyFirstProjectApplication.java` → **Run**.
    
- Console Output includes:
    
    - Spring Boot version
        
    - Logs
        
    - Embedded Apache Tomcat running on **port 8080**
        
    - PID of the running process
        
    - Logs show: `Tomcat started on port(s): 8080`
        

---

## 🌐 **Testing the Default Server**

- Visit: `http://localhost:8080`
    
    - You’ll see a **"Whitelabel Error Page"** or "Not Found" → This is expected (no endpoints yet).
        
- Visit: `http://localhost:8081`
    
    - **"Site can't be reached"** → Port 8081 is not used.
        

---

## ✍️ **Creating a Simple REST Controller**

### ✅ Step-by-step:

1. Create a new class `MyController.java`.
    
2. Annotate it with `@RestController`.
    
3. Add a method `sayHello()` with `@GetMapping`.
    

---

## 💻 **Full Code: `MyController.java`**

```java
package eDigest.myfirstproject;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {

    @GetMapping("/abc")
    public String sayHello() {
        return "Hello";
    }
}
```

---

## ▶️ **Re-run the Application**

### ✅ Output when visiting:

- URL: `http://localhost:8080/abc`
    
- **Output:** `Hello`
    

---

## 📘 **Annotations Used Explained**

|Annotation|Purpose|
|---|---|
|`@RestController`|Indicates this class handles web requests|
|`@GetMapping`|Maps HTTP GET requests to specific handler methods|

---

## 🌍 **What is Context Path?**

- A prefix added to all URLs in your application.
    
- Default = empty (`""`)
    
- Example: if set to `/myproject`, your API would be: `http://localhost:8080/myproject/abc`
    

> You can set context path in `application.properties`:

```properties
server.servlet.context-path=/myproject
```

---

## 📦 **Packaging Type**

|Type|Description|
|---|---|
|JAR|Runs as standalone using `java -jar` command|
|WAR|Deployed on external web servers like Tomcat|

In this video, **JAR** was selected.

---

## ✅ **Final Output Summary**

```text
Project Starts on Port: 8080
Accessing http://localhost:8080/abc
Response: Hello
```

---

## 🧾 **Summary of the Video**

- Created a Spring Boot project using Spring Initializer.
    
- Imported into IntelliJ.
    
- Learned about dependencies, build tools, packaging.
    
- Created and tested a simple REST API using Spring Web.
    
### ✅ JAR vs WAR in Java & Spring Boot

Both **JAR** and **WAR** are packaging formats in Java used to bundle compiled code, resources, and configuration files for deployment — but they serve **different purposes**.

---

### 🔹 JAR (Java ARchive)

|Feature|Details|
|---|---|
|📦 **What it is**|A package of Java classes, resources, and metadata in a compressed format.|
|📌 **Used for**|Running standalone Java or Spring Boot applications.|
|🚀 **Execution**|Can be run with `java -jar file.jar`.|
|✅ **Spring Boot**|By default, Spring Boot apps are packaged as **JARs**.|
|🌱 **Self-contained**|Contains embedded server (e.g., Tomcat), so you don’t need to install a server separately.|

#### 📁 JAR Contents (Spring Boot Example)

```
myapp.jar
├── BOOT-INF/
│   ├── classes/       ← compiled .class files
│   ├── lib/           ← all dependencies
├── META-INF/
└── org/
```

---

### 🔸 WAR (Web Application Archive)

|Feature|Details|
|---|---|
|📦 **What it is**|A package specifically for **web applications**.|
|📌 **Used for**|Deploying web apps into external servlet containers like **Apache Tomcat**, **JBoss**, **WebLogic**.|
|⚙️ **Deployment**|You place the `.war` file in the `webapps/` folder of the web server.|
|⚠️ **Spring Boot**|Needs to be adjusted to support WAR (not default).|

#### 📁 WAR Contents (Servlet App Example)

```
myapp.war
├── WEB-INF/
│   ├── classes/
│   ├── lib/
│   └── web.xml
```

---

### 🔄 JAR vs WAR Summary

| Feature             | JAR                  | WAR                         |
| ------------------- | -------------------- | --------------------------- |
| Format              | Java Archive         | Web Archive                 |
| App Type            | Standalone apps      | Web apps (Servlet/JSP)      |
| Embedded Server     | Yes (e.g., Tomcat)   | No (needs external server)  |
| Spring Boot Default | ✅ Yes                | ❌ No, needs extra config    |
| Deployment Style    | Run with `java -jar` | Deploy to servlet container |

---

### 📌 In Spring Boot:

- Use **JAR** when:
    
    - You want to create a **standalone** app.
        
    - You want to deploy to a **cloud** platform like AWS, Azure, or Heroku.
        
- Use **WAR** when:
    
    - You need to deploy to an **existing servlet container** like Tomcat/Jetty.
        
