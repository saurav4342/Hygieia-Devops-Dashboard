package com.capitalone.dashboard.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.repository.CrudRepository;

import com.capitalone.dashboard.model.PodVersionMap;

public interface PodVersionMapRepository extends CrudRepository<PodVersionMap,ObjectId>{
    //@Query(value="{'pod' : ?0}")
	 List<PodVersionMap> findByApp(String app);
	 PodVersionMap findByPod(String pod);
}
