package analytics;

import static spark.Spark.*;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

import analytics.controller.ElanceController;
import analytics.controller.MainController;
import analytics.controller.ProffstoreController;
import analytics.controller.UpworkController;
import analytics.service.ElanceService;
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
		 * Database initialization
		 */
		//MongoDatabase database = getDatabase();

		/**
		 * Controllers initialization
		 */
		// Controller with static pages
		// (HTML)
		new MainController();

		// Controllers with REST APIs
		// (JSON)
		new ElanceController(new ElanceService());
		new UpworkController(new UpworkService());
		new ProffstoreController(new ProffstoreService());
	}

	// TODO: set local Database name and Heroku database name into properties
	/*
	public static MongoDatabase getDatabase() {
		final String mongoClientUri;
		final String databaseName;
		final String mongoLabUri = System.getenv().get("MONGOLAB_URI");
		if (mongoLabUri == null) {
			mongoClientUri = "mongodb://localhost:27017/proffstore_analytics_db";
			databaseName = "frisbee";
		} else {
			mongoClientUri = mongoLabUri;
			databaseName = "heroku_q1k9ht7d";
		}
		final MongoClient client = new MongoClient(new MongoClientURI(mongoClientUri));
		final MongoDatabase database = client.getDatabase(databaseName);
		return database;
	}
	*/
}
