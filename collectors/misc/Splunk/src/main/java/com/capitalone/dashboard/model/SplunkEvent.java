package com.capitalone.dashboard.model;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "splunk")
public class SplunkEvent extends BaseModel{
private String errorCount;
private String pod;
private String app;
private String percent;
private int uCIdCount;
private int uSysCodeCount;
private List<String> custIDs;
private List<String> syscodes;
private List<String> errorMessages;

public void setCustIDs(List<String> custIDs){
	this.custIDs=custIDs;
}
public void setErrorMessages(List<String> errorMessages){
	this.errorMessages = errorMessages;
}
public void setErrorCount(String string){
	this.errorCount=string;
}
public void setPod(String pod){
	this.pod=pod;
}
public void setApp(String app){
	this.app=app;
}
public void setPercentage(String percent){
	this.percent=percent;
}
public void setUCIdCount(int uCIdCount){
	this.uCIdCount=uCIdCount;
}
public void setuSysCodeCount(int uSysCodeCount){
	this.uSysCodeCount=uSysCodeCount;
}

public void setSyscodes(List<String> syscodes){
	this.syscodes = syscodes;
}
public int getUCIdCount(){
	return uCIdCount;
}
public int getuSysCodeCount(){
	return uSysCodeCount;
}
public List<String> getErrorMessages(){
	return errorMessages;
}
public List<String> getSyscodes(){
	return syscodes;
}
public List<String> getCustIDs(){
return custIDs;	
}
public String getErrorCount(){
	return errorCount;
}
public String getPod(){
	return pod;
}
public String getApp(){
	return app;
}
public String getPercentage(){
	return percent;
}


}
