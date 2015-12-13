package analytics.controller;

import static spark.Spark.*;

import java.util.HashMap;
import java.util.Map;

import spark.ModelAndView;
import spark.template.freemarker.FreeMarkerEngine;
import freemarker.template.Configuration;

public class MainController {

	private static final FreeMarkerEngine freeMarkerEngine = new FreeMarkerEngine(createFreemarkerConfiguration());

	/**
	 * FreeMarker template engine configuration
	 * 
	 * @return configuration of FreeMarker
	 */
	private static Configuration createFreemarkerConfiguration() {
		Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);
		cfg.setClassForTemplateLoading(MainController.class, "/freemarker");
		cfg.setDefaultEncoding("UTF-8");
		return cfg;
	}

	public MainController() {
		initializeRoutes();
	}

	private void initializeRoutes() {
		/**
		 * Index page
		 */
		get("/", (request, response) -> {
			Map<String, Object> attributes = new HashMap<>();
			// TODO: set template variables to the 'attributes' map
				return new ModelAndView(attributes, "proffstore.html");
			}, freeMarkerEngine);
	}
}
