package analytics.controller;

import static spark.Spark.*;

import org.json.JSONArray;

import analytics.service.ProffstoreService;

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

		get("/getAccessToken", (request, response) -> {
			return proffstoreService.getAccessToken();
		});
		get("/getTaskList", (request, response) -> {
			return proffstoreService.getTaskList();
		});

		get("/getCategoriesList", (request, response) -> {
			return proffstoreService.getCategoriesList();
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
