### 🧱 What is the **MVC Pattern**?

**MVC** stands for **Model–View–Controller** — it's a **design pattern** used to separate an application into three interconnected components:

---

### 📦 1. **Model** – _Data & Business Logic_

- Represents the **data**, **logic**, and **rules** of the application.
    
- Interacts with the **database** (e.g., fetching, saving).
    
- Doesn't care how the data is displayed.
    

🧠 Think: _"What the app does and stores."_

#### Example:

```java
public class User {
    private String name;
    private String email;
    // Getters and setters
}
```

---

### 🖼️ 2. **View** – _UI / Presentation Layer_

- Represents the **UI (User Interface)**.
    
- Displays the **model data** to the user.
    
- Accepts user input (like form submissions).
    

🧠 Think: _"What the user sees."_

#### Example (HTML/Thymeleaf):

```html
<h1>Welcome, ${user.name}!</h1>
```

---

### 🎮 3. **Controller** – _Input Logic_

- Acts as a **bridge** between View and Model.
    
- Accepts **user requests**, processes them (often with help from the model), and returns a **response/view**.
    

🧠 Think: _"What happens when the user clicks something."_

#### Example (Spring Boot Controller):

```java
@RestController
public class UserController {
    @GetMapping("/user")
    public User getUser() {
        return new User("Alice", "alice@example.com");
    }
}
```

---

### 🔁 How MVC Works (Flow):

1. **User** sends request (e.g., `/user`)
    
2. **Controller** handles it and asks **Model** for data.
    
3. **Model** provides data (e.g., from DB).
    
4. **Controller** sends data to the **View**.
    
5. **View** renders it to the **user**.
    

---

### 📐 Benefits of MVC:

✅ **Separation of concerns**  
✅ Easier to **test**, **maintain**, and **scale**  
✅ Developers can work in **parallel** (UI team on View, backend on Model)

---

### 🚀 MVC in Spring Boot

Spring Boot supports MVC using **Spring MVC Framework**.

#### Key Annotations:

|Annotation|Role|
|---|---|
|`@Controller`|Marks a Controller|
|`@RestController`|Controller + JSON response|
|`@GetMapping`, `@PostMapping`, etc.|Maps URLs to methods|
|`@Service`|Business logic|
|`@Repository`|Database access|
|`@ModelAttribute`|Bind data to form/view|

---

### 🔄 Real-Life Analogy:

|Role|In MVC|
|---|---|
|Waiter|Controller|
|Chef/Kitchen|Model (business logic)|
|Menu/Plated Dish|View (what you see)|
