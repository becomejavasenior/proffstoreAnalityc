package analytics.utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonParser {

	private final static Pattern p = Pattern.compile("-?\\d+");

	public static Set<String> getCategories(JSONObject jsonObject) throws JSONException {
		Set<String> result = new LinkedHashSet<>();
		for (int i = 0; i < jsonObject.getJSONObject("data").getJSONArray("pageResults").length(); i++) {
			String categoryName = jsonObject.getJSONObject("data").getJSONArray("pageResults").getJSONObject(i)
					.getString("category");
			result.add(categoryName);
		}
		return result;
	}

	public static Map<String, Double> getAverageBudgetPerCategory(Set<String> categories, JSONObject jsonObject)
			throws JSONException {
		Map<String, Double> result = new LinkedHashMap<>();
		for (String etalon : categories) {
			double averageBudget = 0;
			int count = 0;
			for (int i = 0; i < jsonObject.getJSONObject("data").getJSONArray("pageResults").length(); i++) {
				String categoryName;
				try {
					categoryName = jsonObject.getJSONObject("data").getJSONArray("pageResults").getJSONObject(i)
							.getString("category");
					if (categoryName.equalsIgnoreCase(etalon)) {
						double budget = getBudget(
								jsonObject.getJSONObject("data").getJSONArray("pageResults").getJSONObject(i));
						if (budget > 0) {
							count++;
						}
						averageBudget += budget;
					}
				} catch (Exception e) {
					continue;
				}
			}
			if (count == 0) {
				averageBudget = 0;
			} else {
				averageBudget /= count;
			}
			result.put(etalon, averageBudget);
		}
		return result;
	}

	private static double getBudget(JSONObject jsonObject) throws JSONException {
		double result = 0;
		try {
			result = Double.parseDouble(jsonObject.getString("budget"));
		} catch (NumberFormatException e) {
			result = getAverage(jsonObject.getString("budget"));
		}
		return result;
	}

	private static double getAverage(String string) {
		List<Double> numbers = new ArrayList<>();
		Matcher m = p.matcher(string);
		while (m.find()) {
			try {
				numbers.add(Double.parseDouble(m.group()));
			} catch (NumberFormatException e) {
				continue;
			}
		}
		return numbers.size() > 0 ? numbers.stream().mapToDouble(d -> 1 * d).sum() / numbers.size() : 0d;
	}

}
