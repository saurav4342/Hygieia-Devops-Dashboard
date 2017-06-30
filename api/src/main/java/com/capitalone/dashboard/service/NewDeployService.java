package com.capitalone.dashboard.service;

import java.util.List;

import org.bson.types.ObjectId;

import com.capitalone.dashboard.misc.HygieiaException;
import com.capitalone.dashboard.model.DataResponse;
import com.capitalone.dashboard.model.deploy.NewEnvironment;
import com.capitalone.dashboard.request.DeployDataCreateRequest;

public interface NewDeployService {
	 DataResponse<List<NewEnvironment>> getDeployStatus(ObjectId componentId);
	 String create(DeployDataCreateRequest request) throws HygieiaException;
}
