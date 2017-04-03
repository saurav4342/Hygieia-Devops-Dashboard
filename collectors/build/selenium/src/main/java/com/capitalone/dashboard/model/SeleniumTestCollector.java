package com.capitalone.dashboard.model;
public class SeleniumTestCollector extends Collector{
	public static SeleniumTestCollector prototype() {
		SeleniumTestCollector protoType = new SeleniumTestCollector();
        protoType.setName("SeleniumTestController");
        protoType.setCollectorType(CollectorType.Test);
        protoType.setOnline(true);
        protoType.setEnabled(true);
        return protoType;
    }
}
