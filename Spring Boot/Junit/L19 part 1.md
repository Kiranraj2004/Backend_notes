
## ✅ **Topic: Unit Testing with JUnit in Spring Boot**

---

### 🔹 **What is Unit Testing?**

- **Definition**: Testing individual units/components of code separately.
    
- Example: If an API has 4 functions, you test each one **individually**.
    
- Ensures that **each function** behaves as expected.
    

---

### 🔹 **Test-Driven Development (TDD)**

- **Definition**: Writing tests **before** or **during** development of code.
    
- You write a function, then write a test to confirm it works.
    
- Encourages **continuous testing** as code evolves.
    

---

### 🔹 **JUnit in Java**

- Java testing framework.
    
- **Full form**: Java Unit
    
- Used for unit testing Java code.
    

---

### 🔹 **Setting up JUnit in Spring Boot**

- Spring Boot uses **JUnit 5** by default.
    
- Already included in the dependency:
    

```xml
<!-- pom.xml -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

- `scope = test`: means the dependency is used only during **testing phase**.
    
- JUnit 5 = `JUnit Jupiter`.
- in order to take look for j unit dependency in it ctrl+right click on spring-boot-starter-test
- search for junit
-
    

---

### 🔹 **JUnit Folder Structure in Spring Boot**

- `src/main/java`: Main application code.
    
- `src/test/java`: Test classes.
    

Spring Boot auto-generates a test file:

```java
JournalAppApplicationTests.java
```

You write tests for each component/service here.

---

### 🔹 **How to Create a Test Class**

1. Mimic the main package structure in `src/test/java`.
    
2. Example: If you have a `UserService` in `service` package, then in `test/service`, create:
    

```java
public class UserServiceTests {
    @Test
    public void testSomething() {
        // test logic
    }
}
```

---

### 🔹 **Writing Your First Unit Test**

#### ✅ Sample Test Method

```java
@Test
public void testAddition() {
    assertEquals(4, 2 + 2); // This will pass
}
```

#### ❌ Failing Example

```java
@Test
public void testFail() {
    assertEquals(4, 2 + 1); // Will FAIL
}
```

> ❗ `assertEquals(expected, actual)` — Verifies if actual result matches expected.

---

### 🔹 **Commonly Used JUnit Assertions**

|Assertion|Description|
|---|---|
|`assertEquals`|Checks if two values are equal|
|`assertNotEquals`|Checks if two values are NOT equal|
|`assertTrue`|Checks if a condition is true|
|`assertFalse`|Checks if a condition is false|
|`assertNull`|Checks if value is null|
|`assertNotNull`|Checks if value is NOT null|
|`assertSame`|Checks if two references point to same object|
|`assertNotSame`|Checks if two references point to different obj|

---

### 🔹 **Testing Spring Components (e.g., UserRepository)**

```java
@SpringBootTest
public class UserServiceTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByUsername() {
        User user = userRepository.findByUsername("ram");
        assertNotNull(user);
    }
}
```

#### ❗ Problem: `NullPointerException`

**Why?** Because Spring did not create the beans.
when ever the projects runs then only spring is going to create the beans

**Solution**: Use `@SpringBootTest` to load the full Spring context.

---

### 🔹 **Understanding `@SpringBootTest`**

- It **starts the application context**.
    
- Ensures that all `@Component`, `@Service`, `@Repository` beans are created.
    
- Required for tests that depend on **Spring-managed beans**.
    

---

### 🔹 **Testing Database Interactions**

You can write tests that interact with the database:

```java
@Test
public void testFindByUsernameReturnsUser() {
    User user = userRepository.findByUsername("ram");
    assertNotNull(user);
    assertEquals("ram", user.getUsername());
}
```

### ✅ More Examples:

#### Test True Condition

```java
@Test
public void testCondition() {
    assertTrue(5 > 3);
}
```

#### Test User Has Journal Entries

```java
@Test
public void testUserHasJournalEntries() {
    User user = userRepository.findByUsername("ram");
    assertFalse(user.getJournalEntries().isEmpty());
}
```

---

### 🔹 **Running Tests**

- Right-click on test method or class → **Run**
    
- Or use Maven:
    

```bash
./mvnw test
```

Output will indicate which tests **passed** or **failed**.

---

### 🔹 **Best Practices**

- **Write separate test methods** for each condition.
    
- Give meaningful names: `testFindByUsernameReturnsUser`, `testUserHasNoEntries`, etc.
    
- Use **asserts** to validate expected outcomes.
    

---

### 🔹 **What is an Assertion?**

- An **assertion** is a claim that must be true.
    
- If it is not, the test **fails**.
    
- Example: `assertNotNull(user)` claims user is not null. If it is null → test fails.
    

---

### 🔹 **Conclusion:**

- Unit testing helps validate individual components.
    
- Use `@SpringBootTest` to load Spring context and test beans.
    
- JUnit assertions make it easy to validate expectations.
    
- Test methods should cover:
    
    - Normal scenarios
        
    - Edge cases
        
    - Invalid inputs
        

---

### ✅ Summary Table

|Concept|Description|
|---|---|
|Unit Testing|Testing individual components|
|JUnit|Java framework for testing|
|`assertEquals`|Check expected == actual|
|`@Test`|Marks a method as a test|
|`@SpringBootTest`|Starts Spring context for integration/unit testing|
|`assertNotNull(user)`|Ensures user object is not null|
|NullPointer in test|Happens if Spring beans not initialized|
|Test Driven Development (TDD)|Write test before or during code|



## 🔍 **Understanding the Issue**

### ❓Why did we get a `NullPointerException`?

Because you tried to `@Autowired` a Spring bean (e.g., `UserRepository`) **in a test class**, but the **Spring context was never started**, so no beans were created. Hence, the injected dependency is `null`.

---

## ✅ **When are Spring beans created?**

Spring beans are created only when:

1. The application **runs** (like using the main method in `JournalAppApplication.java`), **OR**
    
2. The test uses `@SpringBootTest` or similar annotations that tell Spring to start the **application context**.
    

---

## ❓Can we run the main app and then run the test without `@SpringBootTest`?

No, not in the way you're thinking. Here's why:

> 💡 **Test classes run in a separate JVM process or thread.**  
> They **do not** use the same Spring context as the running application unless explicitly configured to do so — which is **not** the default.

So even if your Spring Boot application (e.g., JournalAppApplication) is running, the test class won't know about that context **unless it starts its own context using annotations like `@SpringBootTest`**.

---

## ❌ Why running the server first **won’t work for tests**

|Misconception|Reality|
|---|---|
|Running the app creates beans, so tests will work|**False** — Tests run in isolation and **don’t connect to already running app’s context**|
|Tests can use beans from the main application|**Only** if you tell Spring to initialize context via `@SpringBootTest`, `@DataJpaTest`, etc.|

---

## ✅ **Correct Way to Test Spring Components**

Use:

```java
@SpringBootTest
public class UserServiceTests {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByUsername() {
        User user = userRepository.findByUsername("ram");
        assertNotNull(user);
    }
}
```

This works because `@SpringBootTest` tells Spring to:

- Start the application context
    
- Scan components
    
- Inject dependencies
    
- Simulate a runtime Spring Boot environment
    

---

## 🔄 Summary

|Question|Answer|
|---|---|
|Will running the main Spring Boot app allow tests to use its beans?|❌ No, because tests run in a separate context|
|What should you do instead?|✅ Use `@SpringBootTest` in the test class to create the context|
|Can test methods use autowired components without context?|❌ No, leads to `NullPointerException`|
|When are beans created?|✅ When the application context is started, either by running the app or using `@SpringBootTest`|

