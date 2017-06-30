package com.capitalone.dashboard.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

import com.capitalone.dashboard.model.DeploymentMap;
import com.capitalone.dashboard.model.EnvironmentComponent;


public interface DeploymentMapRepository extends CrudRepository<EnvironmentComponent,ObjectId>,QueryDslPredicateExecutor<EnvironmentComponent>{
    @Query(value="{'deployment.pod.podName': ?0}")
	List<DeploymentMap> findByPodName(String podName);
    @Query(value="{'application':?0}")
    List<DeploymentMap> findByApplication(String application);
}


