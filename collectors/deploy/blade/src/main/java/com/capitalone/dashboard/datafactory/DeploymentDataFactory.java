package com.capitalone.dashboard.datafactory;


import java.io.IOException;
import java.util.List;
import com.capitalone.dashboard.model.DeploymentMap;
import com.capitalone.dashboard.model.DeploymentTask;
import com.capitalone.dashboard.model.Pod;

public interface DeploymentDataFactory {

	 List<DeploymentTask> connectToSplunk() throws InterruptedException, IOException;
	 List<Pod> createPods(List<DeploymentTask> taskList);
	 List<DeploymentMap> getDeploymentMap(List<DeploymentTask> taskList);
}
