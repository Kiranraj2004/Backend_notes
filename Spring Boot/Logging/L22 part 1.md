
## ✅ Introduction to Logging

### 🔍 Why is logging important?

- When your application is **deployed in production**, unexpected issues may occur (e.g., API failures).
    
- These issues **might not appear locally**, making it hard to debug without insights.
    
- **Logs help track and understand the issues** in production.
    
- You can view the logs while hitting APIs to see:
    
    - Whether the request is reaching the server
        
    - What internal processing is happening
        
    - Where exactly the error is occurring
        

---

## 📌 What is Logging?

- **Logging** is an essential feature for **troubleshooting and monitoring** applications.
    
- It allows developers to **track the application's runtime behavior**.
    
- Spring Boot provides support for various logging frameworks.
    

---

## 🔧 Common Logging Frameworks in Spring Boot

|Framework|Description|
|---|---|
|**Logback**|✅ Default logging framework in Spring Boot. Balanced: Simple + Flexible.|
|**Log4j2**|Used for **advanced use-cases** like asynchronous logging. Very flexible.|
|**Java Util Logging (JUL)**|Built-in Java logging, limited features (no log rotation). Not commonly used with Spring Boot.|

> **Note:** Logback is the most widely used framework in real-world Spring Boot projects.


![[Screenshot (211).png]]
---

## ⚙️ Default Logging in Spring Boot

- Spring Boot uses **Logback** by default.
    
- You may not see any logging code initially because Spring Boot **embeds default configuration internally**.
    
- When you run a Spring Boot application, logs are printed **automatically to the console**.
    
- You can observe logs like:
    
    - Timestamps
        
    - Log Level (INFO, ERROR, etc.)
        
    - Process ID
        
    - Thread name
        
    - Package or class
        
    - Log message
        

---

## 🗂️ Log Structure Breakdown

Example Log:

```
2025-07-18 16:30:45.123  INFO 12345 --- [  main] com.example.MyService : Starting MyService
```

|Part|Description|
|---|---|
|`2025-07-18 16:30:45`|Timestamp (Date and Time)|
|`INFO`|Log Level (Severity)|
|`12345`|Process ID|
|`[ main]`|Thread Identification|
|`com.example.MyService`|Package/Class where log originated|
|`Starting MyService`|Actual log message|

---

## 🧱 Logging Levels (Severity Categories)

|Level|Purpose|
|---|---|
|`TRACE`|Most detailed, used for fine-grained info, like method-by-method flow|
|`DEBUG`|Used during development for debugging details|
|`INFO`|General information, usually used for app startup or milestones|
|`WARN`|Something unexpected, might cause issues later|
|`ERROR`|A problem occurred that needs attention|

> ✅ You can **set logging levels per class or package** in `application.properties` or `logback.xml`.

---

## 🔄 system.out.println vs Logging

|Feature|`System.out.println`|Logging (e.g., SLF4J)|
|---|---|---|
|Basic Output|✅ Yes|✅ Yes|
|Timestamp, Severity|❌ No|✅ Yes|
|Log to File|❌ No|✅ Yes|
|Fine-grained control|❌ No|✅ Yes|
|Production Friendly|❌ Not recommended|✅ Recommended|

---

## 💡 Logger Creation with SLF4J (Simple Logging Facade for Java)

Spring Boot uses **SLF4J** as a facade and binds it to Logback by default.

### ✅ Injecting Logger in a Class:

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JournalEntryService {
    private static final Logger logger = LoggerFactory.getLogger(JournalEntryService.class);
    
    public void saveEntry() {
        logger.info("Saving journal entry...");
        logger.error("An error occurred while saving!");
    }
}
```

> 🔁 Each class should have **its own logger instance** using its class name.

---

## 🛠️ Customizing Logging Behavior

### Option 1: Without Custom Configuration

- Spring Boot uses default settings.
    
- Logs are printed to the console.
    

### Option 2: Custom Configuration with `logback.xml`

- File location:  
    `src/main/resources/logback.xml`
    
- Example:
    

```xml
<configuration>
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>

    <root level="INFO">
        <appender-ref ref="STDOUT" />
    </root>
</configuration>
```

> You can configure:
> 
> - Output format
>     
> - Log file rotation
>     
> - Logging level per class/package
>     
> - Append to file or console
>     

---

## 📝 Summary

- **Logging** is essential for debugging and monitoring in production.
    
- **Spring Boot** uses **Logback** by default.
    
- You can choose from **Logback**, **Log4j2**, or **Java Util Logging**, but Logback is most common.
    
- **Logging levels** (TRACE, DEBUG, INFO, WARN, ERROR) help classify log severity.
    
- Use **SLF4J (via LoggerFactory)** to log messages instead of `System.out.println`.
    
- Customize logging with **`logback.xml`** if needed.
    

---

Let me know if you’d like sample code to write logs into a file or how to use `@Slf4j` from Lombok to simplify logger creation.