package com.sathones.microservices.currencyconversionservice.controller;

import java.math.BigDecimal;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sathones.microservices.currencyconversionservice.CurrencyExchangeProxy;
import com.sathones.microservices.currencyconversionservice.bean.CurrencyConversion;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController
public class CurrencyConversionController {
	
	@Autowired
	Environment environment;
	
	@Autowired
	CurrencyExchangeProxy proxy;
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@GetMapping("/currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversion calculateConversion(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity) throws JsonProcessingException {
		HashMap<String, String > urlParameters = new HashMap<String, String >();
		urlParameters.put("from", from);
		urlParameters.put("to", to);
		CurrencyConversion body = new RestTemplate().getForEntity("http://localhost:8000/currency-exchange/from/{from}/to/{to}", 
				CurrencyConversion.class, urlParameters).getBody();
		
		CurrencyConversion currencyConversion = new CurrencyConversion(1000L, from, to, quantity, body.getConversionMultiple(),quantity.multiply(body.getConversionMultiple()));
		currencyConversion.setEnvironment(environment.getProperty("local.server.port")+" Conventional");
		logger.info("Response of execution of Currency Conversion Service : {}"+new ObjectMapper().writeValueAsString(currencyConversion));
		return currencyConversion;
	}
	
	
	@GetMapping("/currency-conversion-feign/from/{from}/to/{to}/quantity/{quantity}")
	@CircuitBreaker(name = "default", fallbackMethod = "alternateExecution")
	public CurrencyConversion calculateConversionFeign(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity) {
		CurrencyConversion body = proxy.retrieveExchangeValue(from, to);
		CurrencyConversion currencyConversion = new CurrencyConversion(1000L, from, to, quantity, body.getConversionMultiple(),quantity.multiply(body.getConversionMultiple()));
		currencyConversion.setEnvironment(body.getEnvironment()+" feign");
		return currencyConversion;
	}
	
	public CurrencyConversion alternateExecution(Exception e) {
		System.out.println("This is fallback code executed.");
		return new CurrencyConversion(999L,"NoWhere", "SomeWhere",BigDecimal.valueOf(0),BigDecimal.valueOf(0),BigDecimal.valueOf(0));
	}
}
