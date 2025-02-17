package com.fullstackbootcamp.capstoneBackend;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableCaching  // Add this annotation
@EnableJpaRepositories(basePackages = "com.fullstackbootcamp.capstoneBackend")
public class CapstoneBackendApplication {
	@Value("${spring.redis.host}")
	private String redisHost;

	@Value("${spring.redis.port}")
	private String redisPort;

	@PostConstruct
	public void printRedisConfig() {
		System.out.println("Redis Host: " + redisHost);
		System.out.println("Redis Port: " + redisPort);
	}

	public static void main(String[] args) {
		SpringApplication.run(CapstoneBackendApplication.class, args);
	}

}
