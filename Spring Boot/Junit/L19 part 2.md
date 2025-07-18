
## ✅ **Topic: Advanced JUnit Testing in Spring Boot**

---

## 🔹 1. **Debugging Unit Tests**

- You can **run a test with a debugger** just like running the main application.
    
- IDE (like IntelliJ) allows setting **breakpoints** inside test methods.
    
- Helps in understanding **why a test fails** (e.g., inspecting object values).
    
- During debugging:
    
    - You can see each assertion's result.
        
    - Skip or resume specific lines.
        

---

## 🔹 2. **Parameterized Tests**

### 🔸 Why use parameterized tests?

- Avoid duplicating test methods for similar logic with different input values.
    
- Helps test the same logic with **multiple inputs and expected outputs**.
    

### 🔸 Example using `@ParameterizedTest` and `@CsvSource`:

```java
@ParameterizedTest
@CsvSource({
    "1, 1, 2",    // Pass
    "2, 10, 12",  // Pass
    "3, 3, 9"     // Fail (3+3 != 9)
})
void testAddition(int a, int b, int expected) {
    assertEquals(expected, a + b);
}
```

### ❗ Explanation:

- `a`, `b`, `expected` are passed from CSV values.
    
- This test will run **three times** with different inputs.
    
- If expected and actual don’t match, test fails.
    

---

## 🔹 3. **Disabling a Test**

Use `@Disabled` to **skip a test temporarily**:

```java
@Disabled
@Test
void someTestToSkip() {
    // This won't run
}
```

---

## 🔹 4. **Adding Failure Messages**

You can add a **custom failure message** in assertions:

```java
assertNotNull(user, "Failed for: " + name);
```

If the test fails, the message appears in the test result logs, helping you quickly find which case failed.

---

## 🔹 5. **Using `@ValueSource` for Single Parameters**

When you want to test with **one parameter only** (like a username):

```java
@ParameterizedTest
@ValueSource(strings = {"Ram", "Shyam", "Vipul"})
void testUserNotNull(String username) {
    User user = userRepository.findByUsername(username);
    assertNotNull(user, "Failed for: " + username);
}
```

---

## 🔹 6. **Using `@EnumSource`**

When working with enums, use:

```java
@ParameterizedTest
@EnumSource(MyEnum.class)
void testWithEnum(MyEnum value) {
    assertNotNull(value);
}
```

---

## 🔹 7. **Using `@CsvFileSource` for External CSV Data**

- Place a CSV file in `src/test/resources`
    
- Example usage:
    

```java
@ParameterizedTest
@CsvFileSource(resources = "/test-data.csv", numLinesToSkip = 1)
void testWithCsvFile(String username, int expectedCount) {
    // test logic
}
```

- `numLinesToSkip = 1` skips the header row
    

---

## 🔹 8. **Custom Argument Provider with `@ArgumentsSource`**

### Step-by-step:

1. **Create custom class** that implements `ArgumentsProvider`:
    

```java
public class UserArgumentProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return Stream.of(
            Arguments.of(new User("Ram", "ram123")),
            Arguments.of(new User("Shyam", "shyam123")),
            Arguments.of(new User("Suraj", ""))
        );
    }
}
```

2. **Use it in test method**:
    

```java
@ParameterizedTest
@ArgumentsSource(UserArgumentProvider.class)
void testSaveUser(User user) {
    assertTrue(userService.saveNewUser(user));
}
```

---

## 🔹 9. **Handling Builder Pattern in Tests**

If your `User` class uses the builder pattern:

```java
User user = User.builder()
    .username("Ram")
    .password("ram123")
    .build();
```

❗ Remember to call `.build()` at the end, or it will throw an error.

---

## 🔹 10. **Void vs Boolean Methods for Testing**

Original method:

```java
public void saveNewUser(User user) {
    userRepository.save(user);
}
```

Better for testing:

```java
public boolean saveNewUser(User user) {
    try {
        userRepository.save(user);
        return true;
    } catch (Exception e) {
        return false;
    }
}
```

Then in your test:

```java
assertTrue(userService.saveNewUser(user));
```

This makes it easier to assert whether the operation was successful.

---

## 🔹 11. **Running Server to Validate User Creation**

After saving users via unit test, you can:

- Start your Spring Boot server
    
- Use tools like **Postman** or **browser** to hit `/admin/getAllUsers`
    
- Verify that users like "Ram", "Shyam", "Suraj" exist
    

Also ensure:

- The server is running on the correct port (`application.yml` set to 8080)
    
- Your `AdminController` or endpoint exists and is accessible
    

---

## ✅ Summary Table

|Concept|Description|
|---|---|
|`@SpringBootTest`|Loads Spring context for tests|
|`@ParameterizedTest`|Runs a test multiple times with different inputs|
|`@CsvSource`|Inline CSV for multi-param test cases|
|`@ValueSource`|For single-parameter tests (Strings, Ints)|
|`@Disabled`|Temporarily skip a test|
|`@EnumSource`|Supplies enum values to a test|
|`@ArgumentsSource`|Custom argument providers for complex test data|
|`assertEquals/True/NotNull`|Assertions to validate output|
|Builder Pattern|Don’t forget `.build()` in tests|
|Save Method for Testing|Prefer returning `boolean` for testable result|
|Debugging|Use breakpoints to inspect test behavior|

---

## 🔸 Recommendations

- ✅ Use `@SpringBootTest` for integration tests.
    
- ✅ Use `@ParameterizedTest` to avoid repetitive test methods.
    
- ✅ Return testable values (like `boolean`) from service methods.
    
- ✅ Use custom providers (`@ArgumentsSource`) for complex objects.
    
- ✅ Add messages in assertions for better debugging.
    
