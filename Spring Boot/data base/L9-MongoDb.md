

## 🧩 What is **MongoDB**?

**MongoDB** is a **NoSQL, document-oriented database** used for storing data in a **flexible, JSON-like format** instead of traditional rows and columns like in relational databases (e.g., MySQL, PostgreSQL).

---

## 🔑 Key Features of MongoDB:

|Feature|Description|
|---|---|
|**NoSQL**|It does **not** use SQL tables. Instead, it stores data in **collections of documents**.|
|**Document-based**|Each record is stored as a **document** in **JSON format** (technically BSON - Binary JSON).|
|**Schema-less**|Documents in the same collection **can have different structures**. You don’t have to define a fixed schema upfront.|
|**Scalable**|MongoDB is designed to scale easily across multiple servers (horizontal scaling).|
|**High performance**|Optimized for **fast reads/writes**, especially for large volumes of unstructured data.|

---

## 🧱 MongoDB vs SQL (Example)

### SQL Table (Relational):

```sql
Table: users
+----+--------+-----+
| id | name   | age |
+----+--------+-----+
| 1  | Alice  | 25  |
| 2  | Bob    | 30  |
+----+--------+-----+
```

### MongoDB Collection (NoSQL):

```json
Collection: users
[
  { "_id": 1, "name": "Alice", "age": 25 },
  { "_id": 2, "name": "Bob", "age": 30 }
]
```

---

## 🧠 Terminology Comparison:

|SQL|MongoDB|
|---|---|
|Database|Database|
|Table|Collection|
|Row|Document|
|Column|Field|
|Primary Key|`_id` (default)|

---

## ✅ When to Use MongoDB

- When your data is **unstructured or semi-structured**
    
- When you need **scalability and high performance**
    
- When your app’s structure evolves frequently (e.g., agile development)
    
- Ideal for **Spring Boot REST APIs**, real-time analytics, IoT, etc.
    
## 💻 **MongoDB Basic Commands Explained**

### 1. **Start MongoDB Shell**

Run MongoDB on your system using its shell (`mongo` or `mongosh`) depending on your OS.

### 2. **Show Available Databases**

shell

CopyEdit

`show dbs`

### 3. **Use a Database**

If it doesn’t exist, MongoDB will create it when you insert something:

shell

CopyEdit

`use school`

### 4. **Show Collections**

shell

CopyEdit

`show collections`

### 5. **Insert a Document**

shell

CopyEdit

`db.students.insertOne({ name: "Ram", age: 20 }) db.students.insertOne({ name: "Shyam", age: 200 })`

- `insertOne()` adds a document.
    
- `students` is the collection name.
    

### 6. **Find Documents**

shell

CopyEdit

`db.students.find()           // Shows all documents db.students.find().pretty()  // Pretty-printed output db.students.find({ name: "Ram" }) // Filter by field`

### 7. **Delete Documents**

shell

CopyEdit

`db.students.deleteOne({ name: "Ram" })`