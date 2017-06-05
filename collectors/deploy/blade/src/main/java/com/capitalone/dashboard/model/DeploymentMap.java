package com.capitalone.dashboard.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "deployment_data")
public class DeploymentMap extends EnvironmentComponent{

	private String deploymentId;
	private Deployment deployment;
	
	public void setDeploymentId(String deploymentId){
		this.deploymentId= deploymentId;
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
