
## ✅ Advanced JUnit Testing in Spring Boot – Detailed Notes

---

### 🔹 1. **Running and Debugging Tests**

- Tests can be executed with or without the debugger.
    
- Running the class using IntelliJ smartly sets default test configs.
    
- Debugging:
    
    - You can add breakpoints to pause and inspect variables.
        
    - Useful for fixing failed assertions and analyzing test failures.
        

---

### 🔹 2. **Fixing Test Failures Related to Port and Beans**

- If test doesn't work due to incorrect port or bean loading:
    
    - Ensure correct application port in `application.yml`.
        
    - Set correct Spring arguments (application context or profiles).
        
- If Spring fails to load a bean:
    
    - Likely missing `@SpringBootTest`.
        

---

### 🔹 3. **Testing via Maven**

#### 🔸 Command to run tests:

```bash
./mvnw test
```

or if Maven is installed globally:

```bash
mvn test
```

- Uses **Surefire plugin** (included in Spring Boot parent POM) to run tests.
    
- Output:
    
    - Number of tests run, passed, failed, or skipped.
        
    - HTML and plain-text reports generated under:
        
        ```
        target/surefire-reports/
        ```
        

---

### 🔹 4. **Understanding Surefire Reports**

- Generated automatically after tests via Maven.
    
- Includes:
    
    - Total test cases run
        
    - Number of passed, failed, skipped
        
    - Detailed info per test class
        
- Files:
    
    - `.txt` and `.xml` per class
        
    - HTML report if using reporting plugin
        

---

### 🔹 5. **Common Test Failures**

- **Unique constraint violation**: When inserting users with same username, MongoDB throws error due to `username` being unique.
    
- Ensure test data is unique or clean up DB between tests.
    

---

### 🔹 6. **Code Coverage with IntelliJ**

#### 🔸 Steps to View Coverage:

1. Install `Code Coverage for Java` plugin (if not already).
    
2. Click **Run with Coverage**.
    
3. Red = not covered; Green = covered
    
4. Helps identify:
    
    - Which methods or lines are not tested.
        
    - How much percentage of code is covered.
        

#### 🔸 Export Coverage Report:

- Export as HTML
    
- Includes:
    
    - Packages
        
    - Classes
        
    - Methods
        
    - Coverage % for each
        

#### 🔸 Best Practice:

- Demonstrate to managers/tech leads how much of the logic is verified via tests.
    

---

### 🔹 7. **Improving Code Coverage**

- Add more tests to cover untested paths (e.g., exception handling).
    
- Example: If exception block is not hit, those lines show up in red.
    

---

### 🔹 8. **JUnit Lifecycle Annotations**

|Annotation|Purpose|
|---|---|
|`@BeforeEach`|Runs before each test method. Ideal for initializing common data.|
|`@AfterEach`|Runs after each test method. Good for cleanup.|
|`@BeforeAll`|Runs once before all tests (static method). Used for global setup.|
|`@AfterAll`|Runs once after all tests (static method). Used for global cleanup.|

#### 🔸 Use Cases:

- Opening/closing file resources (e.g., CSVs)
    
- Setting up database state
    
- Resetting shared services
    

---

### 🔹 9. **Example – Lifecycle Usage**

```java
@BeforeAll
static void setupOnce() {
    // Create shared CSV or DB resource
}

@BeforeEach
void setup() {
    // Initialize per-test objects
}

@AfterEach
void cleanup() {
    // Clean up per-test
}

@AfterAll
static void tearDownOnce() {
    // Delete shared resources
}
```

---

### 🔹 10. **Best Practices for Testing**

- ✅ Use `@SpringBootTest` to load the full Spring context.
    
- ✅ Always use unique test data if DB constraints exist.
    
- ✅ Use parameterized tests to reduce duplication.
    
- ✅ Generate coverage reports regularly.
    
- ✅ Add assertions with custom messages for easier debugging.
    
- ✅ Run tests with Maven in CI/CD pipelines.
    
- ✅ Use lifecycle annotations to manage setup/teardown logic.
    

---

## ✅ Summary Table

|Feature|Description|
|---|---|
|`@SpringBootTest`|Loads the Spring context for tests|
|Maven Test Command|`./mvnw test` or `mvn test`|
|Surefire Reports|Auto-generated test reports in HTML/TXT|
|IntelliJ Code Coverage|Visual red/green line coverage display|
|Lifecycle Hooks|`@BeforeEach`, `@AfterEach`, `@BeforeAll`, `@AfterAll`|
|Debugging|Breakpoint-based test inspection|
|Export Coverage Report|HTML reports to share with managers|
|Unique Constraint Handling|Use unique data or clean DB between tests|

---
