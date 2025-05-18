


## 🔧 Goal:
Integrate MongoDB into a Spring Boot application using Spring Data MongoDB and follow best practices (Controller → Service → Repository).

---

## 📦 1. Add MongoDB Dependency

Add the following dependency in your `pom.xml` if using Maven:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-mongodb</artifactId>
</dependency>
```

---

## ⚙️ 2. Configure `application.properties`

Located in: `src/main/resources/application.properties`

```properties
# MongoDB Configuration
spring.data.mongodb.host=localhost
spring.data.mongodb.port=27017
spring.data.mongodb.database=journaldb

# Uncomment below if authentication is needed
# spring.data.mongodb.username=yourUsername
# spring.data.mongodb.password=yourPassword
```

✅ With Spring Boot's auto-configuration, this is enough to establish a connection.

---

## 🏗️ 3. Project Structure

```
src
└── main
    └── java
        └── com.example.journal
            ├── controller
            │   └── JournalEntryControllerV2.java
            ├── service
            │   └── JournalEntryService.java
            ├── repository
            │   └── JournalEntryRepository.java
            └── model
                └── JournalEntry.java
```

---

## 📄 4. Model Class (`JournalEntry.java`)

Located in: `model/JournalEntry.java`

```java
package com.example.journal.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "journal_entries")
public class JournalEntry {

    @Id
    private String id;
    private String title;
    private String content;

    public JournalEntry() {
    }

    public JournalEntry(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
```

---

## 🗂️ 5. Repository Interface

Located in: `repository/JournalEntryRepository.java`

```java
package com.example.journal.repository;

import com.example.journal.model.JournalEntry;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface JournalEntryRepository extends MongoRepository<JournalEntry, String> {
}
```

- `MongoRepository<EntityType, IDType>` handles basic CRUD: findAll, save, deleteById, etc.

---

## 💼 6. Service Class

Located in: `service/JournalEntryService.java`

```java
package com.example.journal.service;

import com.example.journal.model.JournalEntry;
import com.example.journal.repository.JournalEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    public JournalEntry createEntry(JournalEntry entry) {
        return journalEntryRepository.save(entry);
    }

    public List<JournalEntry> getAllEntries() {
        return journalEntryRepository.findAll();
    }

    public void deleteEntry(String id) {
        journalEntryRepository.deleteById(id);
    }
}
```

---

## 🌐 7. Controller Class

Located in: `controller/JournalEntryControllerV2.java`

```java
package com.example.journal.controller;

import com.example.journal.model.JournalEntry;
import com.example.journal.service.JournalEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/journal")
public class JournalEntryControllerV2 {

    @Autowired
    private JournalEntryService journalEntryService;

    @PostMapping
    public JournalEntry createEntry(@RequestBody JournalEntry entry) {
        return journalEntryService.createEntry(entry);
    }

    @GetMapping
    public List<JournalEntry> getAllEntries() {
        return journalEntryService.getAllEntries();
    }

    @DeleteMapping("/{id}")
    public void deleteEntry(@PathVariable String id) {
        journalEntryService.deleteEntry(id);
    }
}
```

---

## ▶️ 8. Running & Testing the API

You can use **Postman** or **curl** to test the endpoints:

- **POST** `/api/v2/journal`

```json
{
    "title": "Day 1",
    "content": "Started learning MongoDB with Spring Boot"
}
```

- **GET** `/api/v2/journal`

```json
[
  {
    "id": "662f0fd1f5162d6e3fbbc112",
    "title": "Day 1",
    "content": "Started learning MongoDB with Spring Boot"
  }
]
```

- **DELETE** `/api/v2/journal/{id}`

---

## ✅ Output Example in Console (On Successful Save):

```
Connected to MongoDB at localhost:27017
Saving document to collection 'journal_entries'
```

---

## 🧠 Summary of Best Practices

- Use `@Document` for MongoDB entities.
- Use `MongoRepository` for CRUD operations.
- Follow layered architecture:
  - Controller → Service → Repository
- Store connection config in `application.properties`.
- Use `@Autowired` for dependency injection (or constructor-based injection for better testing).

---

## 🧩 1. **Adding MongoDB Dependency**

To use MongoDB with Spring Boot, you need to add the **Spring Data MongoDB** dependency in your `pom.xml` (for Maven projects):

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-mongodb</artifactId>
</dependency>
```

---

## ⚙️ 2. **MongoDB Configuration in `application.properties`**

Inside `src/main/resources/application.properties`, add the following to connect to a MongoDB server:

```properties
spring.data.mongodb.host=localhost
spring.data.mongodb.port=27017
spring.data.mongodb.database=journal_db
```

> ✅ Note:
> 
> - `localhost` and `27017` are defaults for local MongoDB.
>     
> - `journal_db` will be created automatically when the first record is inserted if it doesn’t exist yet.
>     

Optional Authentication (commented):

```properties
# spring.data.mongodb.username=your-username
# spring.data.mongodb.password=your-password
```

---

## 📦 3. **Package Structure (Best Practice)**

Use layered architecture:

```
com.example.journal
├── controller
├── entity
├── repository
├── service
```

---

## 📁 4. **Entity Class (POJO + MongoDB Mapping)**

```java
package com.example.journal.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "journal_entries")
public class JournalEntry {

    @Id
    private String id;
    private String title;
    private String content;

    // Getters and Setters
}
```

> 📌 `@Document` maps the class to a MongoDB collection.  
> 📌 `@Id` marks the primary key.

---

## 📁 5. **Repository Interface**

```java
package com.example.journal.repository;

import com.example.journal.entity.JournalEntry;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface JournalEntryRepository extends MongoRepository<JournalEntry, String> {
}
```

> 🔄 `MongoRepository<T, ID>` provides built-in CRUD operations.

---

## 💼 6. **Service Layer**

```java
package com.example.journal.service;

import com.example.journal.entity.JournalEntry;
import com.example.journal.repository.JournalEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository repository;

    public JournalEntry createEntry(JournalEntry entry) {
        return repository.save(entry);
    }

    public List<JournalEntry> getAllEntries() {
        return repository.findAll();
    }

    // Additional CRUD methods can be added
}
```

> 🧠 **Why use Service Layer?**  
> Keeps business logic separate from controller. Encourages maintainability and scalability.

---

## 🌐 7. **Controller Layer**

```java
package com.example.journal.controller;

import com.example.journal.entity.JournalEntry;
import com.example.journal.service.JournalEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/journals")
public class JournalEntryController {

    @Autowired
    private JournalEntryService service;

    @PostMapping
    public JournalEntry createEntry(@RequestBody JournalEntry entry) {
        return service.createEntry(entry);
    }

    @GetMapping
    public List<JournalEntry> getAllEntries() {
        return service.getAllEntries();
    }
}
```

> 🎯 This exposes REST endpoints:
> 
> - POST `/api/journals` to create a journal entry.
>     
> - GET `/api/journals` to retrieve all entries.
>     

---

## 🔁 8. **Spring Boot Auto-Configuration**

Because you added the MongoDB dependency and provided configuration in `application.properties`, Spring Boot:

- Auto-configures a MongoDB connection.
    
- Scans for `@Document`, `@Repository`, `@Service`, and `@RestController` annotations.
    
- Injects dependencies via `@Autowired`.
    

> 💡 No manual DB connection code needed.

---

## 🧪 9. **Testing with Postman**

**POST** `http://localhost:8080/api/journals`

```json
{
  "title": "First Entry",
  "content": "This is my first journal entry."
}
```

**Response:**

```json
{
  "id": "64f8ae0b2b8a43210e2bd6d4",
  "title": "First Entry",
  "content": "This is my first journal entry."
}
```

**GET** `http://localhost:8080/api/journals`

Returns a list of entries stored in MongoDB.

---

## 🧠 Key Concepts Recap

|Concept|Description|
|---|---|
|`@Document`|Maps Java class to MongoDB collection|
|`@Id`|Marks primary key|
|`MongoRepository<T, ID>`|Generic interface for CRUD|
|`@Service`|Business logic class|
|`@RestController`|HTTP endpoints|
|Dependency Injection|`@Autowired` automatically injects beans|
|Auto-Configuration|Spring Boot configures DB automatically based on classpath and `application.properties`|

In a Spring Boot application, organizing code into **Controller**, **Service**, and **Repository** layers (and putting them into separate packages) follows a well-established software design pattern called **Layered Architecture**. Each layer has a clear **responsibility**, promoting **clean code**, **separation of concerns**, and **maintainability**.

# why each layer/package is needed:

---

### ✅ 1. **Repository Layer** (DAO Layer)

- **Purpose**: Interacts directly with the database (MongoDB, MySQL, etc.)
    
- **Why needed**:
    
    - Provides **data access abstraction**. You don’t write raw queries or DB connection logic yourself.
        
    - Spring Data automatically implements common database operations like **save, findById, findAll, deleteById**, etc.
        
- **Interface**:  
    You define a simple interface (e.g., `JournalEntryRepository`) and extend `MongoRepository` or `JpaRepository`, and Spring Boot will generate its implementation at runtime.
    

📦 `com.example.project.repository`

```java
public interface JournalEntryRepository extends MongoRepository<JournalEntry, String> {
    // Custom queries if needed
}
```

---

### ✅ 2. **Service Layer**

- **Purpose**: Contains **business logic** of the application.
    
- **Why needed**:
    
    - Keeps the controller clean and focused only on handling requests/responses.
        
    - Makes your code more **testable** and **reusable**.
        
    - You can easily **modify logic** here without touching the controller or DB layer.
        

📦 `com.example.project.service`

```java
@Service
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    public JournalEntry createEntry(JournalEntry entry) {
        return journalEntryRepository.save(entry);
    }

    public List<JournalEntry> getAllEntries() {
        return journalEntryRepository.findAll();
    }
}
```

---

### ✅ 3. **Controller Layer**

- **Purpose**: Handles **HTTP requests** (GET, POST, PUT, DELETE) and returns responses.
    
- **Why needed**:
    
    - Acts as an **entry point** for the client (e.g., frontend or Postman).
        
    - Keeps API endpoints **separate** from business and database logic.
        
    - Converts service return values into JSON or other web responses.
        

📦 `com.example.project.controller`

```java
@RestController
@RequestMapping("/api/journals")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @PostMapping
    public JournalEntry createEntry(@RequestBody JournalEntry entry) {
        return journalEntryService.createEntry(entry);
    }

    @GetMapping
    public List<JournalEntry> getAllEntries() {
        return journalEntryService.getAllEntries();
    }
}
```

---

### 🔄 How They Work Together

```
Client (e.g., Postman or Browser)
        ↓
Controller → receives the request and delegates to →
Service → processes logic and delegates to →
Repository → interacts with MongoDB
```

---

### 🎯 Benefits of Using All Three Layers

|Benefit|Explanation|
|---|---|
|🔄 **Separation of Concerns**|Each layer has a single responsibility.|
|🧪 **Easier to test**|You can test service logic separately from the controller and DB.|
|♻️ **Code reusability**|Service methods can be reused across multiple controllers.|
|🔧 **Easier maintenance**|You can change database logic without affecting the controller.|
|📦 **Scalability**|Easier to manage as your application grows in features and size.|


## ✅ Summary: CRUD Journal App with MongoDB in Spring Boot

This tutorial covers the full CRUD (Create, Read, Update, Delete) functionality in a journaling app using Spring Boot with MongoDB. It also demonstrates debugging with breakpoints and improving code logic using `Optional` and `LocalDateTime`.

---

## 🏗️ 1. Project Setup (Dependencies)

Make sure your `pom.xml` has:

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-mongodb</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
</dependencies>
```

---

## 🗃️ 2. JournalEntry Model

```java
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import java.time.LocalDateTime;

public class JournalEntry {

    @Id
    private ObjectId id;
    private String title;
    private String content;
    

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}
```

---

## 🧾 3. Repository Interface

```java
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface JournalEntryRepository extends MongoRepository<JournalEntry, ObjectId> {
}
```

---

## 💼 4. Service Class

```java
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository repository;

    public JournalEntry createEntry(JournalEntry entry) {
        entry.setCreatedDate(LocalDateTime.now());
        return repository.save(entry);
    }

    public List<JournalEntry> getAllEntries() {
        return repository.findAll();
    }

    public JournalEntry getEntryById(ObjectId id) {
        return repository.findById(id).orElse(null);
    }

    public void deleteById(ObjectId id) {
        repository.deleteById(id);
    }

    public JournalEntry updateEntry(ObjectId id, JournalEntry newEntry) {
        JournalEntry oldEntry = getEntryById(id);
        if (oldEntry != null) {
            if (newEntry.getTitle() != null && !newEntry.getTitle().isEmpty()) {
                oldEntry.setTitle(newEntry.getTitle());
            }
            if (newEntry.getContent() != null && !newEntry.getContent().isEmpty()) {
                oldEntry.setContent(newEntry.getContent());
            }
            return repository.save(oldEntry);
        }
        return null;
    }
}
```

---

## 🌐 5. Controller

```java
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService service;

    @PostMapping
    public JournalEntry createEntry(@RequestBody JournalEntry entry) {
        return service.createEntry(entry);
    }

    @GetMapping
    public List<JournalEntry> getAllEntries() {
        return service.getAllEntries();
    }

    @GetMapping("/{id}")
    public JournalEntry getEntryById(@PathVariable ObjectId id) {
        return service.getEntryById(id);
    }

    @DeleteMapping("/{id}")
    public boolean deleteById(@PathVariable ObjectId id) {
        service.deleteById(id);
        return true;
    }

    @PutMapping("/{id}")
    public JournalEntry updateEntry(@PathVariable ObjectId id, @RequestBody JournalEntry entry) {
        return service.updateEntry(id, entry);
    }
}
```

---

## ⚙️ 6. `application.properties`

```properties
spring.data.mongodb.uri=mongodb://localhost:27017/journaldb
```

---

## 📬 7. Postman Test Cases

### ➕ POST `/journal`

**Body:**

```json
{
  "title": "Morning",
  "content": "Went to gym"
}
```

**Output:** Returns full saved object with `_id` and `createdDate`.

---

### 📥 GET `/journal`

Returns:

```json
[
  {
    "id": "6637b17e019b5e431ce93c15",
    "title": "Morning",
    "content": "Went to gym",
    "createdDate": "2025-05-06T10:00:00"
  }
]
```

---

### 🔍 GET `/journal/{id}`

Returns a specific entry.

---

### 🗑️ DELETE `/journal/{id}`

Returns:

```json
true
```

---

### 🔁 PUT `/journal/{id}`

**Body:**

```json
{
  "title": "Evening"
}
```

**Output:** Updated entry with only the title changed.

---

## 🐞 8. Debugging with IntelliJ

- Set a breakpoint in the `updateEntry` method.
    
- Run using "Debug" not "Run".
    
- Use F8 / step over to go line by line.
    
- Use variables pane to inspect data.
    

Example bug fixed:

```java
// Original (wrong):
if (newEntry.getTitle() == null && newEntry.getTitle().isEmpty())

// Fixed:
if (newEntry.getTitle() != null && !newEntry.getTitle().isEmpty())
```

---

## ✅ Final Result

You now have a fully working Spring Boot journaling app with:

- ObjectId for MongoDB ID
    
- Auto-timestamp with LocalDateTime
    
- Full CRUD API (Create, Read All, Read by ID, Update, Delete)
    
- Postman tested
    
- Debugging walkthrough
    