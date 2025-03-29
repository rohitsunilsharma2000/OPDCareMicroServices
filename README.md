
### ✅ Project Directory Tree:

```plaintext
src
|-- main
|   |-- java
|   |   `-- com
|   |       `-- example
|   |           `-- supportservice
|   |               |-- TuringLLMTuningSystem.java
|   |               |-- config
|   |               |   |-- SecurityConfig.java
|   |               |   |-- WebSocketConfig.java
|   |               |   `-- WebSocketJwtInterceptor.java
|   |               |-- controller
|   |               |   |-- AuthController.java
|   |               |   |-- ChatController.java
|   |               |   |-- ChatRestController.java
|   |               |   `-- TicketController.java
|   |               |-- dto
|   |               |   |-- ChatMessage.java
|   |               |   |-- LoginRequest.java
|   |               |   |-- RegisterRequest.java
|   |               |   |-- TicketRequest.java
|   |               |   |-- TicketResponse.java
|   |               |   |-- UserDto.java
|   |               |   `-- UserResponseDTO.java
|   |               |-- enums
|   |               |   |-- Priority.java
|   |               |   |-- Role.java
|   |               |   `-- TicketStatus.java
|   |               |-- exception
|   |               |   |-- GlobalExceptionHandler.java
|   |               |   |-- ResourceNotFoundException.java
|   |               |   `-- TicketNotFoundException.java
|   |               |-- filter
|   |               |   `-- JwtAuthenticationFilter.java
|   |               |-- model
|   |               |   |-- Attachment.java
|   |               |   |-- MessageEntity.java
|   |               |   |-- Ticket.java
|   |               |   |-- User.java
|   |               |   `-- UserRole.java
|   |               |-- repository
|   |               |   |-- MessageRepository.java
|   |               |   |-- TicketRepository.java
|   |               |   `-- UserRepository.java
|   |               |-- service
|   |               |   |-- ChatService.java
|   |               |   |-- CustomUserDetailsService.java
|   |               |   |-- EmailService.java
|   |               |   |-- JwtService.java
|   |               |   |-- TicketReminderScheduler.java
|   |               |   |-- TicketService.java
|   |               |   `-- UserService.java
|   |               `-- utils
|   |                   |-- FileStorageUtil.java
|   |                   `-- JwtUtil.java
|   `-- resources
|       |-- application-mysql.properties
|       |-- application.properties
|       `-- templates
|           `-- index.html
`-- test
    `-- java
        `-- com
            `-- example
                `-- supportservice
                    |-- TuringLLMTuningSystem.java
                    |-- config
                    |   |-- SecurityConfigTest.java
                    |   |-- WebSocketConfigTest.java
                    |   `-- WebSocketJwtInterceptorTest.java
                    |-- controller
                    |   |-- AuthControllerIntegrationTest.java
                    |   |-- ChatControllerMockTest.java
                    |   |-- ChatRestControllerTest.java
                    |   |-- TicketControllerIntegrationTest.java
                    |   `-- WebSocketIntegrationTest.java
                    |-- dto
                    |   |-- ChatMessageTest.java
                    |   |-- LoginRequestTest.java
                    |   |-- RegisterRequestTest.java
                    |   |-- TicketRequestTest.java
                    |   |-- TicketResponseTest.java
                    |   |-- UserDtoTest.java
                    |   `-- UserResponseDTOTest.java
                    |-- exception
                    |   `-- GlobalExceptionHandlerTest.java
                    |-- model
                    |   |-- AttachmentTest.java
                    |   `-- UserRoleTest.java
                    |-- service
                    |   |-- ChatServiceTest.java
                    |   |-- CustomUserDetailsServiceTest.java
                    |   |-- JwtServiceTest.java
                    |   `-- TicketReminderScheduler.java
                    `-- utils
                        `-- JwtUtilTest.java
```

---


<span style="font-weight: bold; color: white;">UserController: </span>
<span style="background-color: #2e2e2e; padding: 5px; border-radius: 3px; color: #f9e26b;">pom.xml</span>


```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.4.2</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.example</groupId>
    <artifactId>supportservice</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>Turing LLM Tuning System</name>
    <description>Turing LLM Tuning System</description>
    <url/>
    <licenses>
        <license/>
    </licenses>
    <developers>
        <developer/>
    </developers>
    <scm>
        <connection/>
        <developerConnection/>
        <tag/>
        <url/>
    </scm>
    <properties>
        <java.version>17</java.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <!-- Spring Boot Starter Web (Includes Spring Security by default) -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <!-- Spring Security for authentication and authorization -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-test</artifactId>
            <scope>test</scope>
        </dependency>
        <!-- Spring Boot Starter for Validation -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
        <!-- Lombok Dependency -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.24</version>
            <scope>provided</scope>
        </dependency>
        <!-- For Jakarta Bean Validation -->
        <dependency>
            <groupId>jakarta.validation</groupId>
            <artifactId>jakarta.validation-api</artifactId>
            <version>3.0.2</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-mail</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
        </dependency>
        <!-- MySQL, Spring Boot 3.x -->
        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <version>3.4.1</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-websocket</artifactId>
            <version>3.4.0</version>
        </dependency>
        <dependency>
            <groupId>org.webjars</groupId>
            <artifactId>sockjs-client</artifactId>
            <version>1.5.1</version>
        </dependency>

        <dependency>
            <groupId>org.webjars</groupId>
            <artifactId>stomp-websocket</artifactId>
            <version>2.3.4</version>
        </dependency>

        <dependency>
            <groupId>org.webjars</groupId>
            <artifactId>bootstrap</artifactId>
            <version>5.1.3</version>
        </dependency>
        <!--		Token-Based Authentication with JWT-->
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-api</artifactId>
            <version>0.11.5</version>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-impl</artifactId>
            <version>0.11.5</version>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-jackson</artifactId>
            <version>0.11.5</version>
            <scope>runtime</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>
```


<span style="font-weight: bold; color: white;">UserController: </span>
<span style="background-color: #2e2e2e; padding: 5px; border-radius: 3px; color: #f9e26b;">src/main/java/com/example/turingOnlineForumSystem/controller/UserController.java</span>


```java

```

<span style="font-weight: bold; color: white;">UserController: </span>
<span style="background-color: #2e2e2e; padding: 5px; border-radius: 3px; color: #f9e26b;">src/main/java/com/example/turingOnlineForumSystem/controller/UserController.java</span>


```java

```

<span style="font-weight: bold; color: white;">UserController: </span>
<span style="background-color: #2e2e2e; padding: 5px; border-radius: 3px; color: #f9e26b;">src/main/java/com/example/turingOnlineForumSystem/controller/UserController.java</span>


```java

```
<span style="font-weight: bold; color: white;">UserController: </span>
<span style="background-color: #2e2e2e; padding: 5px; border-radius: 3px; color: #f9e26b;">src/main/java/com/example/turingOnlineForumSystem/controller/UserController.java</span>


```java

```
<span style="font-weight: bold; color: white;">UserController: </span>
<span style="background-color: #2e2e2e; padding: 5px; border-radius: 3px; color: #f9e26b;">src/main/java/com/example/turingOnlineForumSystem/controller/UserController.java</span>


```java

```
<span style="font-weight: bold; color: white;">UserController: </span>
<span style="background-color: #2e2e2e; padding: 5px; border-radius: 3px; color: #f9e26b;">src/main/java/com/example/turingOnlineForumSystem/controller/UserController.java</span>


```java

```
<span style="font-weight: bold; color: white;">UserController: </span>
<span style="background-color: #2e2e2e; padding: 5px; border-radius: 3px; color: #f9e26b;">src/main/java/com/example/turingOnlineForumSystem/controller/UserController.java</span>


```java

```
<span style="font-weight: bold; color: white;">UserController: </span>
<span style="background-color: #2e2e2e; padding: 5px; border-radius: 3px; color: #f9e26b;">src/main/java/com/example/turingOnlineForumSystem/controller/UserController.java</span>


```java

```
<span style="font-weight: bold; color: white;">UserController: </span>
<span style="background-color: #2e2e2e; padding: 5px; border-radius: 3px; color: #f9e26b;">src/main/java/com/example/turingOnlineForumSystem/controller/UserController.java</span>


```java

```
<span style="font-weight: bold; color: white;">UserController: </span>
<span style="background-color: #2e2e2e; padding: 5px; border-radius: 3px; color: #f9e26b;">src/main/java/com/example/turingOnlineForumSystem/controller/UserController.java</span>


```java

```
<span style="font-weight: bold; color: white;">UserController: </span>
<span style="background-color: #2e2e2e; padding: 5px; border-radius: 3px; color: #f9e26b;">src/main/java/com/example/turingOnlineForumSystem/controller/UserController.java</span>


```java

```
<span style="font-weight: bold; color: white;">UserController: </span>
<span style="background-color: #2e2e2e; padding: 5px; border-radius: 3px; color: #f9e26b;">src/main/java/com/example/turingOnlineForumSystem/controller/UserController.java</span>


```java

```
<span style="font-weight: bold; color: white;">UserController: </span>
<span style="background-color: #2e2e2e; padding: 5px; border-radius: 3px; color: #f9e26b;">src/main/java/com/example/turingOnlineForumSystem/controller/UserController.java</span>


```java

```
<span style="font-weight: bold; color: white;">UserController: </span>
<span style="background-color: #2e2e2e; padding: 5px; border-radius: 3px; color: #f9e26b;">src/main/java/com/example/turingOnlineForumSystem/controller/UserController.java</span>


```java

```
<span style="font-weight: bold; color: white;">UserController: </span>
<span style="background-color: #2e2e2e; padding: 5px; border-radius: 3px; color: #f9e26b;">src/main/java/com/example/turingOnlineForumSystem/controller/UserController.java</span>


```java

```
<span style="font-weight: bold; color: white;">UserController: </span>
<span style="background-color: #2e2e2e; padding: 5px; border-radius: 3px; color: #f9e26b;">src/main/java/com/example/turingOnlineForumSystem/controller/UserController.java</span>


```java

```
<span style="font-weight: bold; color: white;">UserController: </span>
<span style="background-color: #2e2e2e; padding: 5px; border-radius: 3px; color: #f9e26b;">src/main/java/com/example/turingOnlineForumSystem/controller/UserController.java</span>


```java

```
<span style="font-weight: bold; color: white;">UserController: </span>
<span style="background-color: #2e2e2e; padding: 5px; border-radius: 3px; color: #f9e26b;">src/main/java/com/example/turingOnlineForumSystem/controller/UserController.java</span>


```java

```
<span style="font-weight: bold; color: white;">UserController: </span>
<span style="background-color: #2e2e2e; padding: 5px; border-radius: 3px; color: #f9e26b;">src/main/java/com/example/turingOnlineForumSystem/controller/UserController.java</span>


```java

```
<span style="font-weight: bold; color: white;">UserController: </span>
<span style="background-color: #2e2e2e; padding: 5px; border-radius: 3px; color: #f9e26b;">src/main/java/com/example/turingOnlineForumSystem/controller/UserController.java</span>


```java

```
<span style="font-weight: bold; color: white;">UserController: </span>
<span style="background-color: #2e2e2e; padding: 5px; border-radius: 3px; color: #f9e26b;">src/main/java/com/example/turingOnlineForumSystem/controller/UserController.java</span>


```java

```
<span style="font-weight: bold; color: white;">UserController: </span>
<span style="background-color: #2e2e2e; padding: 5px; border-radius: 3px; color: #f9e26b;">src/main/java/com/example/turingOnlineForumSystem/controller/UserController.java</span>


```java

```
<span style="font-weight: bold; color: white;">UserController: </span>
<span style="background-color: #2e2e2e; padding: 5px; border-radius: 3px; color: #f9e26b;">src/main/java/com/example/turingOnlineForumSystem/controller/UserController.java</span>


```java

```
<span style="font-weight: bold; color: white;">UserController: </span>
<span style="background-color: #2e2e2e; padding: 5px; border-radius: 3px; color: #f9e26b;">src/main/java/com/example/turingOnlineForumSystem/controller/UserController.java</span>


```java

```
<span style="font-weight: bold; color: white;">UserController: </span>
<span style="background-color: #2e2e2e; padding: 5px; border-radius: 3px; color: #f9e26b;">src/main/java/com/example/turingOnlineForumSystem/controller/UserController.java</span>


```java

```
<span style="font-weight: bold; color: white;">UserController: </span>
<span style="background-color: #2e2e2e; padding: 5px; border-radius: 3px; color: #f9e26b;">src/main/java/com/example/turingOnlineForumSystem/controller/UserController.java</span>


```java

```
<span style="font-weight: bold; color: white;">UserController: </span>
<span style="background-color: #2e2e2e; padding: 5px; border-radius: 3px; color: #f9e26b;">src/main/java/com/example/turingOnlineForumSystem/controller/UserController.java</span>


```java

```
<span style="font-weight: bold; color: white;">UserController: </span>
<span style="background-color: #2e2e2e; padding: 5px; border-radius: 3px; color: #f9e26b;">src/main/java/com/example/turingOnlineForumSystem/controller/UserController.java</span>


```java

```
<span style="font-weight: bold; color: white;">UserController: </span>
<span style="background-color: #2e2e2e; padding: 5px; border-radius: 3px; color: #f9e26b;">src/main/java/com/example/turingOnlineForumSystem/controller/UserController.java</span>


```java

```
<span style="font-weight: bold; color: white;">UserController: </span>
<span style="background-color: #2e2e2e; padding: 5px; border-radius: 3px; color: #f9e26b;">src/main/java/com/example/turingOnlineForumSystem/controller/UserController.java</span>


```java

```
<span style="font-weight: bold; color: white;">UserController: </span>
<span style="background-color: #2e2e2e; padding: 5px; border-radius: 3px; color: #f9e26b;">src/main/java/com/example/turingOnlineForumSystem/controller/UserController.java</span>


```java

```
<span style="font-weight: bold; color: white;">UserController: </span>
<span style="background-color: #2e2e2e; padding: 5px; border-radius: 3px; color: #f9e26b;">src/main/java/com/example/turingOnlineForumSystem/controller/UserController.java</span>


```java

```
<span style="font-weight: bold; color: white;">UserController: </span>
<span style="background-color: #2e2e2e; padding: 5px; border-radius: 3px; color: #f9e26b;">src/main/java/com/example/turingOnlineForumSystem/controller/UserController.java</span>


```java

```
<span style="font-weight: bold; color: white;">UserController: </span>
<span style="background-color: #2e2e2e; padding: 5px; border-radius: 3px; color: #f9e26b;">src/main/java/com/example/turingOnlineForumSystem/controller/UserController.java</span>


```java

```
<span style="font-weight: bold; color: white;">UserController: </span>
<span style="background-color: #2e2e2e; padding: 5px; border-radius: 3px; color: #f9e26b;">src/main/java/com/example/turingOnlineForumSystem/controller/UserController.java</span>


```java

```
<span style="font-weight: bold; color: white;">UserController: </span>
<span style="background-color: #2e2e2e; padding: 5px; border-radius: 3px; color: #f9e26b;">src/main/java/com/example/turingOnlineForumSystem/controller/UserController.java</span>


```java

```