package analytics.controller;

import static spark.Spark.*;


import org.json.JSONArray;

import org.json.JSONObject;

import analytics.service.ProffstoreService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ProffstoreController {

	private ProffstoreService proffstoreService;

	public ProffstoreController(ProffstoreService proffstoreService) {
		this.proffstoreService = proffstoreService;
		initializeRoutes();
	}

	private void initializeRoutes() {
		before("/proffstore", (request, response) -> {
			// TODO: authentication via Proffstore API

			});

		get("proffstore/getAccessToken", (request, response) -> {
			return proffstoreService.getAccessToken();
		});
		get("proffstore/getTaskList", (request, response) -> {
			return proffstoreService.getTaskList();
		});

		get("proffstore/getCategoriesList", (request, response) -> {

			Gson gson = new GsonBuilder()
					.setPrettyPrinting()
					.create();
			String json = gson.toJson(proffstoreService.getCategoriesNodeList());
			return  json;
		});
		get("proffstore/getAvarageProjectAmount", (request, response) -> {
			String jsonString = proffstoreService.getAvarageProjectAmount();
			
			// Convert to other Google chart view
			JSONArray googleChartTable = new JSONArray();
			JSONObject jsonObject = new JSONObject(jsonString);
			JSONArray names = jsonObject.names();
			for (int i = 0; i < names.length(); i++) {
				String name = names.getString(i);
				Object value = jsonObject.get(name);
				JSONArray row = new JSONArray();
				row.put(name);
				row.put(value);
				googleChartTable.put(row);
			}
			
			response.type("application/json");
			return googleChartTable.toString();
		});
		get("proffstore/getTasByCategory", (request, response) -> {
			response.type("application/json");
			return proffstoreService.getTasByCategory();
		});

		get("/proffstore/stats", (request, response) -> {
			JSONArray categories = new JSONArray();
			for (int i = 0; i < 10; i++) {
				JSONArray category = new JSONArray();
				category.put("Category " + i);
				category.put(i * 1000);
				categories.put(category);
			}

			response.type("application/json");
			return categories;
		});
		// TODO: add API GET/POST method(routes)
		// with analytics from Proffstore
	}
}
