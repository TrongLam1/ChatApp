package com.chat.app.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;

import io.github.cdimascio.dotenv.Dotenv;

@Configuration
public class CloudinaryConfig {

	@Bean
	public Cloudinary cloudinary() {
		Dotenv dotenv = Dotenv.load();
		Map<String, String> valueMap = new HashMap<>();
		valueMap.put("cloud_name", dotenv.get("cloud_name"));
		valueMap.put("api_key", dotenv.get("api_key"));
		valueMap.put("api_secret", dotenv.get("api_secret"));
		return new Cloudinary(valueMap);
	}
}
