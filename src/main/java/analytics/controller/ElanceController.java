package analytics.controller;

import static spark.Spark.*;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
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

		get("/static-elance", (request, response) -> {
			response.type("application/json");
			InputStream is = this.getClass().getResourceAsStream("/elance10-static.json");
			String result = IOUtils.toString(is, Charset.forName("UTF-8"));
			return result;
		});
		
		get("/static-elance-skills", (request, response) -> {
			response.type("application/json");
			InputStream is = this.getClass().getResourceAsStream("/elance-skills.json");
			String result = IOUtils.toString(is, Charset.forName("UTF-8"));
			return result;
		});

		get("/elance", (request, response) -> {
			response.type("application/json");
			File file = new File("elance10.json");

			if (file.exists()) {
				return new String(Files.readAllBytes(Paths.get("elance10.json")));
			}

			System.out.println("GET");
			AccessToken accessToken = request.session().attribute(ACCESS_TOKEN_KEY);
			if (accessToken == null) {
				return null;
			}
			System.out.println(accessToken);

			List<CategoryBudget> categoryBudgetList = new ArrayList<CategoryBudget>();
			final int freePageCount = 10;
			for (int j = 0; j < freePageCount; j++) {
				JSONArray page = elanceService.getJobs(accessToken.getAccessToken(), j);
				for (int i = 0; i < page.length(); i++) {
					JSONObject job = page.getJSONObject(i);
					int budget = job.getInt("budgetMax");
					String category = job.getString("category");
					CategoryBudget categoryBudget = new CategoryBudget(category, budget);
					categoryBudgetList.add(categoryBudget);
				}
			}

			Map<String, List<Long>> averageBudgetMap = new HashMap<String, List<Long>>();
			for (CategoryBudget categoryBudget : categoryBudgetList) {
				if (averageBudgetMap.containsKey(categoryBudget.getCategory())) {
					averageBudgetMap.get(categoryBudget.getCategory()).add(categoryBudget.getBudget());
				} else {
					List<Long> budgetList = new ArrayList<>();
					budgetList.add(categoryBudget.getBudget());
					averageBudgetMap.put(categoryBudget.getCategory(), budgetList);
				}
			}

			List<CategoryBudget> result = new ArrayList<CategoryBudget>();
			for (String category : averageBudgetMap.keySet()) {
				List<Long> budgets = averageBudgetMap.get(category);
				long sum = 0;
				for (Long budget : budgets) {
					sum += budget;
				}
				long average = Math.round((sum * 1.0) / budgets.size());
				result.add(new CategoryBudget(category, average));
			}

			Collections.sort(result, new Comparator<CategoryBudget>() {
				@Override
				public int compare(CategoryBudget o1, CategoryBudget o2) {
					return Long.compare(o2.getBudget(), o1.getBudget());
				}
			});

			// Convert google chart table
				JSONArray googleChartTable = new JSONArray();
				for (CategoryBudget budget : result) {
					JSONArray row = new JSONArray();
					row.put(budget.getCategory());
					row.put(budget.getBudget());
					googleChartTable.put(row);
				}

				Files.write(Paths.get("elance10.json"), googleChartTable.toString().getBytes());

				return googleChartTable.toString();
			});

	}
}
