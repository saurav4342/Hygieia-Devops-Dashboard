package com.capitalone.dashboard.collector;

//import java.util.ArrayList;
//import java.util.HashSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import com.capitalone.dashboard.datafactory.DeploymentDataFactory;
import com.capitalone.dashboard.model.BladeCollector;
import com.capitalone.dashboard.model.DeploymentMap;
import com.capitalone.dashboard.model.DeploymentTask;
import com.capitalone.dashboard.model.PodVersionMap;
import com.capitalone.dashboard.model.UDeployApplication;
import com.capitalone.dashboard.repository.BaseCollectorRepository;
import com.capitalone.dashboard.repository.BladeCollectorRepository;
import com.capitalone.dashboard.repository.DeploymentMapRepository;
import com.capitalone.dashboard.repository.PodVersionMapRepository;
import com.capitalone.dashboard.repository.UDeployApplicationRepository;

@Component
public class BladeCollectorTask extends CollectorTask<BladeCollector>{
	 private static final Logger LOGGER = LoggerFactory.getLogger(BladeCollectorTask.class);
     private final BladeCollectorRepository bladeCollectorRepository;
     private final BladeSettings bladeSettings;
     private final DeploymentDataFactory dataFactory;
     private final UDeployApplicationRepository uDeployApplicationRepository;
     private final DeploymentMapRepository deploymentMapRepository;
     private final PodVersionMapRepository podVersionMapRepository;
     
     @Autowired
	 public BladeCollectorTask(TaskScheduler taskScheduler,
			                   UDeployApplicationRepository uDeployApplicationRepository,
			                   BladeCollectorRepository bladeCollectorRepository,
			                   BladeSettings bladeSettings,
			                   DeploymentDataFactory dataFactory,
			                   DeploymentMapRepository deploymentMapRepository,
			                   PodVersionMapRepository podVersionMapRepository
			                   ){
		 super(taskScheduler,"BladeLogic Collector");
	     this.bladeCollectorRepository = bladeCollectorRepository;
	     this.bladeSettings = bladeSettings;
	     this.dataFactory = dataFactory;
	     this.deploymentMapRepository = deploymentMapRepository;
	     this.uDeployApplicationRepository = uDeployApplicationRepository;
	     this.podVersionMapRepository = podVersionMapRepository;
	 }
	 
	    @Override
	    public BladeCollector getCollector() {
	        return BladeCollector.prototype();
	    }

	    @Override
	    public BaseCollectorRepository<BladeCollector> getCollectorRepository() {
	        return bladeCollectorRepository;
	    }

	    @Override
	    public String getCron() {
	        return bladeSettings.getCron();
	    }

	    @Override
	    public void collect(BladeCollector collector) {
	    	LOGGER.info("STARTING");
	    	try{
	    	LOGGER.info("Getting Data from Splunk..");
	    	List<DeploymentTask> taskList = dataFactory.connectToSplunk();
	    	LOGGER.info("Getting Version Data");
	    	List<PodVersionMap> podVersionMapList = dataFactory.getVersionData();
	    	LOGGER.info("Saving");
	    	podVersionMapRepository.deleteAll();
	    	podVersionMapRepository.save(podVersionMapList);
	    	LOGGER.info("Creating Map");
	    	List<DeploymentMap> mapList = dataFactory.getDeploymentMap(taskList,podVersionMapList);
	    	List<UDeployApplication> podList = dataFactory.createPods(mapList);
	    	for(UDeployApplication pod:podList){
	    		if(uDeployApplicationRepository.findByDescription(pod.getDescription()).isEmpty()){
	    			pod.setCollectorId(collector.getId());
		    	uDeployApplicationRepository.save(pod);
	    		}
		    	}
	    	LOGGER.info("Saving..");
	    	deploymentMapRepository.deleteAll();
	    	for(DeploymentMap map : mapList){
	    	    
	    		deploymentMapRepository.save(map);
	    	}
	    	LOGGER.info("Completed.");
	    	
	    	}
	    	catch(Exception e){
	    		LOGGER.error("error", e);
	    		LOGGER.info("Error");
	    	}
	    }
}
