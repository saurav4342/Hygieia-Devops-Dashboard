package com.capitalone.dashboard.repository;

import org.bson.types.ObjectId;
import org.springframework.data.repository.CrudRepository;

import com.capitalone.dashboard.model.EnvironmentComponent;


public interface DeploymentMapRepository extends CrudRepository<EnvironmentComponent,ObjectId>{

}


