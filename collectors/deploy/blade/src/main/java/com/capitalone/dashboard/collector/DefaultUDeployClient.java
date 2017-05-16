package com.capitalone.dashboard.collector;

import com.capitalone.dashboard.model.Environment;
import com.capitalone.dashboard.model.EnvironmentComponent;
import com.capitalone.dashboard.model.UDeployApplication;
import com.capitalone.dashboard.model.UDeployEnvResCompData;
import com.capitalone.dashboard.util.Supplier;


import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class DefaultUDeployClient implements UDeployClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultUDeployClient.class);

    private final UDeploySettings uDeploySettings;
    private final RestOperations restOperations;

    @Autowired
    public DefaultUDeployClient(UDeploySettings uDeploySettings,
                                Supplier<RestOperations> restOperationsSupplier) {
        this.uDeploySettings = uDeploySettings;
        this.restOperations = restOperationsSupplier.get();
    }

    @Override
    public List<UDeployApplication> getApplications(String instanceUrl) {
        List<UDeployApplication> applications = new ArrayList<>();
            UDeployApplication application = new UDeployApplication();
            application.setInstanceUrl("http://wfn-fit.com");
            application.setApplicationName("WFN v14");
            application.setApplicationId("wfnv14.0.00");
            applications.add(application);
        
        return applications;
    }

    @Override
    public List<Environment> getEnvironments(UDeployApplication application) {
        List<Environment> environments = new ArrayList<>();
       environments.add(new Environment("DIT","DIT"));
       environments.add(new Environment("FIT","FIT"));
        return environments;
    }

    @SuppressWarnings("PMD.AvoidCatchingNPE")
    @Override
    public List<EnvironmentComponent> getEnvironmentComponents(
            UDeployApplication application, Environment environment) {
        List<EnvironmentComponent> components = new ArrayList<>();
                EnvironmentComponent component = new EnvironmentComponent();
                component.setEnvironmentID(environment.getId());
                component.setEnvironmentName(environment.getName());
                component.setEnvironmentUrl(application.getInstanceUrl() );
                component.setComponentID("WFNFIT02");
                component.setComponentName("WFNFIT02");
                component.setComponentVersion("13.0.0");
                component.setDeployed(true);
                component.setAsOfDate(12012017);
                components.add(component);
        return components;
    }

    // Called by DefaultEnvironmentStatusUpdater
//    @SuppressWarnings("PMD.AvoidDeeplyNestedIfStmts") // agreed, this method needs refactoring.
    @Override
    public List<UDeployEnvResCompData> getEnvironmentResourceStatusData(
            UDeployApplication application, Environment environment) {

        List<UDeployEnvResCompData> environmentStatuses = new ArrayList<>();
                    environmentStatuses.add(buildUdeployEnvResCompData(environment, application, "12.0.40", "File", "Child", "WFNFIT02"));
        return environmentStatuses;
    }

    private UDeployEnvResCompData buildUdeployEnvResCompData(Environment environment, UDeployApplication application, String versionObject, String fileName, String childObject, String string) {
        UDeployEnvResCompData data = new UDeployEnvResCompData();
        String c="";
        data.setEnvironmentName(environment.getName());
        data.setCollectorItemId(application.getId());
        data.setComponentVersion(versionObject);
        data.setAsOfDate(12122017);
        data.setDeployed(false);
        data.setComponentName(fileName);
        data.setOnline(true);
    if(childObject==string)c=string;
            data.setResourceName(c);
        
        return data;
    }




    
}
