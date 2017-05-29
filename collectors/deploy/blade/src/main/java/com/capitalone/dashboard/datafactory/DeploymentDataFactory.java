package com.capitalone.dashboard.datafactory;

import java.io.IOException;
import java.util.List;

import com.capitalone.dashboard.model.EnvironmentComponent;
import com.capitalone.dashboard.model.EnvironmentStatus;

public interface DeploymentDataFactory {
	List<EnvironmentComponent> getEnvironmentComponent() throws InterruptedException, IOException;
	List<EnvironmentStatus> getEnvironmentStatus() throws InterruptedException, IOException;
}
