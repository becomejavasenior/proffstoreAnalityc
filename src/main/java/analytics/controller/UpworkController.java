package analytics.controller;

import static spark.Spark.*;
import analytics.service.UpworkService;

public class UpworkController {
	private UpworkService upworkService;

	public UpworkController(UpworkService upworkService) {
		this.upworkService = upworkService;
		initializeRoutes();
	}

	private void initializeRoutes() {
		before("/upwork", (request, response) -> {
			// TODO: Authentication via UpWork
		});
		
		// TODO: add API GET/POST method(routes)
		// with analytics from UpWork
	}
}
