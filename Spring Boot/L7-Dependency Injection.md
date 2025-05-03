
## 📘 Detailed Notes: Spring Boot Annotations and Dependency Injection

---

### 🔹 1. Manual Object Creation vs. Dependency Injection

#### ❌ Manual Object Creation (Not Recommended):

```java
Dog dog = new Dog();
```

- Creates a new instance manually.
    
- Not managed by Spring’s IoC container.
    
- Cannot benefit from Spring features like proxying, lifecycle management, or AOP.
    
- In large projects, this leads to unnecessary object creation and tight coupling.
    

---

### ✅ Recommended: Field Injection using `@Autowired`

```java
@Autowired
private Dog dog;
```

#### 🧠 What happens here?

- Spring injects the `Dog` bean automatically.
    
- You **don't need to create the object manually**.
    
- **Dog** must be annotated with `@Component` and be inside the **base package** (or its sub-packages).
    

#### 🔄 Concept:

- **Dependency Injection (DI)**: Asking Spring to provide dependencies instead of creating them yourself.
    
- **Dog** is a _dependency_ of the **Car** class.
    
- **Car depends on Dog**, and Spring injects `Dog` using DI.
    

---

### 📌 Why use Dependency Injection?

- Promotes **loose coupling**.
    
- Reduces **duplicate object creation**.
    
- Improves **testability** and **maintainability**.
    

---

## 🧪 Example with Code and Output

### 🔸 `Dog.java`

```java
package com.example.demo;

import org.springframework.stereotype.Component;

@Component
public class Dog {
    public String fun() {
        return "Dog is having fun!";
    }
}
```

---

### 🔸 `Car.java`

```java
package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Car {

    @Autowired
    private Dog dog;

    @GetMapping("/dog")
    public String getDogFun() {
        return dog.fun();
    }
}
```

---

### 🔸 `DemoApplication.java`

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

### ▶️ Output (on running `localhost:8080/dog`):

```
Dog is having fun!
```

---

## 🧨 What if `@Autowired` is removed?

If you don’t use `@Autowired` and also don’t create a new object:

```java
private Dog dog;

@GetMapping("/dog")
public String getDogFun() {
    return dog.fun(); // NullPointerException
}
```

### ❗ Error:

```
java.lang.NullPointerException: Cannot invoke "Dog.fun()" because "this.dog" is null
```

- Because Spring didn't inject the object.
    
- You also didn’t manually create one.
    
- Result: **NullPointerException**


### private Dog dog=new Dog();

@GetMapping("/dog")
public String getDogFun() {
    return dog.fun(); 
}
now it will work but in every class where ever we need dog object we have to create the instance of Dog object .for that reason we can use one beans for every class.



---

## 🔍 `@SpringBootApplication` Internals

The annotation:

```java
@SpringBootApplication
```

is equivalent to:

```java
@Configuration
@EnableAutoConfiguration
@ComponentScan
```

### 🔸 1. `@ComponentScan`

- Scans the **base package** and all sub-packages for components like:
    
    - `@Component`
        
    - `@Service`
        
    - `@Repository`
        
    - `@Controller` / `@RestController`
        

🔺 **Note**: If your class (e.g., `Dog`) is outside the base package, it won't be scanned.

---

### 🔸 2. `@EnableAutoConfiguration`

- Auto-configures Spring Beans based on dependencies present in the classpath.
    
- Example:
    
    - Add MongoDB dependency → Spring auto-configures a MongoDB connection.
        
    - You only need to add configuration properties in `application.properties`.
        

---

### 🔸 3. `@Configuration`

- Marks a class as a **source of bean definitions**.
    
- Used with methods annotated by `@Bean`.
    

```java
@Configuration
public class AppConfig {

    @Bean
    public Dog dog() {
        return new Dog();
    }
}
```

This also registers a `Dog` bean (alternative to `@Component`).

---

## ✅ Summary

|Feature|Purpose|
|---|---|
|`@Autowired`|Injects dependencies (beans) automatically|
|`@Component`|Marks a class as a bean (managed by Spring)|
|`@RestController`|Specialized `@Component` for web layer|
|`@SpringBootApplication`|Combines 3 core annotations: `@Configuration`, `@EnableAutoConfiguration`, `@ComponentScan`|
|`NullPointerException`|Happens if bean is not created (missing `@Autowired` or `new Dog()`)|
|Field Injection|Using `@Autowired` directly on a class field|
### 🔸 `@Configuration` in Spring

The `@Configuration` annotation marks a class as a **source of bean definitions** for the Spring IoC (Inversion of Control) container.

---

### ✅ Purpose

- It tells Spring:
    
    > “This class contains methods that define beans. Treat them like Spring-managed components.”
    

---

### 📦 Typical Use Case

You use `@Configuration` when you want to manually configure beans using Java code, rather than relying only on annotations like `@Component`.

---

### 🧪 Example

#### ✅ `AppConfig.java`

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public Dog dog() {
        return new Dog();
    }
}
```

#### ✅ `Dog.java`

```java
public class Dog {
    public String fun() {
        return "Dog from @Configuration bean!";
    }
}
```

#### ✅ `Car.java`

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Car {

    @Autowired
    private Dog dog;

    @GetMapping("/dog")
    public String getDogFun() {
        return dog.fun();
    }
}
```

---

### 📌 Key Points

| Aspect           | Description                                                                          |
| ---------------- | ------------------------------------------------------------------------------------ |
| `@Configuration` | Declares the class as a Java-based config class                                      |
| `@Bean`          | Used inside a `@Configuration` class to define a bean .it can used only for methods  |
| Result           | Spring registers the return value of the `@Bean` method as a bean                    |

---

### 🔄 Compared to `@Component`

|`@Component`|`@Configuration`|
|---|---|
|Placed **on class** to register it as a bean|Placed **on class** that defines beans via methods|
|Automatic and simple|More **explicit** and **flexible**|
|Cannot customize object creation|Full control over bean creation (you write the logic)|
