package com.capitalone.dashboard.model;

public class Iteration {
private  String sprintID;
private  String sprintName;
private  String sprintStartDate;
private  String sprintEndDate;
public void setID(String sprintID){
	this.sprintID=sprintID;
}
public void setName(String sprintName){
	this.sprintName=sprintName;
}
public void setStartDate(String startDate){
	this.sprintStartDate=startDate;
}
public void setEndDate(String endDate){
	this.sprintEndDate=endDate;
}
public String getID(){
	return this.sprintID;
}
public String getName(){
	return this.sprintName;
}
public String getBeginDate(){
	return this.sprintStartDate;
}
public String getEndDate(){
	return this.sprintEndDate;
}
}
