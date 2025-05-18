
## 📘 What is Project Lombok?

**Lombok** is a Java library used to **reduce boilerplate code** like:

- Getters and Setters
    
- Constructors (NoArgs and AllArgs)
    
- `toString()`, `equals()`, and `hashCode()` methods
    
- Builder pattern
    
- Logging (`@Slf4j`)
    

It helps keep your code clean and readable by **automatically generating these methods during compilation** using annotations.

---

## ✅ Why Use Lombok?

Without Lombok, a typical Java class needs manual writing or generating of:

- Getters and Setters
    
- Constructors
    
- `toString()` and `equals()` methods
    

➡️ This results in a lot of repetitive and bloated code.  
➡️ IDEs (like IntelliJ or Eclipse) can generate them, but it's still manual work.

**With Lombok**, you write:

```java
@Data
public class User { ... }
```

Instead of:

```java
public class User {
  private String name;
  private int age;

  public String getName() { ... }
  public void setName(String name) { ... }
  public String toString() { ... }
  public boolean equals(Object o) { ... }
  public int hashCode() { ... }
}
```

---

## 🔧 How Lombok Works

- Lombok annotations are processed at **compile-time**.
    
- The required methods are inserted into the compiled `.class` files.
    
- So although you **don’t see** the methods in your code, they **exist** at runtime.
    

Example from transcript:

> "Lombok registers bytecode for the methods like getters, setters, constructors, equals, hashCode, toString as specified by the annotations. This code is added to the compiled `.class` files."

---

## 📦 How to Use Lombok in Spring Boot

### Step 1: Add Lombok to `pom.xml` (Maven)

```xml
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>1.18.30</version> <!-- or latest -->
    <scope>provided</scope>
</dependency>
```

Then, **reload Maven**.

### Step 2: Install Lombok Plugin (in IntelliJ IDEA)

> IntelliJ doesn't understand Lombok annotations out-of-the-box.

1. Go to `Settings` → `Plugins`
    
2. Search for `Lombok`
    
3. Install and restart IntelliJ
    

---

## ✨ Common Lombok Annotations

|Annotation|Description|
|---|---|
|`@Getter`|Generates getter methods for all fields|
|`@Setter`|Generates setter methods for all fields|
|`@ToString`|Generates `toString()` method|
|`@EqualsAndHashCode`|Generates `equals()` and `hashCode()` methods|
|`@NoArgsConstructor`|Generates a no-argument constructor|
|`@AllArgsConstructor`|Generates a constructor with all fields|
|`@RequiredArgsConstructor`|Generates constructor for final fields and `@NonNull` fields|
|`@Data`|Combines: `@Getter`, `@Setter`, `@ToString`, `@EqualsAndHashCode`, and `@RequiredArgsConstructor`|
|`@Builder`|Enables the **builder pattern**|
|`@Slf4j`|Adds a logger instance named `log`|

---

## 💡 Example: Using Lombok in Entity Class

### ✅ Without Lombok:

```java
public class User {
    private String name;
    private int age;

    public User() {}
    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String toString() {
        return "User{name='" + name + "', age=" + age + "}";
    }
}
```

### ✅ With Lombok:

```java
import lombok.Data;

@Data
@Getter
@Setter
public class User {
    private String name;
    private int age;
}
```

This generates:

- Getters
    
- Setters
    
- `toString()`
    
- `equals()` and `hashCode()`
    
- RequiredArgsConstructor (if fields are final or annotated)
    

---

## 🔨 Using in Spring Boot REST API

Suppose we use `@Data` on an Entity class:

### `User.java`

```java
import lombok.Data;

@Data
public class User {
    private String id;
    private String name;
    private int age;
}
```

### Usage in Controller:

```java
@PostMapping("/users")
public String addUser(@RequestBody User user) {
    System.out.println(user.getName());
    return "User added!";
}
```

➡️ No need to manually define getters, setters, constructors.

---

## 🧠 Summary

- **Lombok** is a **Java annotation-based tool** that reduces **boilerplate code**.
    
- It works at **compile time**, generating methods inside `.class` files.
    
- You must **add it as a dependency** in Maven or Gradle.
    
- IntelliJ requires a **plugin** to recognize the generated methods.
    
- Use annotations like `@Getter`, `@Setter`, or simply `@Data` for full functionality.
    
