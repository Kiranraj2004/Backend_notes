
## ✅ Goal of This Video

Enable **user-specific authorization** in the `JournalController` so that:

- Ram can only access Ram's journal entries.
    
- Shyam can only access Shyam’s journal entries.
    
- All journal operations (get, add, update, delete) are protected and **only authenticated users** can perform operations on their own data.
    

---

## 🔐 Security Problem Before Fixing

- Earlier, **journal endpoints were public**, and you could pass any username as a path variable without authentication.
    
- Anyone could access any other user's journal entries.
    

---

## 🛠 Setup and Initial Steps

### 1. Run Spring Boot App

- Application runs on default port (localhost:8080).
    
- Open **Postman** and **MongoDB Atlas**.
    
- MongoDB collections are cleaned: `user`, `journal`, and `public`.
    

### 2. Create a Public Endpoint for Health Check

```http
GET /public/health-check
```

### 3. Create User Endpoint (No Authentication Needed)

```http
POST /public/create-user
```

**Request Body (Raw JSON):**

```json
{
  "username": "ram",
  "password": "ram"
}
```

- User is created with encrypted password and default role `USER`.
    
- Journal entries array is initially empty.
    

---

## 🔒 Enable Authorization for `/journal/**` Endpoints

In `SecurityConfig`:

```java
http
  .authorizeRequests()
  .antMatchers("/public/**").permitAll()
  .antMatchers("/journal/**").authenticated();
```

This ensures `/journal/**` requires authentication.

---

## 📬 Test GET Journal Entries with Authentication

### Endpoint:

```http
GET /journal/ram
```

### Auth:

Use **Basic Auth** in Postman:

- Username: `ram`
    
- Password: `ram`
    

If user is authenticated but no entries are present → returns `404 Not Found`.

---

## 📝 Add Journal Entry (Authenticated User Only)

### Endpoint:

```http
POST /journal/ram
```

### Body:

```json
{
  "title": "Block your medical friends",
  "content": "If you do better than them, they will try to bring you down to their level."
}
```

- Entry gets saved.
    
- MongoDB stores it with reference in both `journal` and under the user’s `journalEntries` list.
    

---

## 📌 Journal Controller Structure

Endpoints implemented:

- `GET /journal/{username}` → Get all entries of the user.
    
- `POST /journal/{username}` → Add journal entry.
    
- `GET /journal/{username}/{id}` → Get entry by ID.
    
- `PUT /journal/{username}/{id}` → Update by ID.
    
- `DELETE /journal/{username}/{id}` → Delete by ID.
    

---

## ✅ Secure `GET BY ID` Endpoint

**Security Concern:**

> A user should not be able to fetch someone else’s journal by using their ID.

### Fix:

- Extract the logged-in username from the request.
    
- Retrieve user’s journal entry list.
    
- Check if the requested ID exists in that list.
    

### Implementation Logic:

```java
List<JournalEntry> userEntries = userService.findByUsername(username).getJournalEntries();
boolean ownsEntry = userEntries.stream()
    .anyMatch(entry -> entry.getId().equals(requestedId));

if (!ownsEntry) {
    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not your journal");
}

return journalService.findById(requestedId);
```

This ensures **only the owner** of the journal entry can access it.

---

## 🔧 Debugging & Fixing Save Entry Logic

### Problem:

- Although the journal entry is saved in the journal collection,
    
- The user's `journalEntries` list wasn't updated (entry ID not added).
    

### Fix:

In `JournalService.saveEntry()`:

1. Save the journal entry.
    
2. Retrieve the user by username.
    
3. Append the journal reference to `user.getJournalEntries()`.
    
4. Save the updated user.
    

```java
user.getJournalEntries().add(savedEntry);
userRepository.save(user);
```

---

## 🔐 MongoDB Atlas Tip

If you're using a new system or IP address:

- Ensure your **IP is whitelisted** in MongoDB Atlas:
    
    - Go to “Network Access” → Add Current IP.
        

---

## ✅ Summary of Key Security Fixes

|Endpoint|Access Control|
|---|---|
|`/public/**`|Public (no auth)|
|`/journal/**`|Requires auth|
|Journal operations|Only user’s own data|

---

## 🔚 Final Outcome

- Journal operations are now **user-specific**.
    
- Only authenticated users can:
    
    - View their own journal list.
        
    - Add journal entries for themselves.
        
    - Fetch / update / delete only their own journal entries.
        
- Authorization checks are handled **both at route level and inside service layer** using `username` and `id` validation.
    



### part 2


1. Refactor `saveUser()` and separate password encoding logic.
    
2. Complete secure implementations for:
    
    - `GET by ID`
        
    - `DELETE by ID`
        
    - `UPDATE by ID`
        
3. Ensure journal operations are:
    
    - Authenticated (via Spring Security Basic Auth).
        
    - Authorized (only the owner of the journal entry can modify/delete/view it).
        

---

## 🛠 REFACTORING: Split `saveUser()` into Two Methods

### Problem:

- The same `saveUser()` method is used in multiple places (create, update, internal logic).
    
- If used during update or internal save, it **re-encodes an already-encoded password**, causing **bugs**.
    

### Solution:

#### 🔧 Split into:

```java
// Used only for new user creation (encodes password)
public User saveNewUser(User user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    return userRepository.save(user);
}

// Used for updating existing user (doesn't re-encode)
public User saveUser(User user) {
    return userRepository.save(user);
}
```

### Update usage in:

- `PublicController` → use `saveNewUser()`
    
- `UserController` → during update → use `saveNewUser()` if password is changed
    
- Internal calls (like while deleting or updating journal entries) → use `saveUser()` only
    

---

## 🧹 Clean Slate Testing

To ensure clarity:

- Deleted all existing data from MongoDB (`users` and `journal` collections).
    
- Re-tested:
    
    1. Creating user
        
    2. Adding journal entry
        
    3. Fetching journal entry by ID
        
    4. Ensuring auth and ownership checks work correctly
        

---

## ✅ Secure `DELETE by ID` Endpoint

### Key Steps:

1. Extract logged-in username from **SecurityContextHolder**.
    
2. Fetch user and their journal entries.
    
3. Check if the provided journal ID exists in their entries.
    
4. If yes:
    
    - Remove it from the user’s entry list.
        
    - Delete the journal entry.
        
    - Save the updated user.
        
    - Return `204 No Content`.
        
5. If no:
    
    - Return `404 Not Found`.
        

### Implementation:

```java
boolean removed = user.getJournalEntries().removeIf(entry -> entry.getId().equals(id));
if (removed) {
    journalRepository.deleteById(id);
    userService.saveUser(user);  // Don't re-encode password!
    return ResponseEntity.noContent().build(); // 204
} else {
    return ResponseEntity.notFound().build(); // 404
}
```

### ✅ Added improvement:

If trying to delete the **same ID twice**, it now properly returns `404 Not Found` instead of incorrectly saying `204`.

---

## ✅ Secure `UPDATE by ID` Endpoint

### Key Steps:

1. Remove `username` from path (derive from authenticated user).
    
2. Extract journal ID from path.
    
3. Fetch user and their journal entries.
    
4. Check if the journal ID exists in their entries.
    
5. If yes:
    
    - Retrieve the original journal entry from DB.
        
    - Update the fields (`title`, `content`, `updatedAt`, etc.).
        
    - Save the updated entry.
        
    - Return `200 OK`.
        
6. If no:
    
    - Return `404 Not Found`.
        

### Implementation:

```java
Optional<JournalEntry> oldEntry = journalRepository.findById(id);
if (oldEntry.isPresent()) {
    JournalEntry entry = oldEntry.get();
    entry.setTitle(updatedEntry.getTitle());
    entry.setContent(updatedEntry.getContent());
    entry.setUpdatedAt(LocalDateTime.now());
    journalRepository.save(entry);
    return ResponseEntity.ok("Journal entry updated.");
} else {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not your journal entry.");
}
```

---

## 🔁 Full Functionality Recap

|Operation|Auth Required|User Ownership Check|Method Used|
|---|---|---|---|
|Create User|❌|N/A|`saveNewUser()`|
|Get All Entries|✅|Yes (username filter)|`findByUsername()`|
|Get Entry by ID|✅|✅|Check journal ID in user's list|
|Add Entry|✅|✅|`saveEntry()` and update user’s journal list|
|Delete Entry by ID|✅|✅|Remove from both journal & user’s list|
|Update Entry by ID|✅|✅|Fetch → Modify → Save|

---

## 🧪 Testing in Postman

All operations were tested with **Basic Auth (RAM/RAM)**:

- ✅ POST `/public/create-user`
    
- ✅ POST `/journal` (Add entry)
    
- ✅ GET `/journal` (List all entries for user)
    
- ✅ GET `/journal/{id}` (Fetch entry by ID)
    
- ✅ DELETE `/journal/{id}` (Delete entry by ID with 204/404 handling)
    
- ✅ PUT `/journal/{id}` (Update entry with new title/content)
    

---

## ⚙️ Additional Enhancements Discussed

- Marked controller methods as `@Transactional` to avoid partial save issues.
    
- Plan to improve logging with proper `Logger` instead of `System.out.println`.
    
- Mentioned better auth strategies (like JWT) will be covered in future videos.
    
- Reinforced idea of using security context for user identification instead of passing username in the path.
    

---

## 🔚 Final Thoughts

This video:

- Completed user-specific **authorization** for all journal-related operations.
    
- Emphasized importance of keeping business logic separate and secure.
    
- Ensured no user can access, delete, or update another user's journal entries.
    

---
