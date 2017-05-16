package com.capitalone.dashboard.model;

public class SplunkCollector extends Collector {
	
public static SplunkCollector prototype(){
	SplunkCollector protoType = new SplunkCollector();
	protoType.setName("Splunk");
	protoType.setCollectorType(CollectorType.ChatOps);
	protoType.setOnline(true);
	protoType.setEnabled(true);
	return protoType;
}
}
