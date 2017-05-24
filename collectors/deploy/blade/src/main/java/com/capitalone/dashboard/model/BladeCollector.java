package com.capitalone.dashboard.model;
/**
 * Collector implementation for UDeploy that stores UDeploy server URLs.
 */
public class BladeCollector extends Collector {
    public static BladeCollector prototype() {
        BladeCollector protoType = new BladeCollector();
        protoType.setName("Blade");
        protoType.setCollectorType(CollectorType.Deployment);
        protoType.setOnline(true);
        protoType.setEnabled(true);
        return protoType;
    }
}
