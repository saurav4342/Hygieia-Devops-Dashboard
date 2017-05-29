package com.capitalone.dashboard.model;

import java.util.List;

public class SplunkEvent {
private String host;
private String pod;
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
public void setPod(String pod){
	this.pod=pod;
}
public void setComponents(List<String> components){
	this.components=components;
}
public void setLastDeploymentTime(String lastDeploymentTime){
	this.lastDeploymentTime=lastDeploymentTime;
}

public void setdeploymentStatus(String deploymentStatus){
	this.deploymentStatus=deploymentStatus;
}

public String getHost(){
	return host;
}
public String getPod(){
	return pod;
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
