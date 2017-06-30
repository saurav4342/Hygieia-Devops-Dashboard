package com.capitalone.dashboard.model;

import java.util.List;

public class Deployment {
private Pod pod;
private List<Host> hosts;
private String lastDeploymentTime;
private String deploymentStatus;

public void setHosts(List<Host> hosts){
	this.hosts=hosts;
}
public void setPod(Pod pod){
	this.pod=pod;
}

public void setLastDeploymentTime(String lastDeploymentTime){
	this.lastDeploymentTime=lastDeploymentTime;
}

public void setDeploymentStatus(String deploymentStatus){
	this.deploymentStatus=deploymentStatus;
}

public List<Host> getHosts(){
	return hosts;
}
public Pod getPod(){
	return pod;
}

public String getLastDeploymentTime(){
	return lastDeploymentTime;
}

public String getDeploymentStatus(){
	return deploymentStatus;
}
}
