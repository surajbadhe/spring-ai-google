package com.suraj.ai.controller;

import org.jspecify.annotations.Nullable;
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
	
	/*
	 * Using Google GenAI SDK
	 */
	@GetMapping("/gemini/chat")
	public @Nullable String geminiChat(@RequestParam String message) {

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
