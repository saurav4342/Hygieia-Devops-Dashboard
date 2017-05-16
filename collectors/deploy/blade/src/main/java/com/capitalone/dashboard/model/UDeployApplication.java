package com.capitalone.dashboard.model;

public class UDeployApplication extends CollectorItem {
      private String applicationName ;
    private String applicationId ;

    public String getApplicationId() {
        return applicationId;
    }

    
    public void setApplicationId(String applicationId) {
        this.applicationId=applicationId;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }
}
