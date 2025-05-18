
### ✅ **Recap of the Application So Far**

- **HealthCheckController**: For basic API testing.
    
- **JournalEntryController**: For managing journal entries (CRUD operations).
    
- **MongoDB Collection**: Contains documents with fields like `title`, `content`, and `date`.
    
- **JournalEntryRepository**: Extends `MongoRepository`.
    
- **JournalEntryService**: Calls repository methods: `findAll`, `findById`, `deleteById`, `save`.
    

---

### ✅ **Goal of This Video**

Implement a basic **user functionality**:

- Each user can have **many journal entries**.
    
- A journal entry should be linked to a specific user.
    
- Add authentication logic (simple structure for now).
    
- Connect user documents to their respective journal entries using MongoDB references (`@DBRef`).
    

---

### ✅ **User Entity Creation**

```java
@Document(collection = "users")
public class User {

    @Id
    private ObjectId id;

    @Indexed(unique = true)
    @NonNull
    private String username;

    @NonNull
    private String password;

    @DBRef
    private List<JournalEntry> journalEntries = new ArrayList<>();

    // Getters and Setters
}
```

#### Notes:

- `@Document`: Maps this class to a MongoDB collection.
    
- `@Id`: Primary key.
    
- `@Indexed(unique = true)`: Ensures unique usernames and fast search.
    
- `@NonNull`: Prevents null entries.
    
- `@DBRef`: Creates a reference to another MongoDB collection (i.e., journal entries).
    
- Journal entries are stored as references (only ObjectId), not embedded documents.
    

---

### ✅ **Why Use @DBRef?**

- Mimics a **foreign key** in relational databases.
    
- Helps establish a **parent-child relationship** (User → Journal Entries).
    
- Allows you to fetch linked journal entries from the `user` document.
    

---

### ✅ **MongoDB Document Shape (Sample)**

```json
{
  "_id": ObjectId("..."),
  "username": "ram",
  "password": "hashedPassword",
  "journalEntries": [
    ObjectId("entry1"),
    ObjectId("entry2")
  ]
}
```

---

### ✅ **Auto-Index Creation Configuration**

By default, Spring Boot does not create indexes declared in the entity.

To enable this, add the following to `application.properties`:

```properties
spring.data.mongodb.auto-index-creation=true
```

This ensures that the unique index on `username` is created automatically in MongoDB.

---

### ✅ **User Repository**

```java
public interface UserRepository extends MongoRepository<User, ObjectId> {
    Optional<User> findByUsername(String username);
}
```

--

This line in your `UserRepository`:

```java
Optional<User> findByUsername(String username);
```

is **not extra** — it's actually **necessary** for specific functionality in your application.

---

### 🔍 **Why it's needed**

You want to **update a user based on their username**, **not their ObjectId**. But MongoRepository by default only gives you methods like:

```java
Optional<User> findById(ObjectId id);
List<User> findAll();
void deleteById(ObjectId id);
// ... and other common methods
```

So, to **find a user by `username`**, which is a custom field, you **must define a custom query method** like:

```java
Optional<User> findByUsername(String username);
```

---

### ✅ What it does

This tells Spring Data MongoDB:

> "Generate a query that finds a user where `username = ?`"

Spring will implement this method automatically — no need to write the query manually.

---

### 🛠️ How it's used in your code

This is used in your **UserService** or **UserController** when doing something like:

```java
User userInDb = userRepository.findByUsername(username).orElse(null);
```

Then you can:

- Check if the user exists.
    
- Update their details like password.
    
- Save them back to the database.
    

---

### ⚠️ Why you can't rely only on `findById`

In your flow:

- You aren't using the MongoDB `_id` to update the user.
    
- You get the `username` in the request body or path.
    
- So you need a way to **look up the user by `username`**, not `_id`.
    

---

### ✨ Bonus: Why use `Optional<User>` instead of `User`

Using `Optional<User>` is a modern Java best practice. It helps avoid `NullPointerException` by forcing you to handle the case where the user might not exist.

---

### 🔁 Summary

|Method|Why it's needed|
|---|---|
|`Optional<User> findByUsername(...)`|To find a user by their unique `username` field|
|`Optional` return type|To safely handle cases where no user is found|

### ✅ **User Service**

```java
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(ObjectId id) {
        return userRepository.findById(id);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUserById(ObjectId id) {
        userRepository.deleteById(id);
    }
}
```

---

### ✅ **User Controller**

```java
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return new ResponseEntity<>(userService.saveUser(user), HttpStatus.CREATED);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable ObjectId id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable ObjectId id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }
}
```

---

### ✅ **Important Points**

- **Username & Password** are the only required fields. No email is required (supports anonymity).
    
- **Password storage** should be hashed in production (e.g., using `BCryptPasswordEncoder`).
    
- **Linked Journal Entries** allow users to have private entries accessible only by them.
    
- This setup forms the **foundation for authentication**, where users will later log in and see only their data.




## ✅ 2. **User Entity Class (Updated)**

```java
@Document(collection = "users")
public class User {

    @Id
    private ObjectId id;

    @Indexed(unique = true)
    @NonNull
    private String username;

    @NonNull
    private String password;

    @DBRef
    private List<JournalEntry> journalEntries = new ArrayList<>();

    // Getters and Setters
}
```

> 🧠 `@DBRef`: Represents a reference to another collection (`JournalEntry`).  
> 🧠 `@Indexed(unique = true)`: Ensures username is unique.

---

## ✅ 3. **Enable Index Creation in `application.properties`**

```properties
spring.data.mongodb.auto-index-creation=true
```

---

## ✅ 4. **UserRepository**

```java
public interface UserRepository extends MongoRepository<User, ObjectId> {
    Optional<User> findByUsername(String username);
}
```

---



This line in your `UserRepository`:

```java
Optional<User> findByUsername(String username);
```

is **not extra** — it's actually **necessary** for specific functionality in your application.

---

### 🔍 **Why it's needed**

You want to **update a user based on their username**, **not their ObjectId**. But MongoRepository by default only gives you methods like:

```java
Optional<User> findById(ObjectId id);
List<User> findAll();
void deleteById(ObjectId id);
// ... and other common methods
```

So, to **find a user by `username`**, which is a custom field, you **must define a custom query method** like:

```java
Optional<User> findByUsername(String username);
```

---

### ✅ What it does

This tells Spring Data MongoDB:

> "Generate a query that finds a user where `username = ?`"

Spring will implement this method automatically — no need to write the query manually.

---

### 🛠️ How it's used in your code

This is used in your **UserService** or **UserController** when doing something like:

```java
User userInDb = userRepository.findByUsername(username).orElse(null);
```

Then you can:
- Check if the user exists.
- Update their details like password.
- Save them back to the database.

---

### ⚠️ Why you can't rely only on `findById`

In your flow:
- You aren't using the MongoDB `_id` to update the user.
- You get the `username` in the request body or path.
- So you need a way to **look up the user by `username`**, not `_id`.

---

### ✨ Bonus: Why use `Optional<User>` instead of `User`

Using `Optional<User>` is a modern Java best practice. It helps avoid `NullPointerException` by forcing you to handle the case where the user might not exist.

---

### 🔁 Summary

| Method                              | Why it's needed                                      |
|-------------------------------------|-------------------------------------------------------|
| `Optional<User> findByUsername(...)`| To find a user by their unique `username` field       |
| `Optional` return type              | To safely handle cases where no user is found         |

## ✅ 5. **UserService**

```java
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void deleteUser(ObjectId id) {
        userRepository.deleteById(id);
    }
}
```

---

## ✅ 6. **UserController Implementation**

```java
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // GET all users
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // POST - create new user
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        try {
            User savedUser = userService.saveUser(user);
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT); // e.g., duplicate username
        }
    }

    // PUT - update password by username
    @PutMapping("/{username}")
    public ResponseEntity<Void> updateUserByUsername(@PathVariable String username, @RequestBody User updatedUser) {
        Optional<User> existingUser = userService.findByUsername(username);

        if (existingUser.isPresent()) {
            User userInDb = existingUser.get();
            userInDb.setUsername(updatedUser.getUsername()); // Optional: Can omit if username should not change
            userInDb.setPassword(updatedUser.getPassword());

            userService.saveUser(userInDb);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
```

---

## ✅ 7. **Testing in Postman**

### ➤ Create a new user (POST)

- **URL:** `POST http://localhost:8080/users`
    
- **Body (JSON):**
    

```json
{
  "username": "ram",
  "password": "1234"
}
```

- **Response:** `201 Created`
    

---

### ➤ Get all users (GET)

- **URL:** `GET http://localhost:8080/users`
    
- **Response:**
    

```json
[
  {
    "id": "ObjectId",
    "username": "ram",
    "password": "1234",
    "journalEntries": []
  }
]
```

---

### ➤ Update password or username (PUT)

- **URL:** `PUT http://localhost:8080/users/ram`
    
- **Body (JSON):**
    

```json
{
  "username": "ram123",
  "password": "newpass"
}
```

- **Response:** `204 No Content`
    

> This will find the user by the **old username ("ram")** and update it to `"ram123"` with the new password.

---

## ✅ 8. **Handling Duplicate Username Error**

If you try to `POST` the same username twice:

```json
{
  "username": "ram",
  "password": "anotherpass"
}
```

Response:

```
409 Conflict
```

> 💡 This happens because of `@Indexed(unique = true)` and is handled with try-catch.

---

## ✅ 9. **Future Enhancements (Hinted for Next Video)**

- Add **password encryption** (e.g., using `BCryptPasswordEncoder`).
    
- Implement **Spring Security** for login/authentication.
    
- Modify journal entry APIs to **filter entries per user**.
    

---

## ✅ 10. **Summary of Work Done in This Video**

|Operation|Endpoint|Method|
|---|---|---|
|Create User|`/users`|POST|
|Get All Users|`/users`|GET|
|Update User|`/users/{username}`|PUT|

---

### 🔑 Key Concepts Covered:

- MongoDB `@DBRef` for relational-like reference.
    
- `@Indexed(unique = true)` for username constraint.
    
- Updating users **without ID**, using `username` instead.
    
- `ResponseEntity` for consistent HTTP responses.
    
- Postman testing to verify endpoints.
    
- Prepared ground for future authentication features.
    




## 🎯 Goal

You are building a journaling app where:

- Each **user** can have multiple **journal entries**.
    
- The app should allow:
    
    - ✅ Getting all journal entries of a specific user
        
    - ✅ Creating a journal entry and associating it with the user
        
    - ✅ (Later) Retrieving by ID, deleting, and updating
        

---

## ✅ 1. Get All Journal Entries of a Specific User

### 🧠 Concept

Instead of a generic `/journal` GET endpoint, update it to get journal entries of a specific user using their `username` as a path variable.

### 🔧 Controller Code

```java
@RestController
@RequestMapping("/journal")
public class JournalController {

    @Autowired
    private UserService userService;

    @GetMapping("/{username}")
    public List<JournalEntry> getAllJournalEntriesOfUser(@PathVariable String username) {
        User user = userService.findByUsername(username);
        return user.getJournalEntries();  // Return user's journal entries list
    }
}
```

### 🧪 Test with Postman

- **Method**: `GET`
    
- **URL**: `http://localhost:8080/journal/Vipul`
    

---

## ✅ 2. Create and Link Journal Entry to User

### 🧠 Concept

When saving a journal entry:

- Save it in the `journalEntries` collection.
    
- Also add its ID to the user’s `journalEntries` list in the `users` collection.
    

### ⚠️ Problem Encountered

A `Bad Request` was occurring because the `JournalEntry` class was using Lombok's `@RequiredArgsConstructor` without a no-arg constructor, which is **required for deserialization** from JSON.


---

## 🧩 What Are They?

These are **Lombok annotations** that **automatically generate constructors** for your class.

---

### ✅ `@NoArgsConstructor`

- Generates a **no-argument constructor**:  
    `public JournalEntry() { }`
    
- Needed by:
    
    - **Spring Boot**, **Jackson**, and **JPA** to **deserialize JSON** or **instantiate beans**.
        
    - Especially when you're using `@RequestBody` to map JSON to a Java object.
        

#### 📌 When to use

- Always use it in **DTOs**, **Entities**, or **Models** where Spring/Jackson need to auto-create objects.
    

---

### ✅ `@AllArgsConstructor`

- Generates a constructor with **all fields as parameters**.  
    Example:
    
    ```java
    public JournalEntry(String title, String content, LocalDateTime timestamp) {
        this.title = title;
        this.content = content;
        this.timestamp = timestamp;
    }
    ```
    

#### 📌 When to use

- Helpful for **manually creating objects** in your code (e.g., in tests or when you want to quickly construct a full object).
    

---

## ✅ Use Together

Using **both** is common in Spring Boot apps:

```java
@Data
@NoArgsConstructor  // for deserialization (Spring/Jackson)
@AllArgsConstructor // for convenience when creating manually
public class JournalEntry {
    private String title;
    private String content;
    private LocalDateTime timestamp;
}
```

---

## 🧪 Without @NoArgsConstructor – You Get an Error

Example error when missing:

```
com.fasterxml.jackson.databind.exc.InvalidDefinitionException:
Cannot construct instance of `JournalEntry` (no Creators, like default constructor)
```

➡️ This is because Jackson tries to call `new JournalEntry()` but it's missing.

---

## ✅ Summary

|Annotation|Purpose|Required For|
|---|---|---|
|`@NoArgsConstructor`|Creates `public ClassName()`|✅ JSON deserialization, Spring|
|`@AllArgsConstructor`|Creates constructor with all fields|✅ Manual object creation (convenience)|

---

Let me know if you want to generate constructors only for some fields (in that case, `@RequiredArgsConstructor` might be helpful).



### ✅ Fix

Add `@NoArgsConstructor` to the `JournalEntry` class.

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JournalEntry {
    private String title;
    private String content;
    private LocalDateTime timestamp;
    // other fields...
}
```

### 🔧 Controller Code (Create Entry)

```java
@PostMapping("/{username}")
public ResponseEntity<String> createEntry(@PathVariable String username,
                                          @RequestBody JournalEntry entry) {
    journalService.saveEntry(username, entry);
    return ResponseEntity.ok("Entry saved successfully");
}
```

---

## 🛠️ 3. JournalService Method: saveEntry()

### 💡 Logic

1. Find the user.
    
2. Save the journal entry to `journalEntries` collection.
    
3. Add the journal entry's ID to the user's journal entry list.
    
4. Save the updated user.
    

### 🔧 Service Code

```java
@Service
public class JournalService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private JournalRepository journalRepo;

    public void saveEntry(String username, JournalEntry entry) {
        User user = userRepo.findByUsername(username);

        entry.setTimestamp(LocalDateTime.now());
        JournalEntry savedEntry = journalRepo.save(entry);

        user.getJournalEntries().add(savedEntry);  // Add to user's list
        userRepo.save(user);  // Save user with updated journal references
    }
}
```

---

## ✅ Debugging Tips Mentioned

- Use **breakpoints** in the controller and service.
    
- Check Postman request format.
    
- Watch out for constructor errors (use `@NoArgsConstructor` with Lombok).
    
- Check MongoDB to confirm that:
    
    - Entry is saved in `journalEntries`.
        
    - Entry ID is added in the respective user’s `journalEntries` list.
        

---

## ✅ Output Sample in MongoDB

### In `journalEntries` collection:

```json
{
  "_id": "661f...",
  "title": "test",
  "content": "df",
  "timestamp": "2025-05-18T12:00:00"
}
```

### In `users` collection:

```json
{
  "username": "Vipul",
  "journalEntries": ["661f..."]
}
```

---

## 🧪 Final Testing

- Add entries via `POST /journal/Vipul`
    
- View entries via `GET /journal/Vipul`
    

Everything works! You have created a bi-directional connection between user and journal entries.

---

Let me know when you're ready to implement:

- 📌 Get journal entry by ID
    
- ✏️ Update journal entry
    
- ❌ Delete journal entry
    

I'll help you with clean code and explanations.




### 🎯 **Goals of this Section:**

- Delete a specific journal entry by ID.
    
- Remove the reference of that journal entry from the user document.
    
- Understand why we must do this manually in MongoDB.
    
- Handle update operations for journal entries.
    
- Identify potential data inconsistency problems and plan for transaction-based solutions.
    

---

### 🧩 **Initial Delete Setup**

When deleting a journal entry, we must also remove its reference from the associated user’s list of journal entries.

```java
@DeleteMapping("/journal/{id}/{username}")
public ResponseEntity<Void> deleteJournalEntry(@PathVariable String id, @PathVariable String username) {
    journalEntryRepository.deleteById(new ObjectId(id));

    User user = userRepository.findByUsername(username).orElseThrow();
    user.getJournalEntries().removeIf(entry -> entry.getId().toString().equals(id));
    userRepository.save(user);

    return ResponseEntity.noContent().build();
}
```

### 📝 **Key Points:**

- **MongoDB** does not **automatically handle cascading deletes** like relational DBs (via foreign keys).
    
- You must **manually update the user** document and remove the deleted journal entry’s reference.
    
- If this is not done, it causes **data inconsistency**: the journal entry is gone, but a dead reference remains in the user document.
    

---

### 🔁 **Demonstration & Testing:**

- **Created two entries** from Postman.
    
- Verified they exist in MongoDB using the shell.
    
- **Deleted one entry** using `DELETE /journal/{id}/{username}`.
    
- Verified it was removed both:
    
    - from the `journalEntries` collection
        
    - and from the associated `User` document (if the `removeIf` was not commented out).
        

### ❗ **If `removeIf` is commented out:**

- Entry will still be removed from `journalEntries`.
    
- But its **reference will remain** in the `User` document.
    
- On next save, Spring Data MongoDB **may clean it up**, but **this is unreliable**.
    

---

### ⚠️ **Illustrating Inconsistency:**

When the entry is deleted but the user is not updated:

```java
user.getJournalEntries().removeIf(entry -> entry.getId().toString().equals(id));
userRepository.save(user);
```

If this block is **missing**, then on the next post/save, Spring might ignore or overwrite the stale reference, but this behavior is **not safe**.

---

### 🛠️ **Fix: Immediate Consistency**

Ensure the `user` is updated **at the same time** the journal entry is deleted.

---

### 🧪 **Journal Entry Update Endpoint**

Now, let’s handle updating a journal entry.

```java
@PutMapping("/journal/{id}/{username}")
public ResponseEntity<JournalEntry> updateJournalEntry(
    @PathVariable String id,
    @PathVariable String username,
    @RequestBody JournalEntry updatedEntry
) {
    JournalEntry existing = journalEntryRepository.findById(new ObjectId(id))
        .orElseThrow(() -> new RuntimeException("Entry not found"));

    existing.setTitle(updatedEntry.getTitle());
    existing.setContent(updatedEntry.getContent());
    // any other fields to be updated...

    JournalEntry saved = journalEntryRepository.save(existing);

    return ResponseEntity.ok(saved);
}
```

#### ✅ Key Notes:

- No need to modify the `User` document because the **reference remains unchanged**.
    
- A method without a username parameter was added for generic saves.
    

---

### 🧪 **Testing:**

- Sent a `PUT` request with new title/content using Postman.
    
- Validated that the entry was updated successfully in MongoDB.
    

---

### ⚠️ **Final Problem Identified: Partial Save Issue**

- If we **save the journal entry** successfully...
    
- But **fail before updating the user document** (e.g., exception occurs)...
    
- We get **inconsistent state**:
    
    - Journal entry exists.
        
    - User doesn't reference it.
        

#### 🧠 Solution (Coming in next video):

Use **MongoDB Transactions** to ensure **atomicity** — either both the journal entry and user update succeed or both fail.

---

### 🔚 **Summary:**

|Operation|Journal Collection|User Document|Status|
|---|---|---|---|
|Create|✅ Entry Saved|✅ Reference Added|✅|
|Delete|✅ Entry Deleted|✅ Reference Removed|✅|
|Update|✅ Entry Updated|❌ No User Change Needed|✅|
|Partial Save Risk|✅ Saved|❌ Not Updated|❌ (needs transaction)|

---

### 🧪 Simple Save Method (Without Username)

```java
public JournalEntry saveEntry(JournalEntry entry) {
    return journalEntryRepository.save(entry);
}
```

---

### 🧪 Save with Username (For Linking to User)

```java
public JournalEntry saveEntry(JournalEntry entry, String username) {
    JournalEntry saved = journalEntryRepository.save(entry);

    User user = userRepository.findByUsername(username).orElseThrow();
    user.getJournalEntries().add(saved);
    userRepository.save(user);

    return saved;
}
```
