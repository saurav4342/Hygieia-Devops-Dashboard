package com.capitalone.dashboard.model;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "podVersionMap")
public class PodVersionMap {
private String version;
private String pod;
private String date;
private String app;
private ObjectId id;

public ObjectId getId(){
	return id;
}
public void setVersion(String version){
	this.version = version;
}
public String getVersion(){
	return version;
}

public void setPod(String pod){
	this.pod = pod;
}

public String getPod(){
	return pod;
}
public void setDate(String date){
	this.date = date;
}
public String getDate(){
	return date;
}
public void setApp(String app){
	this.app = app;
}
public String getApp(){
	return app;
}
}
