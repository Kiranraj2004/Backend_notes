
## ✅ **Maven in Spring Boot: Detailed Notes**

---

### 🔹 **What is Maven?**

- **Maven** is a **build automation tool**.
    
- It is mainly used for:
    
    - **Creating builds** (compiling, packaging, etc.)
        
    - **Managing dependencies** (external libraries)
        

---

### 🔹 **Dependency Management with Maven**

- Suppose you want to use a library (like **OpenCSV**) in your Java project:
    
    - Without Maven:
        
        - Manually download the `.jar` file
            
        - Import it into the project
            
    - With Maven:
        
        - Just add the dependency in `pom.xml`
            
        - Maven automatically downloads the required JAR from the **Maven Central Repository**
            
        - The library gets added to your project without manual work
            
- **Maven Repository**:
    
    - A central storage of thousands of libraries (like OpenCSV)
        
    - You search for your required library, copy the Maven dependency tag, and paste it into your `pom.xml`
        
- **Dependency Coordinates**:
    
    - Every dependency has 3 important values:
        
        - `Group ID`: Organization/Company name (e.g., `com.opencsv`)
            
        - `Artifact ID`: Library name (e.g., `opencsv`)
            
        - `Version`: Version number (e.g., `5.7.1`)
            

---

### 🔹 **Build Lifecycle in Maven**

Maven defines a **build lifecycle** composed of several phases:

|Phase|Description|
|---|---|
|`validate`|Checks if the `pom.xml` is correct|
|`compile`|Compiles the Java source code|
|`test`|Runs unit tests (if any)|
|`package`|Creates a `.jar` or `.war` file|
|`verify`|Runs integration tests, if any|
|`install`|Installs the built `.jar` into your local `.m2` repository|
|`deploy`|Deploys the final build to a remote repository (used in CI/CD)|

---

### 🔹 **Commands to Use Maven**

You can run the following commands using your terminal (in the project directory):

|Command|Purpose|
|---|---|
|`mvn validate`|Validates the project structure and `pom.xml`|
|`mvn compile`|Compiles the source code|
|`mvn test`|Runs unit tests|
|`mvn package`|Creates `.jar` or `.war` file inside the `target` folder|
|`mvn install`|Installs the `.jar` into the local repository (`.m2` folder)|
|`mvn deploy`|Deploys to a remote repo (often used in DevOps pipelines)|
|`mvn clean`|Deletes the `target` folder (removes previous build data)|

- **Shortcut**:
    
    - `mvn package` will automatically perform `validate`, `compile`, and `test` before creating the `.jar`.
        

---

### 🔹 **What is Maven Wrapper?**

- If Maven is **not installed** on your system, you can still build the project using the **Maven Wrapper** (`mvnw` or `mvnw.cmd` for Windows).
    
- The wrapper is included automatically when you create a project via Spring Initializr.
    
- Usage:
    
    ```bash
    ./mvnw package   # Linux/Mac
    mvnw package     # Windows
    ```
    

---

### 🔹 **.m2 Local Repository**

- Location: Usually in `~/.m2/repository/`
    
- Maven stores all downloaded dependencies (like `opencsv`) here.
    
- When reused in other projects, Maven fetches the jars from `.m2` instead of downloading again—**offline reuse**.
    

---

### 🔹 **Running the Final Jar**

- After running `mvn package`, the `.jar` will be created in the `target` folder.
    
- Run it using:
    
    ```bash
    java -jar target/your-jar-name.jar
    ```
    
- **Spring Boot jars** are self-contained (embedded Tomcat), so no need for external Tomcat installation.
    

---

### 📌 Summary

|Feature|Explanation|
|---|---|
|**Maven**|Tool to automate build and manage dependencies|
|**Dependencies**|External libraries added via `pom.xml`|
|**Phases**|Validate → Compile → Test → Package → Install/Deploy|
|**Wrapper**|Used when Maven is not installed locally|
|**.m2 Repository**|Local cache of downloaded libraries|
|**Jar Execution**|Easily run Spring Boot apps via `java -jar`|
