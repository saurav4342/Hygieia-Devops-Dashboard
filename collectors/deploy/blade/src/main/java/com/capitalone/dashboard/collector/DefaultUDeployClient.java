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

    private List<String> getPhysicalFileList(UDeployApplication application, JSONObject versionObject) {
        List<String> list = new ArrayList<>();
        String fileTreeUrl = "deploy/version/" + str(versionObject, "id") + "/fileTree";
        ResponseEntity<String> fileTreeResponse = makeRestCall(
                application.getInstanceUrl(), fileTreeUrl);
        JSONArray fileTreeJson = paresAsArray(fileTreeResponse);
        for (Object f : fileTreeJson) {
            JSONObject fileJson = (JSONObject) f;
            list.add(cleanFileName(str(fileJson, "name"), str(versionObject, "name")));
        }
        return list;
    }

    private Set<String> getFailedComponents(JSONArray nonCompliantResourceJSON) {
        HashSet<String> failedComponents = new HashSet<>();
        for (Object nonCompItem : nonCompliantResourceJSON) {
            JSONArray nonCompChildrenArray = (JSONArray) ((JSONObject) nonCompItem)
                    .get("children");
            for (Object nonCompChildItem : nonCompChildrenArray) {
                JSONObject nonCompChildObject = (JSONObject) nonCompChildItem;
                JSONObject nonCompVersionObject = (JSONObject) nonCompChildObject
                        .get("version");
                if (nonCompVersionObject == null) continue;
                JSONObject nonCompComponentObject =
                        (JSONObject) nonCompVersionObject.get("component");
                if (nonCompComponentObject != null) {
                    failedComponents.add(str(nonCompComponentObject, "name"));
                }
            }
        }
        return failedComponents;
    }

    private String cleanFileName(String fileName, String version) {
        if (fileName.contains("-" + version))
            return fileName.replace("-" + version, "");
        if (fileName.contains(version))
            return fileName.replace(version, "");
        return fileName;
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


    private JSONArray getLowestLevelChildren(JSONObject topParent, JSONArray returnArray) {
        JSONArray jsonChildren = (JSONArray) topParent.get("children");

        if (jsonChildren != null && jsonChildren.size() > 0) {
            for (Object child : jsonChildren) {
                if (!hasChildren((JSONObject) child)) {
                    returnArray.add(child);
                } else {
                    getLowestLevelChildren((JSONObject) child, returnArray);
                }
            }
        }
        return returnArray;
    }

    private boolean hasChildren(JSONObject object) {
        return (boolean) object.get("hasChildren");
    }
    // ////// Helpers

    private ResponseEntity<String> makeRestCall(String instanceUrl,
                                                String endpoint) {
        String url = normalizeUrl(instanceUrl, "/rest/" + endpoint);
        ResponseEntity<String> response = null;
        try {
            response = restOperations.exchange(url, HttpMethod.GET,
                    new HttpEntity<>(createHeaders()), String.class);

        } catch (RestClientException re) {
            LOGGER.error("Error with REST url: " + url);
            LOGGER.error(re.getMessage());
        }
        return response;
    }

    private String normalizeUrl(String instanceUrl, String remainder) {
        return StringUtils.removeEnd(instanceUrl, "/") + remainder;
    }

    protected HttpHeaders createHeaders() {
        String auth = uDeploySettings.getUsername() + ":"
                + uDeploySettings.getPassword();
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(
                StandardCharsets.US_ASCII));
        String authHeader = "Basic " + new String(encodedAuth);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);
        return headers;
    }

    private JSONArray paresAsArray(ResponseEntity<String> response) {
        if (response == null)
            return new JSONArray();
        try {
            return (JSONArray) new JSONParser().parse(response.getBody());
        } catch (ParseException pe) {
            LOGGER.debug(response.getBody());
            LOGGER.error(pe.getMessage());
        }
        return new JSONArray();
    }

    private String str(JSONObject json, String key) {
        Object value = json.get(key);
        return value == null ? null : value.toString();
    }

    private long date(JSONObject jsonObject, String key) {
        Object value = jsonObject.get(key);
        return value == null ? 0 : (long) value;
    }
}
