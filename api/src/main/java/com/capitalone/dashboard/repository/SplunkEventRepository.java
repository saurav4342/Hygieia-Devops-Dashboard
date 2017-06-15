package com.capitalone.dashboard.repository;
import com.capitalone.dashboard.model.SplunkEvent;
import org.bson.types.ObjectId;
import org.springframework.data.repository.CrudRepository;
public interface SplunkEventRepository extends CrudRepository<SplunkEvent,ObjectId>{

	SplunkEvent findByPod(String pod);

	

}
