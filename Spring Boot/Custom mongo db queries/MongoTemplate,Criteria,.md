The main topic is **interacting with MongoDB using Criteria and Query with `MongoTemplate`** in **Spring Boot**, instead of using derived query methods.

---

## ✅ 1. Traditional Way: Using Repository Interface

### Until now, how have we been interacting with MongoDB?

- We were using a **repository interface** that extends `MongoRepository`.
    
    ```java
    public interface UserRepository extends MongoRepository<User, String> {
        List<User> findByUsernameAndEmail(String username, String email);
    }
    ```
    
- Spring Boot auto-generates implementations for such repository interfaces at runtime and injects them where required (e.g., via `@Autowired`).
    
- These are **query method DSLs** (Domain Specific Language):
    
    - You write methods like `findByEmailAndUsernameOrderByCreatedAtDesc()`.
        
    - Spring interprets the method name and generates the query behind the scenes.
        

---

## ❌ Limitations of Method Name Queries

1. **Requires knowing exact syntax**.
    
    - No IntelliSense or auto-completion help.
        
    - Must refer to documentation for correct naming conventions.
        
2. **Limited flexibility**.
    
    - Complex filtering (e.g., range queries, negation, multiple nested conditions) is hard to express.
        
    - Difficult to dynamically build queries.
        

---

## ✅ Solution: Using `Criteria` and `Query` with `MongoTemplate`

### Why use `Criteria` and `Query`?

- More **flexible and powerful** than query DSL.
    
- Allows dynamic, conditional query building.
    
- Supports nested queries, regex, range conditions, projections, etc.
    

---

## 👨‍💻 Example: Custom Repository with Criteria

### Step 1: Create Custom Implementation Class

```java
@Component
public class UserRepositoryImpl {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<User> getUsersForSentimentAnalysis() {
        Query query = new Query();

        // Add criteria: email is not null or blank AND sentimentAnalysis = true
        query.addCriteria(
            Criteria.where("email").ne(null).ne("") // email exists
                    .and("sentimentAnalysis").is(true) // opted for analysis
        );

        return mongoTemplate.find(query, User.class);
    }
}
```

---

### 🔍 Explanation of Fields

Suppose the `User` class is like this:

```java
@Data
@Document(collection = "users")
public class User {
    private String id;
    private String email;
    private boolean sentimentAnalysis;
    // other fields
}
```

You’re querying users:

- Who have an email (i.e., not null or blank),
    
- And who opted in for sentiment analysis (`sentimentAnalysis = true`).
    

---

## 🧪 Step 2: Write a Unit Test to Verify

### In `test` directory:

```java
@SpringBootTest
public class UserRepositoryImplTests {

    @Autowired
    private UserRepositoryImpl userRepositoryImpl;

    @Test
    void testGetUsersForSentimentAnalysis() {
        List<User> users = userRepositoryImpl.getUsersForSentimentAnalysis();
        System.out.println(users);
        // Add assertions as needed
    }
}
```

- Use breakpoints or debug tools to inspect the generated query.
    
- Spring Boot uses the class `User.class` to figure out the collection name via `@Document`.
    

---

## ✅ Using Criteria

```java
Criteria.where("field").is(value);
Criteria.where("field").ne(value); // not equal
Criteria.where("field").gt(value); // greater than
Criteria.where("field").lt(value); // less than
Criteria.where("field").regex("pattern");
Criteria.where("field").in(list);
```

You can **chain multiple criteria** using `.and()`:

```java
new Criteria()
    .and("field1").is(val1)
    .and("field2").ne(val2);
```

---

## ✅ Using `MongoTemplate`

`MongoTemplate` is a helper class provided by Spring Data MongoDB for programmatic access.

Common methods:

```java
mongoTemplate.find(query, User.class);        // Find list
mongoTemplate.findOne(query, User.class);     // Find single
mongoTemplate.save(user);                     // Save document
mongoTemplate.remove(query, User.class);      // Delete
mongoTemplate.updateFirst(...)                // Update first match
mongoTemplate.updateMulti(...)                // Update all matches
```

---

## 🛠️ Troubleshooting

- If a query returns no results:
    
    - Double-check field names in MongoDB (e.g., `username` vs `name`).
        
    - Ensure documents exist that match the criteria.
        
- If error: `Failed to instantiate user`:
    
    - Add a **no-args constructor** to your `User` class.
        
        ```java
        public User() {}
        ```
        

---

## 🔁 Summary of Steps

|Step|Description|
|---|---|
|1.|Stop using method name DSL (`findBy...`) for complex queries|
|2.|Use a custom repository implementation (`UserRepositoryImpl`)|
|3.|Use `Query` and `Criteria` to build flexible queries|
|4.|Inject and use `MongoTemplate` to execute queries|
|5.|Test using `@SpringBootTest` and validate results|

---

## 🌟 Real-World Use Case: Sentiment Analysis Feature

- Users who opt in (i.e., `sentimentAnalysis = true`) will receive a weekly email with mood insights.
    
- This is achieved by:
    
    - Fetching those users using criteria query.
        
    - Analyzing their journal entries with NLP tools.
        
    - Sending personalized mood reports.
        

---


### part 2


Here are **detailed, structured notes** based on your second transcript section. It continues the topic of **building flexible MongoDB queries in Spring Boot** using `Criteria`, `Query`, and `MongoTemplate`.

---

## 🔄 Recap of What We're Building

We're querying the MongoDB `users` collection to:

- Find users who have:
    
    - A **valid email**.
        
    - Have **opted in for sentiment analysis** (`sentimentAnalysis = true`).
        
- Use **Criteria and Query** to build custom queries.
    
- Use `MongoTemplate` to execute those queries.
    
- Write tests to verify everything works.
    

---

## ✅ Advanced Usage of Criteria

### 1. Common Comparison Operators

```java
Criteria.where("field").lt(value);   // less than
Criteria.where("field").lte(value);  // less than or equal
Criteria.where("field").gt(value);   // greater than
Criteria.where("field").gte(value);  // greater than or equal
```

Example:

```java
Criteria.where("age").gt(29);  // age > 29
Criteria.where("username").is("Vipul").and("age").gt(20);
```

You can pass **strings or numbers**, but they should match the data type in MongoDB.

---

### 2. Matching Existing Field

If a field must **exist** in the document:

```java
Criteria.where("email").exists(true);
```

To also ensure it is **not empty** or **null**:

```java
Criteria.where("email").ne(null).ne("");
```

---

### 3. Using Regular Expressions for Email Validation

Instead of chaining `ne(null)` and `ne("")`, use regex:

```java
Criteria.where("email").regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
```

This ensures the email format is **valid** (basic regex example).

---

### 4. Using `andOperator` Explicitly

Instead of chaining `.and()` on multiple criteria, you can build a list and combine with `andOperator`.

```java
Criteria criteria = new Criteria().andOperator(
    Criteria.where("email").ne(null).ne(""),
    Criteria.where("sentimentAnalysis").is(true)
);

Query query = new Query(criteria);
```

Result:

- Both conditions must be true (logical AND).
    

---

### 5. Using `not().in()` for Exclusion (e.g., blacklist)

To exclude specific usernames (e.g., "Rajat", "Shanu"):

```java
Criteria.where("username").nin(Arrays.asList("Rajat", "Shanu"));
```

This helps in:

- Blacklisting users.
    
- Filtering out specific entries.
    

---

### 6. Matching Roles (with `in`)

If a field contains user roles (e.g., `["user", "admin"]`):

```java
Criteria.where("roles").in(Arrays.asList("user", "admin"));
```

MongoDB can match **arrays** or **strings**:

- Even if `roles` is a single string or an array, it works.
    

---

### 7. Filtering by Field Type using `BsonType`

You can filter documents by checking the type of a field:

```java
Criteria.where("sentimentAnalysis").type(BsonType.BOOLEAN);
```

Use `BsonType` for:

- `BOOLEAN`
    
- `STRING`
    
- `OBJECT_ID`
    
- `DATE`
    
- etc.
    

This is useful when:

- MongoDB documents are **inconsistent**.
    
- You want to filter by correct field type.
    

---

## 🧪 Test Preparation and Execution

### Update Sample Users

To test the query:

1. Pick a user from MongoDB (e.g., "Ram").
    
2. Add/update fields manually using MongoDB Compass:
    
    - `email: "abc@xyz.com"`
        
    - `sentimentAnalysis: true`
        
3. Create another user ("Shyam") with a **valid** email and `sentimentAnalysis: true`.
    
4. Save documents and ensure they match your query criteria.
    

---

### Debugging in IntelliJ

- Set breakpoints in your test.
    
- Use **Step Over (F8)** to go line-by-line.
    
- Inspect the generated Mongo query.
    
- Use `Alt + Enter` to refactor inline values into local variables.
    
- Use `Ctrl + Alt + ←` to jump back to previously viewed files.
    

---

### Writing the Unit Test

```java
@SpringBootTest
public class UserRepositoryImplTest {

    @Autowired
    private UserRepositoryImpl userRepositoryImpl;

    @Test
    void testGetUsersForSentimentAnalysis() {
        List<User> users = userRepositoryImpl.getUsersForSentimentAnalysis();

        assertNotNull(users);              // Basic test
        assertFalse(users.isEmpty());      // Ensure at least one user returned
    }
}
```

---

## ✅ Benefits of Using `Criteria` and `MongoTemplate`

|Criteria DSL|Method Name DSL|
|---|---|
|Fully customizable|Limited by method name patterns|
|Add/remove conditions dynamically|Not possible|
|Use regex, type checks, projections|Not available|
|More control over query logic|Less flexibility|
|Easier debugging|No visibility into query|

---

## 🧠 Final Thoughts from Instructor

- Technologies like this are **easy to forget**.
    
- You don’t need to memorize everything.
    
- Focus on **DSA**, **System Design**, and **problem solving**.
    
- Real learning happens when **building real-world features**.
    
- When requirements change, you’ll search, learn, and adapt.
    

> "Technology is like a movie — you enjoy it once, and then you forget it. What stays with you is the way of thinking."

---

## ✅ Summary of What You Learned

### ✔ Core Concepts

- Using `Criteria` to build MongoDB filters
    
- Executing queries with `MongoTemplate`
    
- Advanced query logic with AND, OR, IN, NOT IN
    
- Type-based filtering using `BsonType`
    
- Regex matching for validating fields like email
    

### ✔ Tools

- `Criteria`, `Query`, `MongoTemplate`
    
- MongoDB Compass for DB editing
    
- IntelliJ Debugging tools
    

### ✔ Real-World Use Case

- Weekly sentiment analysis mail for users who:
    
    - Have a valid email
        
    - Opted in for sentiment tracking
        

---

## 🧩 1. What is `MongoTemplate`?

### ✅ Definition:

`MongoTemplate` is a **core class provided by Spring Data MongoDB** to interact with MongoDB using **imperative (non-reactive)** programming.

It is the **lower-level alternative** to using `MongoRepository`.

---

### ✅ Why use `MongoTemplate`?

|Situation|Why Use `MongoTemplate`|
|---|---|
|Custom, dynamic, or complex queries|You can programmatically build flexible queries|
|More control over MongoDB operations|Full control over find, update, insert, delete, projections, pagination, etc.|
|When query method names become unreadable or impossible|Avoid long method names like `findByAgeGreaterThanAndStatusEqualsAndEmailLike...`|
|Need to run raw Mongo queries|MongoTemplate supports raw JSON queries too|
|No need to auto-generate repository classes|You can skip extending repository interfaces if not needed|

---

### ✅ Example:

```java
@Autowired
private MongoTemplate mongoTemplate;

Query query = new Query();
query.addCriteria(Criteria.where("age").gt(25));
List<User> users = mongoTemplate.find(query, User.class);
```

---

## 🔍 2. What is `Criteria`?

### ✅ Definition:

`Criteria` is a class that represents **filter conditions** used to form MongoDB queries in a fluent, readable way.

It’s like saying:

> “Give me users where age > 25 and status = 'ACTIVE'.”

---

### ✅ Why use `Criteria`?

|Need|Criteria helps with...|
|---|---|
|Filtering|`age > 25`, `name = 'John'`, `email != null`|
|Chaining conditions|`AND`, `OR`, `NOT`|
|Complex nested logic|Combine multiple filters in one object|
|Matching arrays, types, regex|Very easy with criteria|

---

### ✅ Example Conditions:

```java
Criteria.where("name").is("John");
Criteria.where("age").gt(25).lt(60);
Criteria.where("email").ne(null).ne("");
Criteria.where("tags").in(List.of("java", "spring"));
Criteria.where("username").regex("^[a-zA-Z0-9]+$");
```

---

## 🧾 3. What is `Query`?

### ✅ Definition:

`Query` is a wrapper around one or more `Criteria` objects.

It is used to **pass to `MongoTemplate`** to execute queries.

---

### ✅ Why use `Query`?

|Role|Why it matters|
|---|---|
|It’s the "query object"|Holds the filter (`Criteria`) and options (limit, sort, skip, etc.)|
|Required by `MongoTemplate`|You pass a `Query` object to `mongoTemplate.find()` or similar methods|
|Easily build complex queries|Add multiple `Criteria` and modifiers|

---

### ✅ Example:

```java
Query query = new Query();
query.addCriteria(Criteria.where("email").exists(true).ne(""));
query.addCriteria(Criteria.where("sentimentAnalysis").is(true));

List<User> result = mongoTemplate.find(query, User.class);
```

---

## ⚖️ Comparison: `MongoRepository` vs `MongoTemplate`

|Feature|MongoRepository|MongoTemplate|
|---|---|---|
|Abstraction Level|High (auto methods)|Low (manual query building)|
|Best For|Simple CRUD & finder methods|Complex, dynamic, flexible queries|
|Control|Limited to method names|Full programmatic control|
|Dynamic Filters|❌ Hard or not possible|✅ Easy|
|Custom Conditions|❌ Not supported|✅ Yes (regex, types, range, etc.)|

---

## ✅ Summary

|Component|Purpose|
|---|---|
|`MongoTemplate`|Executes database operations (find, insert, update, delete)|
|`Criteria`|Builds flexible filter conditions (where clauses)|
|`Query`|Holds one or more criteria and is passed to `MongoTemplate`|

---

## ✅ Real-World Example Use Case

### 📩 Goal:

Send weekly emails only to users who:

- Have a **valid email**.
    
- Have opted in for **sentiment analysis**.
    

### 👇 Implemented With:

```java
Query query = new Query();
query.addCriteria(
  Criteria.where("email").ne(null).ne("").regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")
);
query.addCriteria(Criteria.where("sentimentAnalysis").is(true));

List<User> result = mongoTemplate.find(query, User.class);
```

---

