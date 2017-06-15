package com.capitalone.dashboard.service;

import com.capitalone.dashboard.model.Service;
import com.capitalone.dashboard.model.SplunkEvent;

import org.bson.types.ObjectId;

import java.util.List;

public interface ServiceService {
    /**
     *
     * @return All registered services
     */
    Iterable<SplunkEvent> all();

    /**
     * Our Services for a given Dashboard.
     *
     * @param dashboardId unique id of Dashboard
     * @return services
     */
    List<SplunkEvent> dashboardServices(ObjectId dashboardId);

    /**
     * Dependent Services for a given Dashboard.
     *
     * @param dashboardId unique id of Dashboard
     * @return dependent services
     */
    List<Service> dashboardDependentServices(ObjectId dashboardId);

    /**
     * A Service by its id
     *
     * @param id id
     * @return a Service
     */
    Service get(ObjectId id);

    /**
     * Create a new Service for a Dashboard.
     *
     * @param dashboardId id of Dashboard
     * @param name Name of new Service
     * @return Service
     */
    Service create(ObjectId dashboardId, String name);

    /**
     * Update an existing Service.
     *
     * @param dashboardId id of Dashboard
     * @param service updated Service
     * @return Service
     */
    Service update(ObjectId dashboardId, Service service);

    /**
     * Delete an existing Service.
     *
     * @param dashboardId id of Dashboard
     * @param serviceId id of Service
     */
    void delete(ObjectId dashboardId, ObjectId serviceId);

    /**
     * Associate an existing Service as a dependency for a Dashboard.
     *
     * @param dashboardId id of Dashboard
     * @param serviceId id of Service
     * @return Service
     */
   

    /**
     * Remove a Service dependency for a Dashboard.
     *
     * @param dashboardId id of Dashboard
     * @param serviceId id of Service
     */
    void deleteDependentService(ObjectId dashboardId, ObjectId serviceId);

	Service addDependentService(ObjectId dashboardId, ObjectId serviceId);

	
}
