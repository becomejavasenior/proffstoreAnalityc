package analytics.controller;

import static spark.Spark.*;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import analytics.model.AccessToken;
import analytics.model.CategoryBudget;
import analytics.service.ElanceService;

public class ElanceController {
	private static final String ACCESS_TOKEN_KEY = "elance_access_token";

	private ElanceService elanceService;

	public ElanceController(ElanceService elanceService) {
		this.elanceService = elanceService;
		initializeRoutes();
	}

	private void initializeRoutes() {
		before("/elance", (request, response) -> {
			System.out.println("BEFORE");
			AccessToken accessTokenString = request.session().attribute(ACCESS_TOKEN_KEY);
			if (accessTokenString == null) {
				final String code = request.queryParams("code");
				if (code == null) {

					String error = request.queryParams("error");

					if (error == null) {
						response.redirect(elanceService.getAuthorizationUrl());
					} else {
						halt(401, error);
					}
				} else {
					AccessToken accessToken = elanceService.exchangeCode(code);
					request.session().attribute(ACCESS_TOKEN_KEY, accessToken);
				}
			}
		});

		get("/elance", (request, response) -> {
			System.out.println("GET");
			AccessToken accessToken = request.session().attribute(ACCESS_TOKEN_KEY);
			if (accessToken == null) {
				return null;
			}
			System.out.println(accessToken);
			response.type("application/json");

			List<CategoryBudget> categoryBudgetList = new ArrayList<CategoryBudget>();
			final int freePageCount = 10;
			for (int j = 0; j < freePageCount; j++) {
				JSONArray page = elanceService.getJobs(accessToken.getAccessToken(), j);
				for (int i = 0; i < page.length(); i++) {
					JSONObject job = page.getJSONObject(i);
					int budget = job.getInt("maxBudget");
					String category = job.getString("category");
					CategoryBudget categoryBudget = new CategoryBudget(category, budget);
					categoryBudgetList.add(categoryBudget);
				}
			}
			

				return elanceService.getJobs(accessToken.getAccessToken(), 0);
			});

	}
}
