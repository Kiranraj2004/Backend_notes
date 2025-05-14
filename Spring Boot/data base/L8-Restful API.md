### 🔍 What Are RESTful APIs? (In-Depth Explanation)

**RESTful API** stands for **Representational State Transfer API**, which is a standard way of designing web services so that systems can communicate over the **HTTP protocol** in a scalable, stateless, and predictable manner.

---

## 🧩 1. What is REST?

**REST** is an architectural style (not a protocol) introduced by **Roy Fielding** in his PhD dissertation in 2000.

It defines **6 key constraints** for building scalable web services:

|Constraint|Description|
|---|---|
|**1. Client-Server**|Separation of concerns: client handles UI, server handles data/storage.|
|**2. Stateless**|Every request contains all the info needed; server does **not** store client session state.|
|**3. Cacheable**|Responses must define whether they can be cached. Improves performance.|
|**4. Uniform Interface**|A consistent, well-defined way to interact with resources (explained below).|
|**5. Layered System**|Clients should not know if they’re connected to the final server or an intermediary.|
|**6. Code on Demand**|(Optional) Servers can send executable code to clients (like JavaScript).|

---

## 🔁 2. REST vs RESTful API

- **REST** is a set of **principles**.
    
- A **RESTful API** is an actual implementation of those principles using **HTTP**.
    

---

## 🔗 3. RESTful API Core Concepts

### 📌 Resources

A **resource** is any data or object you want to expose via the API, e.g., `User`, `Post`, `JournalEntry`.

Resources are accessed using **URLs (Uniform Resource Locators)**.

Example:

```
GET /users/1
```

Means: Get user with ID 1.

---

### 📌 HTTP Verbs (Methods)

|HTTP Method|Purpose|Example URL|Description|
|---|---|---|---|
|GET|Read resource|`GET /users`|Get all users|
|POST|Create resource|`POST /users`|Create a new user|
|PUT|Update resource|`PUT /users/1`|Update user with ID 1|
|DELETE|Delete resource|`DELETE /users/1`|Delete user with ID 1|

---

### 📌 Status Codes

RESTful APIs use **HTTP status codes** to inform clients of the result:

|Code|Meaning|Example|
|---|---|---|
|200|OK (successful GET)|`GET /users` returns user list|
|201|Created|New resource successfully created|
|204|No Content|Success, but nothing to return|
|400|Bad Request|Client sent invalid data|
|401|Unauthorized|Client must authenticate|
|404|Not Found|Resource does not exist|
|500|Internal Server Error|Something broke on the server|

---

## 📦 4. JSON as Standard Format

REST APIs typically use **JSON** for data exchange because it's lightweight and widely supported.

Example request/response:

```json
// POST /users
{
  "name": "Alice",
  "email": "alice@example.com"
}
```

```json
// Response
{
  "id": 1,
  "name": "Alice",
  "email": "alice@example.com"
}
```

---

## 📐 5. Endpoint Naming Conventions

- Use **nouns** for endpoints, not verbs.
    
- Use **plural names**.
    
- Use **nested paths** for relations.
    

|Action|Endpoint|
|---|---|
|Get all posts|`GET /posts`|
|Get post 1|`GET /posts/1`|
|User's posts|`GET /users/1/posts`|

---

## 🛡️ 6. Statelessness Explained

Each request from a client must contain **all information needed to process it**. Server doesn't store session.

Good: ✅

```http
GET /users/1
Headers:
  Authorization: Bearer <token>
```

Bad: ❌

```http
GET /users/1
(No auth info sent; server remembers user — not RESTful)
```

---

## 🔄 7. Idempotency and Safety

|Method|Safe?|Idempotent?|Description|
|---|---|---|---|
|GET|✅|✅|Does not change server state|
|POST|❌|❌|Creates new resources|
|PUT|❌|✅|Updates resource completely|
|DELETE|❌|✅|Deletes same thing multiple times = same result|

---

## 📚 Example: Simple RESTful API in Spring Boot

```java
@RestController
@RequestMapping("/api/journals")
public class JournalController {

    // GET all journal entries
    @GetMapping
    public List<Journal> getAllJournals() {
        return journalService.getAll();
    }

    // GET single journal entry
    @GetMapping("/{id}")
    public Journal getById(@PathVariable Long id) {
        return journalService.getById(id);
    }

    // POST new journal
    @PostMapping
    public Journal create(@RequestBody Journal journal) {
        return journalService.save(journal);
    }

    // PUT update journal
    @PutMapping("/{id}")
    public Journal update(@PathVariable Long id, @RequestBody Journal journal) {
        return journalService.update(id, journal);
    }

    // DELETE journal
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        journalService.delete(id);
    }
}
```

---



---

## 📦 Project Structure and Renaming

### 1. Rename Main Class

**Before:**  
`JournalAppApplication.java`

**After (using Refactor):**  
`JournalApplication.java`

This ensures consistency and updates all references to the class.

```java
package com.edigest.journalapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JournalApplication {
    public static void main(String[] args) {
        SpringApplication.run(JournalApplication.class, args);
    }
}
```

---

## 🌐 What is REST API?

### Definition:

- **REST** = Representational State Transfer
    
- **API** = Application Programming Interface
    

REST API = **HTTP Verb + URL**

Examples of **HTTP Verbs**:

|Verb|Purpose|
|---|---|
|GET|Read|
|POST|Create|
|PUT|Update|
|DELETE|Delete|

---

## 🧑‍💻 Creating the First REST API Endpoint

### Create a Package

```plaintext
com.edigest.journalapp.controller
```

### Create a Class

**File:** `JournalEntryController.java`

```java
package com.edigest.journalapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JournalEntryController {

    @GetMapping("/health-check")
    public String healthCheck() {
        return "Application is running!";
    }
}
```

### Key Concepts:

- `@RestController`: Special component that handles HTTP requests and returns data as JSON or plain text.
    
- `@GetMapping("/health-check")`: Maps HTTP GET request to this method.
    

---

## ▶️ Running the Application

1. Run the `JournalApplication.java` main class.
    
2. It starts the server on **localhost:8080** by default.
    

---

## 🧪 Testing the REST API

### Option 1: Browser

Visit:

```
http://localhost:8080/health-check
```

**Expected Output:**

```
Application is running!
```

### Option 2: Postman (Recommended)

- Set method to `GET`
    
- Enter URL: `http://localhost:8080/health-check`
    
- Click **Send**
    

**Response:**

```text
Application is running!
```

---

## 📌 Summary

|Task|Status|
|---|---|
|Spring Boot Setup|✅ Done|
|Added REST Controller|✅ Done|
|Created `GET` endpoint|✅ Done|
|Tested in Postman|✅ Done|


## 📘 Key Concepts Covered

### 🔹 @RestController

- Combines `@Controller` and `@ResponseBody`.
    
- It tells Spring that this class will handle RESTful web requests and responses will be directly written to HTTP response body.
    

---

## 🏗️ Project Structure Overview

```
src
└── main
    └── java
        └── com.example.demo
            ├── DemoApplication.java
            ├── controller
            │   └── JournalEntryController.java
            └── entity
                └── JournalEntry.java
```

---

## 📦 Step 1: Create Entity Class (`JournalEntry.java`)

```java
package com.example.demo.entity;

public class JournalEntry {
    private long id;
    private String title;
    private String content;

    // Constructors (optional)
    public JournalEntry() {}

    public JournalEntry(long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    // Getters & Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
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

## 🧠 Step 2: Create Controller (`JournalEntryController.java`)

```java
package com.example.demo.controller;

import com.example.demo.entity.JournalEntry;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/journal") 
compiler will check the base url like ("/journal") then after it will go for inside
public class JournalEntryController {

    private Map<Long, JournalEntry> journalEntries = new HashMap<>();

    // GET all entries
    @GetMapping
    public List<JournalEntry> getAll() {
        return new ArrayList<>(journalEntries.values());
    }

    // GET entry by ID
    @GetMapping("/{id}")
    public JournalEntry getById(@PathVariable("id") long id) {
        return journalEntries.get(id);
    }

    // POST new entry
    @PostMapping
    public boolean createEntry(@RequestBody JournalEntry entry) {
        journalEntries.put(entry.getId(), entry);
        return true;
    }

    // PUT update entry by ID
    @PutMapping("/{id}")
    public JournalEntry updateEntry(@PathVariable("id") long id,
                                    @RequestBody JournalEntry updatedEntry) {
        updatedEntry.setId(id); // enforce id consistency
        journalEntries.put(id, updatedEntry);
        return updatedEntry;
    }

    // DELETE entry by ID
    @DeleteMapping("/{id}")
    public JournalEntry deleteEntry(@PathVariable("id") long id) {
        return journalEntries.remove(id);
    }
}
```

---

## 🚀 Step 3: Main Application (`DemoApplication.java`)

```java
package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
```

---

## 📮 Step 4: Testing in Postman

### 🔸 POST (Create Entry)

- URL: `http://localhost:8080/journal`
    
- Method: `POST`
    
- Body → `raw` → `JSON`
    

```json
{
  "id": 1,
  "title": "Morning",
  "content": "It was a good morning"
}
```

### 🔸 GET (All Entries)

- URL: `http://localhost:8080/journal`
    
- Method: `GET`
    

### 🔸 GET (Entry by ID)

- URL: `http://localhost:8080/journal/1`
    
- Method: `GET`
    

### 🔸 PUT (Update Entry)

- URL: `http://localhost:8080/journal/1`
    
- Method: `PUT`
    

```json
{
  "title": "Morning Update",
  "content": "Updated content"
}
```

### 🔸 DELETE (Remove Entry)

- URL: `http://localhost:8080/journal/1`
    
- Method: `DELETE`
    

---

## 📤 Outputs from API

### ✅ POST

```
true
```

### ✅ GET (All)

```json
[
  {
    "id": 1,
    "title": "Morning",
    "content": "It was a good morning"
  }
]
```

### ✅ GET (By ID)

```json
{
  "id": 1,
  "title": "Morning",
  "content": "It was a good morning"
}
```

### ✅ PUT (Updated Entry)

```json
{
  "id": 1,
  "title": "Morning Update",
  "content": "Updated content"
}
```

### ✅ DELETE

```json
{
  "id": 1,
  "title": "Morning Update",
  "content": "Updated content"
}
```

---

## 🔁 Summary of Annotations

|Annotation|Purpose|
|---|---|
|`@RestController`|Indicates this class handles REST endpoints|
|`@RequestMapping`|Maps base path like `/journal`|
|`@GetMapping`|Maps HTTP GET requests|
|`@PostMapping`|Maps HTTP POST requests|
|`@PutMapping`|Maps HTTP PUT requests|
|`@DeleteMapping`|Maps HTTP DELETE requests|
|`@RequestBody`|Accepts JSON body and binds to Java object|
|`@PathVariable`|Binds URL segment to method parameter|

---

Would you like me to generate a downloadable Spring Boot project zip for this?