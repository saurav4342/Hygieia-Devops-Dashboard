package com.capitalone.dashboard.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "deployment_data")
public class DeploymentMap extends EnvironmentComponent{

	private String deploymentId;
	private Deployment deployment;
	private String version;
	private String application;
	
	public void setDeploymentId(String deploymentId){
		this.deploymentId= deploymentId;
	}
    public void setApplication(String application){
    	this.application = application;
    }
    public String getApplication(){
    	return application;
    }
    public void setVersion(String version){
    	this.version = version;
    }
    public String getVersion(){
    	return version;
    }
	public void setDeployment(Deployment deployment){
		this.deployment=deployment;
	}
	public String getDeploymentId(){
		return deploymentId;
	}
	public Deployment getDeployment(){
		return deployment;
	}
}
