
## **1. Docker Setup on Windows (and Mac)**

- **Installed Docker Desktop** on Windows.
    
- Option to **sign in** with a Docker Hub account:
    
    - Useful if you want to **sync settings across machines**.
        
    - Skipped sign-in in this case.
        
- **Docker Desktop started successfully** → Engine is running.
    
- **Key tabs in Docker Desktop**:
    
    - **Containers** → Shows running/stopped containers.
        
    - **Images** → Lists all downloaded images.
        
    - **Volumes** → Displays storage volumes (persistent data).
        

---

## **2. Testing Docker Installation**

- Verified Docker installation via command:
    
    ```bash
    docker version
    ```
    
    - Output showed version **24.0.x** confirming installation success.
        
- Important note: after installing Docker, sometimes **you must restart CMD/Terminal** for commands to work.
    

---

## **3. Pulling and Running Images**

### **a) Pulling a Linux (Ubuntu) image**

- Searched for **Ubuntu** image in Docker Desktop.
    
- Clicked **Pull** to download it.
    
- Size of Ubuntu image: **~77 MB** (lightweight for an OS image).
    

---

### **b) Understanding Images vs Containers**

- **Images** = Blueprint (instructions and base files).
    
- **Containers** = Running instance of that image.
    
- **Images are lightweight**, but containers consume more resources while running.
    

---

## **4. Pulling and Running MySQL**

- Searched for **MySQL** in Docker Desktop.
    
- Found:
    
    - **Official image** → Preferred for stability and support.
        
    - **Verified/Community images** → Not always safe or consistent.
        
- Pulled the **official MySQL image**.
    
- Observations:
    
    - Download was slower (MySQL is a heavier image).
        
    - Attempted to run it directly but it exited (likely due to missing configuration like root password).
        
    - Could not delete the image while a container was running → had to **delete container first**, then the image.
        

---

## **5. Using Command Line**

- Moved from Docker Desktop GUI to **command line usage** for better control.
    
- Verified Docker version again:
    
    ```bash
    docker version
    ```
    

---

## **6. Pulling and Running "Hello World" Image**

- **Why Hello World?**  
    Lightweight (only ~13 KB), perfect for testing.
    
- Command to pull and run:
    
    ```bash
    docker run hello-world
    ```
    
- Process:
    
    - Docker **checked for local image** (not found).
        
    - Pulled the **latest image** from Docker Hub automatically.
        
    - Ran the container and displayed confirmation output.
        
- Verification:
    
    - **Docker Desktop → Images** → `hello-world` listed.
        
    - **Docker Desktop → Containers** → Running instance named randomly (e.g., `nice_bog`).
        

---

## **7. Introduction to Docker Hub**

- **Docker Hub**:
    
    - A central **remote registry** for Docker images.
        
    - Includes:
        
        - **Official images** (maintained by Docker team).
            
        - **Verified publisher images** (trusted third parties).
            
        - **Community images** (use with caution).
            
- Examples of popular images:
    
    - `nginx`, `mysql`, `mongo`, `node`, `ubuntu`, etc.
        
- You can also **push your own custom images** to Docker Hub.
    

---

## **8. Key Learnings**

|**Concept**|**Key Insight**|
|---|---|
|**Docker Desktop**|GUI to manage containers, images, and volumes.|
|**Docker Engine**|Core service that runs and manages containers.|
|**Images**|Templates for containers; lightweight and reusable.|
|**Containers**|Live, running instances of images; heavier than images.|
|**Docker Hub**|Remote repository for pulling and pushing images.|
|**Commands**|CLI gives faster, more flexible control than GUI.|
|**Persistence**|Containers are ephemeral by default — volumes needed for data persistence.|

---

## **Useful Commands Recap**

|**Command**|**Purpose**|
|---|---|
|`docker version`|Check Docker installation version.|
|`docker pull <image>`|Download an image without running it.|
|`docker run <image>`|Pull (if needed) and run a container.|
|`docker ps -a`|List all containers (running or stopped).|
|`docker images`|List downloaded images.|
|`docker rm <container_id>`|Remove a container.|
|`docker rmi <image_id>`|Remove an image.|

---

## **Common Issues Observed**

1. **Command not found initially** → Needed CMD restart.
    
2. **Container exits unexpectedly** → Often due to missing configuration or incorrect run command.
    
3. **Image deletion blocked** → Must remove containers using that image first.


### Question ? The **“hello-world” Docker image** is mainly a **test image** that verifies your Docker setup is working correctly.

Here’s **why you need to pull and run it**:

---

### **1. To Verify Docker Installation**

After installing Docker, you run:

```bash
docker run hello-world
```

What happens:

- Docker checks if the `hello-world` image is present locally.
    
- If not, it **pulls the image** from Docker Hub.
    
- It runs a tiny container that prints a confirmation message.
    

This confirms:

- Docker Engine is running correctly
    
- Your system can connect to **Docker Hub**
    
- Containers can start and run
    

---

### **2. To Check Networking & Permissions**

Running `hello-world` ensures:

- You have internet access to Docker Hub
    
- The Docker daemon can pull images
    
- Your user account has permission to interact with Docker
    

---

### **3. First-Time Image Pull Test**

Since most Docker workflows start by pulling a base image (`node`, `python`, `alpine`, etc.), running `hello-world` first ensures that **the pulling process works fine** before you work on real projects.

---

### **4. It's a Minimal Image**

- The image is **tiny (less than 20KB)**.
    
- Downloads quickly, making it ideal for initial setup tests.
    

---

### **Output Example**

When you run:

```bash
docker run hello-world
```

You’ll see something like:

```
Hello from Docker!
This message shows that your installation appears to be working correctly.
...
```

---

### **Key Points**

- You don’t **need** `hello-world` for actual development.
    
- It’s only used **once or occasionally** to verify Docker’s installation and connectivity.
    
- After that, you typically work with real images like `node`, `python`, or `mysql`.
    
# commands 
	
## **1. Verifying Docker Installation**

- After running:
    
    ```bash
    docker run hello-world
    ```
    
    Docker:
    
    - Checks if the `hello-world` image exists locally
        
    - Pulls it from Docker Hub if not present
        
    - Runs the container, prints confirmation text, and exits
        
- This confirms:
    
    - Docker Engine is installed and running
        
    - Docker Hub connectivity is working
        
    - Containers can start and execute commands
        

---

## **2. Viewing Images and Containers**

### **View Images**

```bash
docker images
```

- Lists all images available locally.
    
- Shows:
    
    - **Repository** (image name)
        
    - **Tag** (like `latest`)
        
    - **Image ID**
        
    - **Created time**
        
    - **Size**
        

### **View Running Containers**

```bash
docker ps
```

- Shows **active containers** only.
    

### **View All Containers (including stopped ones)**

```bash
docker ps -a
```

- Lists **running and stopped** containers.
    
- Useful for seeing containers that executed and exited, like `hello-world`.
    

---

## **3. Container Naming**

- Docker assigns **random names** (like `nice_bog`) to containers if you don’t specify one.
    
- You can name a container explicitly:
    
    ```bash
    docker run --name my_hello hello-world
    ```
    

---

## **4. Deleting Containers and Images**

### **Remove a Container**

1. Check container ID:
    
    ```bash
    docker ps -a
    ```
    
2. Remove it:
    
    ```bash
    docker rm <container_id>
    ```
    

### **Remove an Image**

1. Make sure no containers are using it:
    
    ```bash
    docker rm <container_id>
    ```
    
2. Then remove the image:
    
    ```bash
    docker rmi <image_id>
    ```
    

---

## **5. Understanding `docker help`**

Running:

```bash
docker help
```

Shows commonly used commands:

- `docker run` – run a container
    
- `docker ps` – list containers
    
- `docker images` – list images
    
- `docker pull` – download image
    
- `docker push` – upload image
    
- `docker rm` – remove containers
    
- `docker rmi` – remove images
    
- `docker search` – search Docker Hub
    
- `docker info` – display system-wide info
    
- `docker version` – show version details
    

---

## **6. Step-by-Step Workflow**

If you want to manage containers **manually**:

|Step|Command|Purpose|
|---|---|---|
|**1. Search Image**|`docker search hello-world`|Check if image exists on Docker Hub|
|**2. Pull Image**|`docker pull hello-world`|Download image from Docker Hub|
|**3. Create Container**|`docker create hello-world`|Create a container without running it|
|**4. Start Container**|`docker start <container_id>`|Start the created container|
|**5. Run and Create in One Step**|`docker run hello-world`|Combines pull, create, and start in one command|

---

## **7. Container & Image IDs**

- **Container IDs** change **every time** you run a container.
    
- **Image IDs** remain the same until you delete and re-pull the image.
    

---

## **8. Key Cleanup Commands**

- Remove all **stopped containers**:
    
    ```bash
    docker container prune
    ```
    
- Remove all **unused images**:
    
    ```bash
    docker image prune
    ```
    

---

## **Key Takeaways**

- `docker run hello-world` = test your setup.
    
- Use `docker ps` and `docker images` to check what’s running and installed.
    
- Always remove containers before removing images to avoid dependency errors.
    
- Manual steps (`search → pull → create → start`) help you understand the process that `docker run` automates.
    
- Naming containers makes it easier to manage multiple containers.


# Architecture 
# **Docker Detailed Notes**

---

## **1. Container Creation and IDs**

- When you **create** a container using:
    
    ```bash
    docker create <image-name>
    ```
    
    - Docker assigns a **long unique ID** to the container.
        
    - In most commands, you don’t need the full ID — the **first few characters** are enough as long as they are unique.
        
- Example:
    
    ```bash
    docker create hello-world
    ```
    
    Output: `Created container <LONG_ID>`
    
- You can refer to the container using:
    
    ```bash
    docker start <container-short-id>
    ```
    

---

## **2. Running and Stopping Containers**

- **Start a container:**
    
    ```bash
    docker start <container-id>
    ```
    
- **Check running containers:**
    
    ```bash
    docker ps
    ```
    
- **Check all containers (including stopped):**
    
    ```bash
    docker ps -a
    ```
    
- **Stop a container:**
    
    ```bash
    docker stop <container-id>
    ```
    
- **Pause a container:**  
    Works only if the container is running.
    
    ```bash
    docker pause <container-id>
    ```
    
- **Remove a container:**
    
    ```bash
    docker rm <container-id>
    ```
    

---

## **3. Understanding `docker run`**

- The `docker run` command **combines multiple steps**:
    
    - Pull the image (if not available locally)
        
    - Create the container
        
    - Start the container
        
- Equivalent manual steps:
    
    1. `docker pull <image>`
        
    2. `docker create <image>`
        
    3. `docker start <container-id>`
        

---

## **4. Docker Architecture**

Docker is made up of **several components**:

|**Component**|**Description**|
|---|---|
|**Docker Client**|The CLI or GUI (Docker Desktop) that you interact with.|
|**Docker Daemon (dockerd)**|Background process that handles images, containers, volumes, and networks.|
|**Docker API**|Communication layer between client and daemon.|
|**Images**|Templates used to create containers.|
|**Containers**|Running instances of images.|
|**Networks**|Enable communication between containers and external systems.|
|**Volumes**|Persistent storage for containers.|
|**Registry**|Storage for images. Can be **public** (like Docker Hub) or **private**.|

### **Flow of Commands**

1. You type a command in **Docker Client**.
    
2. The request goes to the **Docker API**.
    
3. **Docker Daemon** performs the required action (pull image, run container, etc.).
    
4. If the image is not available locally:
    
    - Daemon fetches it from the **registry** (e.g., Docker Hub).
        

---

## **5. Using JDK in Docker**

### **Why use JDK in Docker**

- Standardized development environment.
    
- Avoid installing JDK on every machine.
    
- Share containers with all dependencies included.
    

### **Steps to Get JDK Image**

1. **Search for OpenJDK image**
    
    ```bash
    docker search openjdk
    ```
    
    Shows official OpenJDK images and their details.
    
2. **Pull the image**
    
    ```bash
    docker pull openjdk
    ```
    
3. **Run a container with JDK**
    
    ```bash
    docker run -it openjdk
    ```
    
    `-it` makes it interactive so you can run commands inside.
    
4. **Use JShell inside the container**
    
    ```bash
    jshell
    ```
    
    Example:
    
    ```java
    int num = 9;
    System.out.println("Hello World");
    ```
    

---

## **6. Key Commands Recap**

|**Task**|**Command**|
|---|---|
|Search for an image|`docker search <name>`|
|Pull an image|`docker pull <name>`|
|Create a container|`docker create <name>`|
|Start a container|`docker start <id>`|
|Run container (all-in-one)|`docker run <name>`|
|List running containers|`docker ps`|
|List all containers|`docker ps -a`|
|Stop a container|`docker stop <id>`|
|Pause a container|`docker pause <id>`|
|Remove container|`docker rm <id>`|
|Remove image|`docker rmi <id>`|
|Prune stopped containers|`docker container prune`|
|Prune unused images|`docker image prune`|

---

## **7. Key Learnings**

- **Partial IDs** can be used for convenience.
    
- `docker run` is just a shortcut for multiple commands.
    
- **Docker Daemon** is the main engine that handles all container operations.
    
- Using Docker for tools like JDK ensures consistent environments across machines.
    

---

Would you like me to **create a visual diagram** of Docker architecture from these notes to help you memorize it better?
