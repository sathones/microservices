package com.sathones.microservices.currencyexchangeservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;



@RestController
public class CircuitBreakerController {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@GetMapping("/sample-api")
	public String sampleAPI() {
		return " This is sample API";
	}
	
	@GetMapping("/sample-retry")
	@Retry(name="sample-retry",fallbackMethod = "retryFallbackMethod")
	public String sampleRetry() {
		logger.info("Executing sample-retry");
		ResponseEntity<String> forEntity = new RestTemplate().getForEntity("http://localhost:8080/failure-service", String.class);
		return forEntity.getBody();
	}
	
	public String retryFallbackMethod(Exception e) {
		return "This is Fallback Invocation.";
	}
	
	@GetMapping("/sample-breaker")
	@CircuitBreaker(name = "default", fallbackMethod = "retryFallbackMethod")
	public String sampleBreaker() {
		logger.info("Executing sample-breaker");
		ResponseEntity<String> forEntity = new RestTemplate().getForEntity("http://localhost:8080/failure-service", String.class);
		return forEntity.getBody();
	}
	
	@GetMapping("/sample-rate-limiter")
	@RateLimiter(name="default",fallbackMethod = "retryFallbackMethod")
	public String sampleRate() {
		logger.info("Executing sample-rate-limiter");
//		ResponseEntity<String> forEntity = new RestTemplate().getForEntity("http://localhost:8080/failure-service", String.class);
//		return forEntity.getBody();
		return "sample rate limiter";
	}
	
//	@GetMapping("/sample-time-limiter")
//	@TimeLimiter(name = "default")
//	public String sampleTimeLimitter() throws InterruptedException {
//		logger.info("Executing sample-rate-limiter");
//		if(Math.random()%2==0) Thread.sleep(4000);
//		else System.out.println("Successful response");
//		return CompletionStage.
//	}

}
