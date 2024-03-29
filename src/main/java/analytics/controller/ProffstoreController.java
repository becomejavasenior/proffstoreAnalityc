package analytics.controller;

import static spark.Spark.*;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import analytics.model.CategoryBudget;
import analytics.model.SkillPopularity;
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

			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String json = gson.toJson(proffstoreService.getCategoriesNodeList());
			return json;
		});
		get("proffstore/getAvarageProjectAmount", (request, response) -> {
			String jsonString = proffstoreService.getAvarageProjectAmount();

			// Convert to list
				JSONObject jsonObject = new JSONObject(jsonString);
				JSONArray names = jsonObject.names();
				List<CategoryBudget> categoryBudgetList = new ArrayList<CategoryBudget>(names.length());
				for (int i = 0; i < names.length(); i++) {
					String name = names.getString(i);
					long value = jsonObject.getLong(name);
					CategoryBudget categoryBudget = new CategoryBudget(name, value);
					categoryBudgetList.add(categoryBudget);
				}

				// Sort
				Collections.sort(categoryBudgetList, new Comparator<CategoryBudget>() {
					@Override
					public int compare(CategoryBudget o1, CategoryBudget o2) {
						return Long.compare(o2.getBudget(), o1.getBudget());
					}
				});

				// Convert google chart table
				JSONArray googleChartTable = new JSONArray();
				for (CategoryBudget categoryBudget : categoryBudgetList) {
					JSONArray row = new JSONArray();
					row.put(categoryBudget.getCategory());
					row.put(categoryBudget.getBudget());
					googleChartTable.put(row);
				}

				response.type("application/json;charset=utf-8");
				return googleChartTable.toString();
			});
		get("proffstore/getTasByCategory", (request, response) -> {
			response.type("application/json");
			return proffstoreService.getTasByCategory();
		});
		
		
		get("freelancer/getAvarageProjectAmount", (request, response) -> {
			response.type("application/json");
			InputStream is = this.getClass().getResourceAsStream("/freelance-budget.json");
			String result = IOUtils.toString(is, Charset.forName("UTF-8"));
			return result;
		});
		get("freelancer/getSkillsPopularity", (request, response) -> {
			response.type("application/json");
			InputStream is = this.getClass().getResourceAsStream("/freelance-skills.json");
			String result = IOUtils.toString(is, Charset.forName("UTF-8"));
			return result;
		});
		
		
		get("proffstore/getSkillsPopularity", (request, response) -> {
			response.type("application/json;charset=utf-8");
			String jsonString = proffstoreService.getSkillsPopularity();

			JSONObject jsonObject = new JSONObject(jsonString);
			JSONArray names = jsonObject.names();
			List<SkillPopularity> skillPopularityList = new ArrayList<SkillPopularity>(names.length());
			for (int i = 0; i < names.length(); i++) {
				String name = names.getString(i);
				long value = jsonObject.getLong(name);
				SkillPopularity skillPopularity = new SkillPopularity(name, value);
				skillPopularityList.add(skillPopularity);
			}

			// Sort
				Collections.sort(skillPopularityList, new Comparator<SkillPopularity>() {
					@Override
					public int compare(SkillPopularity o1, SkillPopularity o2) {
						return Long.compare(o2.getPopularity(), o1.getPopularity());
					}
				});
				
				
				int toIndex = skillPopularityList.size() > 15? 15: skillPopularityList.size();
				skillPopularityList = skillPopularityList.subList(0, toIndex);
				

				// Convert google chart table
				JSONArray googleChartTable = new JSONArray();
				for (SkillPopularity skillPopularity : skillPopularityList) {
					JSONArray row = new JSONArray();
					row.put(skillPopularity.getSkill());
					row.put(skillPopularity.getPopularity());
					googleChartTable.put(row);
				}

				return googleChartTable.toString();
			});
		get("proffstore/getAvarageUserRate", (request, response) -> {
			response.type("application/json");
			return proffstoreService.getAvarageUserRate();
		});
		get("proffstore/getUserList", (request, response) -> {
			response.type("application/json");
			return proffstoreService.getUserList();
		});

		/**
		 * Fake data
		 */
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
