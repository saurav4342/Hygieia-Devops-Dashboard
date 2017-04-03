package com.hygieia.rally.collector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;

import com.capitalone.dashboard.collector.CollectorTask;
import com.capitalone.dashboard.repository.BaseCollectorRepository;
import com.hygieia.rally.model.FeatureCollector;
import com.hygieia.rally.repository.FeatureCollectorRepository;

public class FeatureCollectorTask extends CollectorTask<FeatureCollector> {
    private static final Logger LOGGER = LoggerFactory.getLogger(FeatureCollectorTask.class);
private final FeatureCollectorRepository featureCollectorRep;
    @Autowired
	public FeatureCollectorTask(TaskScheduler taskScheduler,FeatureCollectorRepository featureCollectorRep) {
		super(taskScheduler, "Rally");
		this.featureCollectorRep=featureCollectorRep;
		// TODO Auto-generated constructor stub
	}

	@Override
	public FeatureCollector getCollector() {
		// TODO Auto-generated method stub
		return FeatureCollector.prototype();
	}

	@Override
	public BaseCollectorRepository<FeatureCollector> getCollectorRepository() {
		// TODO Auto-generated method stub
		return featureCollectorRep;
	}

	@Override
	public String getCron() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void collect(FeatureCollector collector) {
		// TODO Auto-generated method stub
		LOGGER.info("started collector");
	}

}
