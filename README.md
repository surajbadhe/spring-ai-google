# Spring AI with Google GenAI SDK

This project demonstrates how to integrate the Google GenAI SDK with a Spring Boot application to make calls to the Gemini model. It provides a simple REST API to send prompts and receive responses from the large language model.

## Features

  * **RESTful API**: Exposes an endpoint to interact with the Gemini model.
  * **Google GenAI SDK**: Uses Google's official Java SDK for direct API calls to Gemini.

## Prerequisites

Before running this application, ensure you have the following:

  * **Java 21**: The project is configured to use Java 21.
  * **Maven**: The project's build tool.
  * **A Google API Key**: A `Generative Language API Key` from Google AI Studio.

## Step 1: Get Your Google API Key

1.  Go to **Google AI Studio** at [https://aistudio.google.com/](https://aistudio.google.com/).
2.  In the left-hand navigation, click **Get API key**.
3.  Click **Create API key in new project**.
4.  Copy the generated key. **This is your `GOOGLE_API_KEY`**. Store it securely. Do not commit this key to your version control system. .

## Step 2: Project Setup and Dependencies

This project uses the official Google GenAI SDK instead of the Spring AI starter for Google. This gives you direct access to the SDK's features.

1.  **Add the Dependencies**: Ensure your `pom.xml` includes the following dependencies.

```xml
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        
        <dependency>
            <groupId>com.google.genai</groupId>
            <artifactId>google-genai</artifactId>
            <version>1.0.0</version>
        </dependency>
        
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
    </dependencies>
```

## Step 3: Configure the Application

You will configure the API key by setting it as an environment variable. This is the most secure way to handle sensitive credentials.

1.  **Set the Environment Variable**: Before running the application, set the `GOOGLE_API_KEY` environment variable in your terminal.

**Windows (Command Prompt)**

```bash
	set GOOGLE_API_KEY="your-api-key-here"
```

2.  **Configure `application.properties`**: In `src/main/resources/application.properties`, reference the environment variable using a placeholder.

```properties
    spring.application.name=spring-ai-google
    google.genai.api.key=${GOOGLE_API_KEY}
```

## Step 4: Write the Java Code

### `GoogleClientConfig.java`

This class creates a Spring bean for the `Client` from the Google GenAI SDK. This allows the client to be injected into other components.

```java
	package com.suraj.ai.config;
	
	import org.springframework.context.annotation.Bean;
	import org.springframework.context.annotation.Configuration;
	
	import com.google.genai.Client;
	
	@Configuration
	public class GoogleClientConfig {
		@Bean
		Client googleGenAiClient() {
			return Client.builder().build();
		}
	}
```

### `GoogleChatController.java`

This controller uses constructor injection to receive the `Client` bean and expose a GET endpoint to interact with the Gemini model.

```java
	package com.suraj.ai.controller;
	
	import org.slf4j.Logger;
	import org.slf4j.LoggerFactory;
	import org.springframework.beans.factory.annotation.Value;
	import org.springframework.web.bind.annotation.GetMapping;
	import org.springframework.web.bind.annotation.RequestParam;
	import org.springframework.web.bind.annotation.RestController;
	
	import com.google.genai.Client;
	import com.google.genai.types.GenerateContentResponse;
	
	import lombok.RequiredArgsConstructor;
	
	@RestController
	@RequiredArgsConstructor
	public class GoogleChatController {
		private static final Logger log = LoggerFactory.getLogger(GoogleChatController.class);
		
		@Value("${google.genai.api.key}")
		private String GEMINI_API_KEY;
		
		private final Client googClient;
		
		@GetMapping("/gemini/chat")
		public String geminiChat(@RequestParam String message) {
	
			try {
	            GenerateContentResponse response = googClient.models.generateContent(
	                "gemini-2.5-flash",
	                message,
	                null
	            );
	            return response.text();
	        } catch (Exception e) {
	            log.error("Error calling Gemini API: " + e.getMessage(), e);
	            return "An error occurred while processing your request.";
	        }
		}
	}
```

## How to Run the Application

1.  **Build the Project**: Open your terminal in the project's root directory and run:

```bash
	mvn clean install
```

2.  **Run the Application**: Once the build is successful, run the application with the `GOOGLE_API_KEY` environment variable set.

```bash
	java -jar target/spring-ai-google-0.0.1-SNAPSHOT.jar
```

3.  **Test the Endpoint**: Open your browser or a tool like `curl` to test the API.

```bash
	curl http://localhost:8080/gemini/chat?message=What is Spring AI?
```
