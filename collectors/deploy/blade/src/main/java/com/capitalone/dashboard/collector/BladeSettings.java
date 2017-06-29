package com.capitalone.dashboard.collector;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Bean to hold settings specific to the UDeploy collector.
 */
@Component
@ConfigurationProperties(prefix = "udeploy")
public class BladeSettings {
    private String cron;
    private String username;
    private String password;
    private String url;
    private String command;
    private String splunkApp;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
    public String getCommand() {
    	return command;
    }
    
    public void setCommand(String command) {
    	this.command = command;
    }
    public void setSplunkApp(String splunkApp){
    	this.splunkApp = splunkApp;
    }
    public String getSplunkApp(){
    	return splunkApp;
    }
    public void setSavedSearch(String savedSearch){
    	this.savedSearch = savedSearch;
    }
    public String getSavedSearch(){
    	return savedSearch;
    }
}
