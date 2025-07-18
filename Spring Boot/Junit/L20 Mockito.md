
## ✅ Topic: **Unit Testing with Mockito in Spring Boot**

---

### 🔧 Problem with Full Spring Context in Tests

- In previous videos, unit testing was done using `@SpringBootTest`.
    
- Using `@SpringBootTest` **loads the full application context** which:
    
    - Autowires dependencies.
        
    - Connects to the database.
        
    - Loads all beans.
        
- This is **slow**, especially for large applications.
    

---

### ✅ Solution: **Use Mocking with Mockito**

#### ➤ Why Mocking?

- **Mocking** allows us to:
    
    - Replace actual beans (like repositories) with **fake ones**.
        
    - **Avoid DB calls**.
        
    - **Focus on logic** within the class being tested.
        
    - **Speed up tests**.
        

---

## 🔁 Example Use Case

### 👨‍💻 Class Under Test:

```java
UserDetailServiceImpl
```

### 👨‍💻 Method to Test:

```java
loadUserByUsername(String username)
```

- This method internally calls:
    

```java
userRepository.findByUsername(username)
```

---

## ⚙️ Approach 1: Using `@MockBean` + `@SpringBootTest`

```java
@SpringBootTest
public class UserDetailServiceImplTest {

    @Autowired
    private UserDetailServiceImpl userDetailService;

    @MockBean
    private UserRepository userRepository;

    @Test
    void testLoadUserByUsername() {
        User user = User.builder()
                        .username("ram")
                        .password("pass")
                        .roles(new ArrayList<>())
                        .build();

        when(userRepository.findByUsername("ram")).thenReturn(user);

        UserDetails userDetails = userDetailService.loadUserByUsername("ram");

        assertNotNull(userDetails);
    }
}
```

### 🔍 Observations:

- `@SpringBootTest` loads the Spring context.
    
- `@MockBean` replaces the real `UserRepository` bean with a mock.
    
- Actual DB is not called, but context loading still takes time.
    

---

## ⚙️ Approach 2: Using `@Mock` + `@InjectMocks` (No Spring Context)

```java
@ExtendWith(MockitoExtension.class)
public class UserDetailServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailServiceImpl userDetailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoadUserByUsername() {
        User user = User.builder()
                        .username("ram")
                        .password("pass")
                        .roles(new ArrayList<>())
                        .build();

        when(userRepository.findByUsername(anyString())).thenReturn(user);

        UserDetails userDetails = userDetailService.loadUserByUsername("ram");

        assertNotNull(userDetails);
    }
}
```

### 🔍 Key Concepts:

- `@Mock` creates a mock of `UserRepository`.
    
- `@InjectMocks` creates an instance of `UserDetailServiceImpl` and injects mocks.
    
- `MockitoAnnotations.openMocks(this)` initializes mocks manually.
    
- This approach is **faster** as it avoids Spring Boot context altogether.
    

---

### 🔁 Common Mistake

- Using `@Mock` with `@SpringBootTest` causes `NullPointerException` because `@Mock` doesn’t register the mock with Spring context.
    
- Solution: Use `@MockBean` for Spring-based tests or `@InjectMocks` with `@Mock` for pure unit tests.
    

---

### 💡 Extra: When to Use `@MockBean` vs `@Mock`

|Annotation|Context Loaded?|Purpose|
|---|---|---|
|`@MockBean`|Yes (`@SpringBootTest`)|Replaces bean in Spring context|
|`@Mock`|No|Basic Mockito mock|
|`@InjectMocks`|No|Injects mocks into the class under test|

---

## 🧠 Key Benefits of Mockito-based Testing

- ✅ Fast execution (no DB or context load)
    
- ✅ Isolated testing
    
- ✅ Precise control over dependencies
    

---

### ✅ Summary

- Use `@Mock` + `@InjectMocks` for pure unit testing (fast, no context).
    
- Use `@MockBean` with `@SpringBootTest` when Spring context is needed.
    
- Use `MockitoAnnotations.openMocks(this)` in setup to initialize mocks.
    
- Mock repositories/services to return dummy objects.
    
- Avoid using actual DB for unit tests.
    

---

Let me know if you’d like a PDF version of these notes or example test files for reference.