package com.sathones.limitservice.bean;

public class Limit {
	
	private int minimum;
	private int maximum;
	private String url;
	
	public Limit(int minimum, int maximum, String url) {
		super();
		this.minimum = minimum;
		this.maximum = maximum;
		this.url = url;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Limit(int minimum, int maximum) {
		super();
		this.minimum = minimum;
		this.maximum = maximum;
	}
	public Limit() {
		super();
	}
	public int getMinimum() {
		return minimum;
	}
	public void setMinimum(int minimum) {
		this.minimum = minimum;
	}
	public int getMaximum() {
		return maximum;
	}
	public void setMaximum(int maximum) {
		this.maximum = maximum;
	}
	
	
	

}
