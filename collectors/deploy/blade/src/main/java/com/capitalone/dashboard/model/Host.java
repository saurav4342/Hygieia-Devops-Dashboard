package com.capitalone.dashboard.model;

import java.util.List;

public class Host {
private String hostName;
private List<String> components;

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

}
