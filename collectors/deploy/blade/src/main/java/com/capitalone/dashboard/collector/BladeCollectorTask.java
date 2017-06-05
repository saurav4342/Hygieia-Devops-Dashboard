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
import com.capitalone.dashboard.model.Pod;
import com.capitalone.dashboard.repository.BaseCollectorRepository;
import com.capitalone.dashboard.repository.DeploymentMapRepository;
import com.capitalone.dashboard.repository.BladeCollectorRepository;
import com.capitalone.dashboard.repository.PodRepository;

@Component
public class BladeCollectorTask extends CollectorTask<BladeCollector>{
	 private static final Logger LOGGER = LoggerFactory.getLogger(BladeCollectorTask.class);
     private final BladeCollectorRepository bladeCollectorRepository;
     private final BladeSettings bladeSettings;
     private final DeploymentDataFactory dataFactory;
     private final PodRepository podRepository;
     private final DeploymentMapRepository depMapRepository;
     
     @Autowired
	 public BladeCollectorTask(TaskScheduler taskScheduler,
			                   PodRepository podRepository,
			                   BladeCollectorRepository bladeCollectorRepository,
			                   BladeSettings bladeSettings,
			                   DeploymentDataFactory dataFactory,
			                   DeploymentMapRepository depMapRepository){
		 super(taskScheduler,"BladeLogic Collector");
	     this.bladeCollectorRepository=bladeCollectorRepository;
	     this.bladeSettings=bladeSettings;
	     this.dataFactory=dataFactory;
	     this.depMapRepository=depMapRepository;
	     this.podRepository=podRepository;
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
	    	LOGGER.info("Completed.");
	    	LOGGER.info("Creating Pods..");
	    	List<Pod> podList = dataFactory.createPods(taskList);
	    	LOGGER.info("Saving..");
	    	for(Pod pod:podList){
	    	pod.setCollectorId(collector.getId());
	    	podRepository.save(pod);
	    	}
	    	LOGGER.info("Completed");
	    	LOGGER.info("Creating Map");
	    	List<DeploymentMap> mapList = dataFactory.getDeploymentMap(taskList);
	    	depMapRepository.save(mapList);
	    	LOGGER.info("Completed");
	    	}
	    	catch(Exception e){
	    		LOGGER.error("error", e);
	    		LOGGER.info("Error");
	    	}
	    }
}
