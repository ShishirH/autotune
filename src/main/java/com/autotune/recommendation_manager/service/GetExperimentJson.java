package com.autotune.recommendation_manager.service;

import com.autotune.dependencyAnalyzer.deployment.AutotuneDeployment;
import com.autotune.dependencyAnalyzer.util.DAConstants;
import com.autotune.recommendation_manager.ApplicationSearchSpace;
import com.autotune.recommendation_manager.ApplicationTunable;
import com.autotune.recommendation_manager.RecommendationManager;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GetExperimentJson extends HttpServlet
{
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String id = req.getParameter(DAConstants.AutotuneObjectConstants.ID);

		JSONArray outputJsonArray = new JSONArray();
		resp.setContentType("application/json");

		getExperiment(outputJsonArray, id);
		resp.getWriter().println(outputJsonArray.toString(4));
	}

	public void getExperiment(JSONArray outputJsonArray, String id) {
		if (id == null) {
			//No application parameter, generate search space for all applications
			for (String applicationID : RecommendationManager.applicationSearchSpaceMap.keySet()) {
				addExperiment(outputJsonArray, applicationID);
			}
		} else {
			if (RecommendationManager.applicationSearchSpaceMap.containsKey(id)) {
				addExperiment(outputJsonArray, id);
			}
		}

		if (outputJsonArray.isEmpty()) {
			if (AutotuneDeployment.autotuneObjectMap.isEmpty())
				outputJsonArray.put("Error: No objects of kind Autotune found!");
			else
				outputJsonArray.put("Error: Application " + id + " not found!");
		}
	}

	private void addExperiment(JSONArray outputJsonArray, String id) {
		JSONObject jsonObject = new JSONObject();
		ApplicationSearchSpace applicationSearchSpace = RecommendationManager.applicationSearchSpaceMap.get(id);

		String applicationID = applicationSearchSpace.getId();;
		String name = applicationSearchSpace.getApplicationName();

		//TODO Replace trialNum hardcoding
		int trialNum = 1;

		jsonObject.put("id", applicationID);
		jsonObject.put("application_name", name);
		jsonObject.put("trial_num", trialNum);

		JSONArray updateConfigJson = new JSONArray();

		for (String tunableName : RecommendationManager.tunablesMap.get(id).keySet()) {
			JSONObject tunableJson = new JSONObject();
			tunableJson.put("tunable_name", tunableName);
			tunableJson.put("tunable_value", RecommendationManager.tunablesMap.get(id).get(tunableName));
			updateConfigJson.put(tunableJson);
		}

		JSONArray queriesJsonArray = new JSONArray();
		for (ApplicationTunable applicationTunable : applicationSearchSpace.getApplicationTunables()) {
			JSONObject queryJson = new JSONObject();
			queryJson.put("tunable_name", applicationTunable.getName());
			queryJson.put("query_url", applicationTunable.getQueryURL());
			queriesJsonArray.put(queryJson);
		}

		jsonObject.put("update_config", updateConfigJson);
		jsonObject.put("queries", queriesJsonArray);
		outputJsonArray.put(jsonObject);
	}
}
