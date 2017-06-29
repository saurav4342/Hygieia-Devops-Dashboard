package com.capitalone.dashboard.repository;

import org.bson.types.ObjectId;
import org.springframework.data.repository.CrudRepository;

import com.capitalone.dashboard.model.DeploymentMap;


public interface DeploymentMapRepository extends CrudRepository<DeploymentMap,ObjectId>{

}


