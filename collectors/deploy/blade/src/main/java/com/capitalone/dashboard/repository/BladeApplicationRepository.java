package com.capitalone.dashboard.repository;

import com.capitalone.dashboard.model.BladeApplication;
import com.capitalone.dashboard.model.BladeCollector;

public interface BladeApplicationRepository extends BaseCollectorItemRepository<BladeCollector>{
//Find an application by application Id from mongoDb repository
	BladeApplication findByApplicationId(String applicationId); 
}
