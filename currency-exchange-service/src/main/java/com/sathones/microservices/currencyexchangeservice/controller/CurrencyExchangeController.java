package com.sathones.microservices.currencyexchangeservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sathones.microservices.currencyexchangeservice.bean.CurrencyExchange;
import com.sathones.microservices.currencyexchangeservice.repo.CurrencyExchangeRepository;

@RestController
public class CurrencyExchangeController {
	
	@Autowired
	private Environment env;
	
	@Autowired
	CurrencyExchangeRepository currencyExchangeRepository;
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@GetMapping("/currency-exchange/from/{from}/to/{to}")
	public CurrencyExchange retrieveExchangeValue(@PathVariable String from, @PathVariable String to) throws JsonProcessingException {
		CurrencyExchange currencyExchange = currencyExchangeRepository.findByFromAndTo(from, to);
		currencyExchange.setEnvironment(env.getProperty("local.server.port"));
		logger.info("Response of execution of Currency Exchange Service : {}"+new ObjectMapper().writeValueAsString(currencyExchange));
		return currencyExchange;
	}

}
