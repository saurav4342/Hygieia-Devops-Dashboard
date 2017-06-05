package com.capitalone.dashboard.model;

import java.util.List;

public class DeploymentTask {
private String host;
private String podName;
private List<String> components;
private String lastDeploymentTime;
private String deploymentStatus;
private String deploymentId;

public String getDeploymentId(){
	return deploymentId;
}
public void setDeploymentId(String deploymentId){
	this.deploymentId = deploymentId;
}
public void setHost(String host){
	this.host=host;
}
public void setPodName(String podName){
	this.podName=podName;
}
public void setComponents(List<String> components){
	this.components=components;
}
public void setLastDeploymentTime(String lastDeploymentTime){
	this.lastDeploymentTime=lastDeploymentTime;
}

public void setDeploymentStatus(String deploymentStatus){
	this.deploymentStatus=deploymentStatus;
}

public String getHost(){
	return host;
}
public String getPodName(){
	return podName;
}
public List<String> getComponents(){
	return components;
}
public String getLastDeploymentTime(){
	return lastDeploymentTime;
}

public String getDeploymentStatus(){
	return deploymentStatus;
}

}
