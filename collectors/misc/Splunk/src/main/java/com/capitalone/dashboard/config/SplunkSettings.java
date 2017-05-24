package com.capitalone.dashboard.config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "splunk")
public class SplunkSettings {
	

	/**
	 * Bean to hold settings specific to the UDeploy collector.
	 */
	    private String cron;
	    private String username;
	    private String password;
	    private String url;
	    private String app;
	    private String savedSearch;

	    public String getCron() {
	        return cron;
	    }

	    public void setCron(String cron) {
	        this.cron = cron;
	    }

	    public String getUsername() {
	        return username;
	    }

	    public void setUsername(String username) {
	        this.username = username;
	    }

	    public String getPassword() {
	        return password;
	    }

	    public void setPassword(String password) {
	        this.password = password;
	    }
	    public String getUrl(){
	    	return url;
	    }
	    public void setUrl(String url){
	    	this.url=url;
	    }
	    public String getApp(){
	    	return app;
	    }
	    public void setApp(String app){
	    	this.app=app;
	    }
	    public String getSavedSearch(){
	    	return savedSearch;
	    }
	    public void getSavedSearch(String savedSearch){
	    	this.savedSearch=savedSearch;
	    }
	}


