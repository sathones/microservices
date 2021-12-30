package com.sathones.limitservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sathones.limitservice.bean.Limit;
import com.sathones.limitservice.config.LimitsConfiguration;

@RestController
public class LimitController {
	
	@Autowired
	LimitsConfiguration config;
	
	@GetMapping(value="/limit")
	public Limit getLimits() {
		return new Limit(config.getMinimum(),config.getMaximum(),config.getUrl());
	}
}
