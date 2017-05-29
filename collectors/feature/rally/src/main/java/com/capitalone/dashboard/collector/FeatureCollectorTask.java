package com.capitalone.dashboard.collector;


import com.capitalone.dashboard.datafactory.rally.RallyDataFactoryImpl;
import com.capitalone.dashboard.misc.HygieiaException;
import com.capitalone.dashboard.model.FeatureCollector;
import com.capitalone.dashboard.repository.BaseCollectorRepository;
import com.capitalone.dashboard.repository.FeatureCollectorRepository;
//import java.io.IOException;
//import java.net.URISyntaxException;
import com.capitalone.dashboard.repository.FeatureRepository;
//import com.capitalone.dashboard.repository.ScopeOwnerRepository;
import com.capitalone.dashboard.repository.ScopeRepository;
import com.capitalone.dashboard.repository.TeamRepository;

import java.io.IOException;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;


/**
 * Collects {@link FeatureCollector} data from feature content source system.
 */
@Component
public class FeatureCollectorTask extends CollectorTask<FeatureCollector> {
    private static final Logger LOGGER = LoggerFactory.getLogger(FeatureCollectorTask.class);

  
    private final FeatureCollectorRepository featureCollectorRepository;
    private final FeatureRepository featureRepository;
    private final TeamRepository teamRepository;
    private final ScopeRepository projectRepository;
    private final FeatureSettings featureSettings;
    private final RallyDataFactoryImpl dataFactory;
    
    @Autowired
    public FeatureCollectorTask(TaskScheduler taskScheduler, FeatureRepository featureRepository,
            TeamRepository teamRepository, ScopeRepository projectRepository,
            FeatureCollectorRepository featureCollectorRepository, FeatureSettings featureSettings) throws HygieiaException,NullPointerException
             {
        super(taskScheduler,"Rally");
        
        this.featureCollectorRepository = featureCollectorRepository;
       this.featureRepository=featureRepository;
       this.featureSettings=featureSettings;
       this.teamRepository = teamRepository;
       this.projectRepository = projectRepository;
       this.dataFactory= new RallyDataFactoryImpl(this.featureSettings);
    }

    /**
     * Accessor method for the collector prototype object
     */
    @Override
    public FeatureCollector getCollector() {
        return FeatureCollector.prototype();
    }

    /**
     * Accessor method for the collector repository
     */
    @Override
    public BaseCollectorRepository<FeatureCollector> getCollectorRepository() {
        return featureCollectorRepository;
    }

    /**
     * Accessor method for the current chronology setting, for the scheduler
     */
    @Override
    public String getCron() {
        return featureSettings.getCron();
    }

    /**
     * The collection action. This is the task which will run on a schedule to
     * gather data from the feature content source system and update the
     * repository with retrieved data.
     */
    @Override
    public void collect(FeatureCollector collector) {
       ProjectDataClient projectClient = new ProjectDataClient(dataFactory,projectRepository,featureCollectorRepository);
       TeamDataClient teamClient = new TeamDataClient(dataFactory,teamRepository,featureCollectorRepository);
       StoryDataClient storyClient = new StoryDataClient(dataFactory,featureCollectorRepository,featureRepository);
       try {
    	   LOGGER.info("Fetching Projects");
		projectClient.updateMongoInfo();
 	   LOGGER.info("Fetching Teams");
		teamClient.updateMongoInfo();
 	   LOGGER.info("Fetching Stories");
		storyClient.updateMongoinfo();
	} catch (URISyntaxException e) {
		// TODO Auto-generated catch block
		LOGGER.info("URI Syntax");
	} catch (IOException e) {
		// TODO Auto-generated catch block
		LOGGER.info(e.getMessage());
	}
       catch(DuplicateKeyException e){
    	   LOGGER.info(e.getMessage());
       }
       LOGGER.info("Completed...");
    }


}
