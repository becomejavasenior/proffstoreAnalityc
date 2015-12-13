package analytics.controller;

import static spark.Spark.*;
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
		// TODO: add API GET/POST method(routes)
		// with analytics from Proffstore
	}
}
