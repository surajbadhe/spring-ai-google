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
