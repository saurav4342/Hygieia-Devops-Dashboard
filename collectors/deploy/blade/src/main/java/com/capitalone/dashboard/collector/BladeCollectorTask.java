package com.capitalone.dashboard.collector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import com.capitalone.dashboard.datafactory.DeploymentDataFactory;
import com.capitalone.dashboard.model.BladeCollector;
import com.capitalone.dashboard.repository.BaseCollectorRepository;
import com.capitalone.dashboard.repository.BladeCollectorRepository;
import com.capitalone.dashboard.repository.EnvironmentComponentRepository;
import com.capitalone.dashboard.repository.EnvironmentStatusRepository;
@Component
public class BladeCollectorTask extends CollectorTask<BladeCollector>{
	 private static final Logger LOGGER = LoggerFactory.getLogger(BladeCollectorTask.class);
     private final BladeCollectorRepository bladeCollectorRepository;
     private final BladeSettings bladeSettings;
     private final DeploymentDataFactory dataFactory;
     private final EnvironmentStatusRepository envStatusRepository;
     private final EnvironmentComponentRepository envComponentRepository;
     @Autowired
	 public BladeCollectorTask(TaskScheduler taskScheduler, BladeCollectorRepository bladeCollectorRepository, BladeSettings bladeSettings,DeploymentDataFactory dataFactory,EnvironmentStatusRepository envStatusRepository,EnvironmentComponentRepository envComponentRepository){
		 super(taskScheduler,"BladeLogic Collector");
	     this.bladeCollectorRepository=bladeCollectorRepository;
	     this.bladeSettings=bladeSettings;
	     this.dataFactory=dataFactory;
	     this.envComponentRepository=envComponentRepository;
	     this.envStatusRepository=envStatusRepository;
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
	    	LOGGER.info("Getting components");
	    	try{
	    	envComponentRepository.save(dataFactory.getEnvironmentComponent());
	    	LOGGER.info("Completed");
	    	LOGGER.info("Getting Status");
	    	envStatusRepository.save(dataFactory.getEnvironmentStatus());
	    	LOGGER.info("Completed");
	    	}
	    	catch(Exception e){
	    		LOGGER.error("error", e);
	    		LOGGER.info("Error");
	    	}
	    }
}
