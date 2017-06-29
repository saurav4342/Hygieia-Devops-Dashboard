package com.capitalone.dashboard.model;

import java.util.List;

public class Host {
private String hostName;
private List<String> components;
private String deploymentStatus;
private boolean isOnline;

public String getHostName(){
	return hostName;
}

public void setHostName(String hostName){
	this.hostName=hostName;
}
public List<String> getComponents(){
	return components;
}

public void setComponents(List<String> components){
	this.components=components;
}
public void setDeploymentStatus(String deploymentStatus){
	this.deploymentStatus = deploymentStatus;
}

public String getDeploymentStatus(){
	return deploymentStatus;
}
public boolean isOnline(){
	return isOnline;
}
public void setOnline(boolean isOnline){
	this.isOnline = isOnline;
}
}
