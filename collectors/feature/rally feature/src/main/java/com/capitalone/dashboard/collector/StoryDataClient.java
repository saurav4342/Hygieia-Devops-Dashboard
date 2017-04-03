package com.capitalone.dashboard.collector;

import com.capitalone.dashboard.datafactory.versionone.VersionOneDataFactoryImpl;
import com.capitalone.dashboard.misc.HygieiaException;
import com.capitalone.dashboard.model.Collector;
import com.capitalone.dashboard.model.Feature;
import com.capitalone.dashboard.repository.FeatureCollectorRepository;
import com.capitalone.dashboard.repository.FeatureRepository;
import com.capitalone.dashboard.util.FeatureCollectorConstants;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the primary implemented/extended data collector for the feature
 * collector. This will get data from the source system, but will grab the
 * majority of needed data and aggregate it in a single, flat MongoDB collection
 * for consumption.
 *
 * @author kfk884
 */
public class StoryDataClient extends BaseClient {
	private static final Logger LOGGER = LoggerFactory.getLogger(StoryDataClient.class);

	private final FeatureSettings featureSettings;
	private final FeatureCollectorRepository featureCollectorRepository;
	private final FeatureRepository featureRepo;

	/**
	 * Extends the constructor from the super class.
	 */
	public StoryDataClient(FeatureSettings featureSettings, FeatureRepository featureRepository,
			FeatureCollectorRepository featureCollectorRepository,
			VersionOneDataFactoryImpl vOneApi) {
		super(vOneApi, featureSettings);
		LOGGER.debug("Constructing data collection for the feature widget, story-level data...");

		this.featureSettings = featureSettings;
		this.featureRepo = featureRepository;
		this.featureCollectorRepository = featureCollectorRepository;
	}

	/**
	 * Updates the MongoDB with a JSONArray received from the source system
	 * back-end with story-based data.
	 *
	 * @param tmpMongoDetailArray
	 *            A JSON response in JSONArray format from the source system
	 *
	 */
	@Override
	@SuppressWarnings({ "unchecked", "PMD.ExcessiveMethodLength", "PMD.NPathComplexity" })
	protected void updateMongoInfo() {
		
			Feature feature = new Feature();

			// collectorId
			feature.setCollectorId(featureCollectorRepository
					.findByName("Rally").getId());

			// ID
			feature.setsId("123456");

			// sNumber
			feature.setsNumber("555555");

			// sName
			feature.setsName("User story to demo hygieia");

			// sStatus
			feature.setsStatus("In-Progress");

			// sState
			feature.setsState("State");

			// sEstimate
			feature.setsEstimate("Estimate");

			// sChangeDate
			feature.setChangeDate("ChangeDate");

			// IsDeleted
			feature.setIsDeleted("False");

			// sProjectID
			
				feature.setsProjectID("project_id");
			

			// sProjectName
			feature.setsProjectName("Project Name");

			// sProjectBeginDate
			feature.setsProjectBeginDate("Start");

			// sProjectEndDate
			feature.setsProjectEndDate("End");

			// sProjectChangeDate
			feature.setsProjectChangeDate("Change");

			// sProjectState
			feature.setsProjectState("In Progress");

			// sProjectIsDeleted
			feature.setsProjectIsDeleted("false");

			// sProjectPath
			String projPath = feature.getsProjectName();
			List <String> list = new ArrayList<>();
			list.add("projlist");
			List<String> projList = list;
			if (!CollectionUtils.isEmpty(projList)) {
				for (String proj : projList) {
					projPath = proj + "-->" + projPath;
				}
				projPath = "All-->" + projPath;
			} else {
				projPath = "All-->" + projPath;
			}
			feature.setsProjectPath(sanitizeResponse(projPath));

			// sEpicID
			
				feature.setsEpicID("12121");
			

			// sEpicNumber
			feature.setsEpicNumber("121211");

			// sEpicName
			feature.setsEpicName("Epic Name");

			// sEpicBeginDate
			feature.setsEpicBeginDate("start");

			// sEpicEndDate
			feature.setsEpicEndDate("end");

			// sEpicType
			feature.setsEpicType("type");

			// sEpicAssetState
			feature.setsEpicAssetState("state");

			// sEpicChangeDate
			feature.setsEpicChangeDate("change");

			// sEpicIsDeleted
			feature.setsEpicIsDeleted("false");

			
				// sSprintID
				
				feature.setsSprintID("sprint id");

				// sSprintName
				feature.setsSprintName("sprint name");

				// sSprintBeginDate
				feature.setsSprintBeginDate("start");

				// sSprintEndDate
				feature.setsSprintEndDate("end");

				// sSprintAssetState
				feature.setsSprintAssetState("asset state");

				// sSprintChangeDate
				feature.setsSprintChangeDate("change");

				// sSprintIsDeleted
				feature.setsSprintIsDeleted("false");

			// sTeamID
			
			feature.setsTeamID("Teamid");

			// sTeamName
			feature.setsTeamName("Team name");

			// sTeamChangeDate
			feature.setsTeamChangeDate("change date");

			// sTeamAssetState
			feature.setsTeamAssetState("asset state");

			// sTeamIsDeleted
			feature.setsTeamIsDeleted("false");

			// sOwnersID
			List<String> ownersIdList = new ArrayList<>();
			
				ownersIdList.add("ownerid");
			
			feature.setsOwnersID(ownersIdList);
List<String> owners = new ArrayList<>();
owners.add("owner name");
owners.add("saurav");
			// sOwnersShortName
			feature.setsOwnersShortName(
					owners);

			// sOwnersFullName
			feature.setsOwnersFullName(
					owners);

			// sOwnersUsername
			feature.setsOwnersUsername(
					owners);

			// sOwnersState
			feature.setsOwnersState(
					owners);

			// sOwnersChangeDate
			feature.setsOwnersChangeDate(
					owners);

			// sOwnersIsDeleted
			feature.setsOwnersIsDeleted(
					owners);

			featureRepo.save(feature);
		}
	

	/**
	 * Explicitly updates queries for the source system, and initiates the
	 * update to MongoDB from those calls.
	 */
	public void updateStoryInformation() throws HygieiaException {
		updateObjectInformation();
	}

	/**
	 * Validates current entry and removes new entry if an older item exists in
	 * the repo
	 *
	 * @param localId
	 *            local repository item ID (not the precise mongoID)
	 */
	protected void removeExistingEntity(String localId) {
		if (StringUtils.isEmpty(localId))
			return;
		List<Feature> listOfFeature = featureRepo.getFeatureIdById(localId);

		if (CollectionUtils.isEmpty(listOfFeature))
			return;
		featureRepo.delete(listOfFeature);
	}

	@Override
	public String getMaxChangeDate() {
		Collector col = featureCollectorRepository.findByName(FeatureCollectorConstants.VERSIONONE);
		if (col == null)
			return "";
		if (StringUtils.isEmpty(featureSettings.getDeltaStartDate()))
			return "";

		List<Feature> response = featureRepo
				.findTopByCollectorIdAndChangeDateGreaterThanOrderByChangeDateDesc(col.getId(),
						featureSettings.getDeltaStartDate());
		if (!CollectionUtils.isEmpty(response))
			return response.get(0).getChangeDate();
		return "";
	}
}
