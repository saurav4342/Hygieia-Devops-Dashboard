package com.capitalone.dashboard.repository;


import com.capitalone.dashboard.model.UDeployApplication;


public interface BladeApplicationRepository extends BaseCollectorItemRepository<UDeployApplication>{
//Find an application by application Id from mongoDb repository
	UDeployApplication findByApplicationId(String applicationId); 
}
