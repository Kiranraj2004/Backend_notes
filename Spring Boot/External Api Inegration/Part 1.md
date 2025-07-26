Here's a **detailed note with code examples and explanations** based on the transcript you provided. The topic is **making external API calls in a Spring Boot application using `RestTemplate`**, specifically to:

- A **Quotes API** (which failed due to authentication issues)
    
- A **Weather API** (which worked and returned data like temperature and humidity)
    

---

## ✅ **Overview**

In this video, the instructor explains how to consume external REST APIs using Java code in a Spring Boot application. Instead of using tools like Postman, the goal is to automate this process in a Spring Boot service using `RestTemplate`.

---

## ✅ **External APIs Used**

### 1. **Quotes API**

- Requires an API key.
    
- Limited number of free requests (e.g., 5/day).
    
- Couldn’t be used successfully due to email verification or token issues.
    

### 2. **Weather API**

- Also requires an API key.
    
- Free tier allows a decent number of requests.
    
- Works without HTTPS (only HTTP).
    
- Returns weather information like temperature, humidity, etc.
    

---

## ✅ **Setup in Spring Boot Project**

### Project Structure

- `UserController`: Handles the `/greetings` GET endpoint.
    
- `WeatherService`: Service to fetch weather data.
    
- `QuoteService` _(planned)_: Would fetch quotes but was skipped due to token issues.
    

---

## ✅ **Step-by-step Implementation**

### 1. **Create WeatherService**

#### 🔧 Define API Endpoint Template

```java
@Component
public class WeatherService {

    private static final String API_KEY = "your_api_key_here";

    private final RestTemplate restTemplate;
%% we can autowire this before that we need to create it as beans in main method file    or else initalize the object here  like below%%

    public WeatherService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getWeather(String city) {
        String urlTemplate = "http://api.weatherstack.com/current?access_key={apiKey}&query={city}";

        // Replace placeholders
        String finalUrl = urlTemplate
                .replace("{apiKey}", API_KEY)
                .replace("{city}", city);

        // Make the GET request
        ResponseEntity<String> response = restTemplate.exchange(
                finalUrl,
                HttpMethod.GET,
                null,
                String.class
        );

        return response.getBody();
    }
}
```

---

### 2. **Enable `RestTemplate` Bean**

In your Spring Boot configuration class or a separate config class:

```java
@Configuration
public class AppConfig {
    @Bean
	    public RestTemplate restTemplate() {
	        return new RestTemplate();
	    }
}
```

---

### 3. **Use in Controller**

```java
@RestController
public class UserController {

    private final WeatherService weatherService;

    public UserController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/greetings")
    public ResponseEntity<String> greetUser(@RequestParam String name, @RequestParam String city) {
        String greeting = "Hi " + name + "!";
        String weather = weatherService.getWeather(city);
        return ResponseEntity.ok(greeting + "\n" + weather);
    }
}
```

---

## ✅ **What is `RestTemplate`?**

- A Spring-provided class for **client-side HTTP communication**.
    
- Supports HTTP methods like `GET`, `POST`, `PUT`, `DELETE`.
    
- Automatically handles conversion between HTTP request/response and Java objects.
    

### Example:

```java
RestTemplate restTemplate = new RestTemplate();
String result = restTemplate.getForObject("https://api.example.com", String.class);
```

---

## ✅ **Key Concepts Covered**

|Concept|Explanation|
|---|---|
|**API Key Usage**|Token required to authenticate with the external service. Passed as a query parameter in the URL.|
|**URL Construction**|Dynamically build the URL with city and API key using `.replace()`|
|**RestTemplate.exchange()**|General method for HTTP calls. Takes URL, method, entity, and return type.|
|**@Component Service**|The `WeatherService` is marked as a Spring component so it can be autowired in the controller.|
|**HTTP vs HTTPS**|Free plan of Weather API doesn't support HTTPS, so REST calls must use HTTP.|

---

## ✅ **Sample Output**

When you hit the endpoint:

```
GET /greetings?name=Vipul&city=Mumbai
```

The output will be something like:

```
Hi Vipul!
{"location":{"name":"Mumbai","country":"India"},"current":{"temperature":32,"humidity":78,...}}
```

To parse only temperature from JSON, you'd typically use:

- `ObjectMapper` (Jackson)
    
- Or define a POJO (Plain Old Java Object)
    

---

## ✅ **Next Steps (if you wish to expand):**

1. **Use DTOs to map API responses** (instead of plain strings).
    
2. **Add error handling** (`try-catch`, check status codes).
    
3. **Log external API failures.**
    
4. **Secure API key using application.properties/yml instead of hardcoding.**
    
5. **Add caching** for frequent queries (e.g., weather data doesn’t change every second).
    

---

## ✅ **Conclusion**

This video demonstrated how to:

- Register for external APIs
    
- Get API keys
    
- Use `RestTemplate` in Spring Boot to call those APIs
    
- Integrate the response with your existing controller logic
    




Great question!

When you write:

```java
@Bean
public RestTemplate restTemplate() {
    return new RestTemplate();
}
```

You're **registering `RestTemplate` as a Spring-managed bean**, so Spring can automatically **inject (autowire)** it into your service classes **instead of creating a new instance manually** each time.

---

### ✅ Why We Do This

Let’s break it down:

---

### 🔹 1. **Spring Manages the Lifecycle**

By declaring `RestTemplate` as a `@Bean`, you're telling Spring:

> “Hey Spring, please create and manage a single instance of `RestTemplate` for me.”

Spring will **reuse this same object** wherever it is `@Autowired`.

---

### 🔹 2. **Avoid Manual Instantiation**

If you don’t register it as a bean, you’d have to manually create it like this:

```java
RestTemplate restTemplate = new RestTemplate(); // not ideal
```

You'd do this in every class you want to use it. That’s repetitive and doesn't follow dependency injection principles.

---

### 🔹 3. **Supports Dependency Injection**

Once registered, you can inject it:

```java
@Autowired
private RestTemplate restTemplate;
```

OR using constructor injection (preferred):

```java
public WeatherService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
}
```

---

### 🔹 4. **Easy to Customize**

Once it's a bean, you can **customize the `RestTemplate`** in one place — for example, add:

- Interceptors
    
- Message converters
    
- Timeout settings
    
- Error handlers
    

Example:

```java
@Bean
public RestTemplate restTemplate() {
    RestTemplate restTemplate = new RestTemplate();

    // Example: Add custom timeout or interceptors here

    return restTemplate;
}
```

---

### 🔹 5. **Best Practice in Spring Applications**

It follows **Inversion of Control (IoC)** and **Dependency Injection (DI)**, which are the core principles of Spring Framework.

---

### ✅ Summary

|Without Bean|With Bean (`@Bean`)|
|---|---|
|`new RestTemplate()` manually|Spring creates and injects it automatically|
|No sharing of instance|Singleton bean shared across app|
|No customization unless repeated everywhere|Customizable in one central config|

---

So, by writing:

```java
@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
```

