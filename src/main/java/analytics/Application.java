package analytics;

import static spark.Spark.*;
import analytics.controller.MainController;
import analytics.controller.ProffstoreController;
import analytics.controller.UpworkController;
import analytics.service.ProffstoreService;
import analytics.service.UpworkService;

public class Application {

	/**
	 * Heroku integration
	 * 
	 * @return Heroku port number
	 */
	static int getHerokuAssignedPort() {
		ProcessBuilder processBuilder = new ProcessBuilder();
		if (processBuilder.environment().get("PORT") != null) {
			return Integer.parseInt(processBuilder.environment().get("PORT"));
		}
		return 4567; // return default port if heroku-port isn't set (i.e. on
						// localhost)
	}

	public static void main(String[] args) {
		/**
		 * Heroku integration
		 */
		port(getHerokuAssignedPort());

		/**
		 * CSS, JS files location
		 */
		staticFileLocation("/public");

		/**
		 * Global exception handler
		 */
		exception(Exception.class, (e, request, response) -> {
			response.status(500);
			e.printStackTrace();
			response.body(e.getMessage());
		});

		/**
		 * Controllers initialization
		 */
		// Controller with static pages
		// (HTML)
		new MainController();

		// Controllers with REST APIs
		// (JSON)
		new UpworkController(new UpworkService());
		new ProffstoreController(new ProffstoreService());
	}
}
