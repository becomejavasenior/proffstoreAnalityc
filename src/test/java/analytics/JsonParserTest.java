package analytics;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Stream;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import analytics.utils.JsonParser;
import junit.framework.Assert;

public class JsonParserTest {

	private final static double averageBugetSum = 517.5;
	private final static double[] averageBugetsPerCategory = { 500, 0, 0, 0, 17.5 };

	private static JSONObject readJsonFile(String fileName) throws JSONException {
		StringBuilder result = new StringBuilder("");
		ClassLoader classLoader = JsonParser.class.getClassLoader();
		File file = new File(classLoader.getResource(fileName).getFile());
		try (Scanner scanner = new Scanner(file)) {

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				result.append(line).append("\n");
			}
			scanner.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new JSONObject(result.toString());
	}

	@Test
	public void getAverageBudgetPerCategory() {
		try {
			Assert.assertEquals(JsonParser.getCategories(readJsonFile("json.txt")).size(),
					JsonParser.getAverageBudgetPerCategory(JsonParser.getCategories(readJsonFile("json.txt")),
							readJsonFile("json.txt")).size());
			Assert.assertEquals(JsonParser.getCategories(readJsonFile("json15cat.txt")).size(),
					JsonParser.getAverageBudgetPerCategory(JsonParser.getCategories(readJsonFile("json15cat.txt")),
							readJsonFile("json15cat.txt")).size());
			Assert.assertEquals(JsonParser.getCategories(readJsonFile("json6cat.txt")).size(),
					JsonParser.getAverageBudgetPerCategory(JsonParser.getCategories(readJsonFile("json6cat.txt")),
							readJsonFile("json6cat.txt")).size());
			Map<String, Double> averageBudgetsMap = JsonParser.getAverageBudgetPerCategory(
					JsonParser.getCategories(readJsonFile("json6cat.txt")), readJsonFile("json6cat.txt"));
			Assert.assertEquals(averageBugetSum, sum(averageBudgetsMap));
			Assert.assertTrue(isEqual(averageBudgetsMap.values(), averageBugetsPerCategory));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private boolean isEqual(Collection<Double> values, double[] averageBugetsPerCategory) {
		int count = 0;
		for (double d : values) {
			if (d == averageBugetsPerCategory[count]) {
				count++;
				continue;
			} else {
				return false;
			}
		}
		return true;
	}

	private double sum(Map<String, Double> averageBudgetsMap) {
		return averageBudgetsMap.values().stream().mapToDouble(d -> 1 * d).sum();
	}
}
