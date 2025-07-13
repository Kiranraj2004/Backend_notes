
## 📘 Spring Boot Project Structure Explained

### 🛠️ Project Creation Method

- Created using **Spring Initializr**.
    
- The presence of `pom.xml` indicates it is a **Maven project**.
    
- If Maven is not installed locally, a **Maven Wrapper (`.mvn`)** can be used.
    

---

## 📁 Project Structure Breakdown

### `.idea/`

- IntelliJ-specific config folder.
    
- Not related to Spring Boot; **can be ignored**.
    
- Mentioned in `.gitignore` to avoid committing.
    

### `.mvn/`

- Contains Maven Wrapper files.
    
- Allows Maven build commands without Maven being installed globally.
    

### `.gitignore`

- Lists files/folders that shouldn't be committed to Git (e.g., `.idea/`, `target/`).
    
- Helps keep the repo clean from IDE/config or build artifacts.
    

---

## 📦 `src/` Directory Structure

### `src/main/java/`

- Contains **main Java code**.
    
- Entry point class (e.g., `MyFirstProjectApplication.java`) resides here.
    

### `src/main/resources/`

- Holds **application configuration and static resources**:
    
    - `application.properties`: for configuration (e.g., server ports, DB config).
        
    - `static/`: static files like images, JS, CSS.
        
    - `templates/` (if using): for Thymeleaf/Freemarker HTML templates.
        

### `src/test/java/`

- Contains **unit and integration test code**.
    
- You can test your service/controller logic here.
    

---

## 📄 `pom.xml` - Project Object Model File

- Central config file for Maven-based project.
    
- Manages:
    
    - Project metadata
        
    - Dependencies
        
    - Plugins
        
    - Build configurations
        

---

## 🧱 Key `pom.xml` Sections

### `<project>`

- Root element; wraps the entire structure.
    

### `<modelVersion>`

- Declares the version of the POM model being used.
    

### `<groupId>`, `<artifactId>`, `<version>`, `<description>`

- Metadata provided while creating the project in Spring Initializr.
    

### `<parent>`

- Inherits from **`spring-boot-starter-parent`**:
    
    - Simplifies dependency and plugin management.
        
    - Brings predefined versions for plugins and dependencies.
        
    - Here, version is `2.7.15`.
        

### `<dependencies>`

- External libraries required by the project (e.g., web, JPA, test starters).
    
- Fetched from Maven Central.
    

### `<build>` → `<plugins>`

- Specifies build-related plugins.
    
- One key plugin: **Spring Boot Maven Plugin**:
    
    - Packages the project into a **"fat JAR"** (self-contained).
        
    - Plugin handles creation of `original-xxx.jar` and the final executable jar.
        

---

## 🧰 Maven Build Commands Explained

### `mvn clean`

- Cleans the previous builds and deletes the `target/` directory.
    

### `mvn package`

- Compiles the code and packages it as a **JAR file**:
    
    - Creates:
        
        - `original-xxx.jar` → only compiled code.
            
        - `xxx.jar` → **fat jar** with code + dependencies.
            
    - The **fat jar** is self-contained:
        
        - Includes embedded Tomcat server.
            
        - No need for an external server.
            
        - Can be run with `java -jar xxx.jar`.
            

---

## 🧩 Concepts Introduced

- **Fat JAR**: Executable JAR including all dependencies and embedded server.
    
- **Original JAR**: Contains only your compiled classes (no dependencies).
    
- **Self-contained Application**: No need to install Tomcat separately.
    
- **Java Compiler Plugin**: Ensures the project compiles with a specific Java version (e.g., 1.8), defined using:
    
    ```xml
    <properties>
        <java.version>1.8</java.version>
    </properties>
    ```
    

---

## 📌 Key Takeaways

- Maven wrapper and `pom.xml` make the build process flexible and portable.
    
- Spring Boot’s **starter parent** simplifies dependency and plugin setup.
    
- The project can be packaged into a **standalone executable JAR**.
    
- Understanding this structure is crucial before starting with actual coding.
    


### 🧱 What is a Fat JAR?

**Fat JAR (also called Uber JAR)** is a single **executable JAR file** that contains:

- Your **compiled Java code** (classes)
    
- All **dependencies** (external libraries)
    
- An **embedded server** (like Tomcat or Jetty, for Spring Boot)
    

#### ✅ Key Benefits:

- **Self-contained**: No need to install external servers or libraries.
    
- **Portable**: Just run it anywhere with `java -jar`.
    
- **Ideal for production**: Simple deployment process.
    

#### 📦 Example:

After `mvn package`, Maven creates:

- `myproject-1.0.0.jar` → This is the **Fat JAR**
    
- `original-myproject-1.0.0.jar` → Just your code, no dependencies
    

---

### 🔁 What is Repackaging?

**Repackaging** is the process of:

1. Building the original JAR with only your classes.
    
2. **Adding dependencies and required metadata** into that JAR.
    
3. Creating a new **Fat JAR** using the original one as the base.
    

This process is automatically handled by the **Spring Boot Maven Plugin**, usually defined in `pom.xml` like this:

```xml
<build>
  <plugins>
    <plugin>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-maven-plugin</artifactId>
    </plugin>
  </plugins>
</build>
```

---

### ⚙️ Summary:

|Term|Meaning|
|---|---|
|**Fat JAR**|A JAR file with your app + all dependencies + embedded server|
|**Repackaging**|The Maven process that takes your app JAR and wraps it with dependencies|
### 📦 What is a JAR in Java?

**JAR** stands for **Java ARchive**.  
It is a **compressed file format** (like `.zip`) used to bundle:

- **.class** files (compiled Java code)
    
- **resources** (images, text files, properties)
    
- **metadata** (like `MANIFEST.MF`)
    

Into a single file with a `.jar` extension.

---

### ✅ Why Use a JAR?

|Purpose|Benefit|
|---|---|
|Package many class files|Easy to distribute Java applications|
|Include libraries/resources|All-in-one packaging|
|Share reusable libraries|Others can use your code as a dependency|
|Create executable programs|Run with `java -jar`|

---

### 🔨 How to Create a JAR File

```bash
jar cf MyApp.jar *.class
```

- `c` = create
    
- `f` = specify file name
    
- `*.class` = files to include
    

You can also include packages or resources like this:

```bash
jar cf MyApp.jar com/myapp/*.class config.properties
```

---

### 🏃 How to Run an Executable JAR

If your JAR includes a `Main-Class` in the manifest:

```bash
java -jar MyApp.jar
```

---

### 📝 Manifest File (META-INF/MANIFEST.MF)

A JAR can include a special file called `MANIFEST.MF` that contains metadata, like:

```
Main-Class: com.myapp.Main
```

You can specify this when creating a JAR:

```bash
jar cfm MyApp.jar manifest.txt *.class
```

Where `manifest.txt` contains:

```
Main-Class: com.myapp.Main
```

---

### 📁 Example JAR Structure

```
MyApp.jar
├── META-INF/
│   └── MANIFEST.MF
├── com/
│   └── myapp/
│       ├── Main.class
│       └── Utils.class
└── config.properties
```

---

### 🧪 Example: Hello World JAR

#### Hello.java

```java
public class Hello {
    public static void main(String[] args) {
        System.out.println("Hello from JAR!");
    }
}
```

#### Compile:

```bash
javac Hello.java
```

#### Create Manifest:

`manifest.txt`:

```
Main-Class: Hello
```

#### Create JAR:

```bash
jar cfm Hello.jar manifest.txt Hello.class
```

#### Run:

```bash
java -jar Hello.jar
```

🖨️ Output:

```
Hello from JAR!
```

---
