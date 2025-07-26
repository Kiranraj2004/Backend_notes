
### 🔧 **Objective**

Learn how to **send emails programmatically in Spring Boot** using **JavaMailSender**, by configuring Gmail SMTP server and using a **secure app password**.

---

## 🧱 Prerequisites

### ✅ JavaMailSender Dependency

Add this dependency to your `pom.xml` to use email functionality in Spring Boot:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-mail</artifactId>
</dependency>
```

Then reload Maven to download the dependencies.

---

### 📁 Project Structure

- Create a new service: `EmailService.java` inside the `service` package.
    
- Later, you can write unit tests in `EmailServiceTest.java`.
    

---

## ⚙️ Spring Boot Email Configuration

In your `application.yml` (or `application.properties`), add the following configuration:

```yaml
spring:
  mail:
    host: smtp.gmail.com
    port: 587
    username: your-email@gmail.com
    password: your-app-password
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
```

### 🔐 About App Password

- **DO NOT use your personal Gmail password.**
    
- Instead, **enable 2-step verification** and create an **App Password**.
    
- Steps:
    
    1. Go to Google Account → Security → 2-Step Verification → Enable it.
        
    2. After enabling, you will see an **App Password** option below.
        
    3. Choose "Mail" → "Other (Custom)" → Name it like `JavaMail` → Generate.
        
    4. You’ll get a **16-character password**, use it in the config above.
        

🔒 App Password bypasses 2FA for this specific app securely.

---

## 🛠️ JavaMailSender Setup

Spring Boot automatically creates a bean for `JavaMailSender` **if configuration is provided** in `application.yml`.  
Without these settings, the bean will be `null`.

```java
@Autowired
private JavaMailSender javaMailSender;
```

---

## 📤 Email Sending Code

Here’s the full `EmailService.java` code:

```java
@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    public void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(to);
            mailMessage.setSubject(subject);
            mailMessage.setText(body);
            mailMessage.setFrom("your-email@gmail.com"); // Optional, if already configured

            javaMailSender.send(mailMessage);
            log.info("Email sent successfully to {}", to);
        } catch (Exception e) {
            log.error("Exception while sending email: ", e);
        }
    }
}
```

---

## 🧪 Testing the Email Service

Example test call from another service or controller:

```java
emailService.sendEmail(
    "recipient@example.com",
    "Testing JavaMailSender",
    "Hi, how are you?"
);
```

✅ If setup is correct, you'll receive an email in your inbox.

---

## 🔍 Explanation of Email Config Properties

|Property|Description|
|---|---|
|`spring.mail.host`|SMTP server of email provider (e.g., `smtp.gmail.com`)|
|`spring.mail.port`|SMTP port (587 for TLS, 25 for plain text, 465 for SSL)|
|`spring.mail.username`|Your Gmail address|
|`spring.mail.password`|App password from Google (not your actual password)|
|`spring.mail.properties.mail.smtp.auth`|Set to `true` to enable SMTP authentication|
|`spring.mail.properties.mail.smtp.starttls.enable`|Set to `true` to enable TLS encryption|

---

## 🔒 Security Tips

- Use **App Passwords**, never store your personal password in code.
    
- Avoid sending sensitive data in the email body.
    
- For production, consider using email services like **SendGrid**, **Mailgun**, etc.
    

---

## 🧠 Summary

|Step|Action|
|---|---|
|1️⃣|Add `spring-boot-starter-mail` dependency|
|2️⃣|Configure `application.yml` with Gmail SMTP settings|
|3️⃣|Enable 2FA in Google and generate App Password|
|4️⃣|Autowire `JavaMailSender` in service|
|5️⃣|Use `SimpleMailMessage` to compose and send email|
|6️⃣|Test sending an email from the service|

---

Let me know if you'd like:

- ✅ Integration with SendGrid (as mentioned for future videos)
    
- ✅ Email with attachments
    
- ✅ HTML email support
    
- ✅ Unit test for the email service using Mockito
    

Would you like the `EmailService.java` code or the test class in a file too?