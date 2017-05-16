package com.capitalone.dashboard.model;

public class SplunkEvent {
private String hostcount;
private String pod;
private String warncount;
private String lastDeploymentTime;
private String errorcount;
private String deploymentStatus;
private String latestStage;
public void setHostCount(String hostcount){
	this.hostcount=hostcount;
}
public void setPod(String pod){
	this.pod=pod;
}
public void setwarncount(String warncount){
	this.warncount=warncount;
}
public void setLastDeploymentTime(String lastDeploymentTime){
	this.lastDeploymentTime=lastDeploymentTime;
}
public void setErrorCount(String errorcount){
	this.errorcount=errorcount;
}
public void setdeploymentStatus(String deploymentStatus){
	this.deploymentStatus=deploymentStatus;
}
public void setLatestStage(String latestStage){
	this.latestStage=latestStage;
}
public String getHostCount(){
	return hostcount;
}
public String getPod(){
	return pod;
}
public String getWarnCount(){
	return warncount;
}
public String getLastDeploymentTime(){
	return lastDeploymentTime;
}
public String getErrorCount(){
	return errorcount;
}
public String getDeploymentStatus(){
	return deploymentStatus;
}
public String getLatestStage(){
	return latestStage;
}
}
