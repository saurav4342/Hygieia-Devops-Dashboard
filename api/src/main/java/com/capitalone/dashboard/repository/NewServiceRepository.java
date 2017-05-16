package com.capitalone.dashboard.repository;

import com.capitalone.dashboard.model.Service;

public interface NewServiceRepository extends ServiceRepository {

	Service findByName(String name);
}
