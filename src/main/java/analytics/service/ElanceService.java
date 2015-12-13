package analytics.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.json.JSONException;
import org.json.JSONObject;

import analytics.model.AccessToken;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class ElanceService {

	private final String authEndpoint;
	private final String clientId;
	private final String redirectUri;

	private final String clientSecret;
	private final String accessTokenUrl;

	public ElanceService() {
		Properties properties = new Properties();
		InputStream propertiesInputStream = ElanceService.class.getClassLoader().getResourceAsStream("elance.properties");
		try {
			properties.load(propertiesInputStream);
			propertiesInputStream.close();
		} catch (IOException e) {
			System.out.println("Cannot load Elance properties.");
			new RuntimeException(e);
		}

		authEndpoint = properties.getProperty("auth.endpoint");
		clientId = properties.getProperty("auth.clientId");
		redirectUri = properties.getProperty("auth.redirectUri");

		accessTokenUrl = properties.getProperty("auth.accessToken.url");
		clientSecret = properties.getProperty("auth.clientSecret");

	}

	public String getAuthorizationUrl() {
		StringBuilder builder = new StringBuilder();
		builder.append(authEndpoint).append("?client_id=").append(clientId);
		builder.append("&redirect_uri=").append(redirectUri);
		builder.append("&response_type=code");
		return builder.toString();

	}

	// {
	// "code": 400,
	// "error_type": "OAuthException",
	// "error_message": "You must provide a client_secret"
	// }
	public AccessToken exchangeCode(String code) {
		StringBuilder bodyBuilder = new StringBuilder();
		bodyBuilder.append("client_id=").append(clientId);
		bodyBuilder.append("&client_secret=").append(clientSecret);
		bodyBuilder.append("&grant_type=authorization_code");
		bodyBuilder.append("&redirect_uri=").append(redirectUri);
		bodyBuilder.append("&code=").append(code);
		try {
			HttpResponse<JsonNode> response = Unirest.post(accessTokenUrl).header("Content-Type", "application/x-www-form-urlencoded").body(bodyBuilder.toString()).asJson();
			int responseStatus = response.getStatus();
			if (responseStatus != 200) {
				throw new RuntimeException(response.getStatusText());
			}
			JsonNode responseBody = response.getBody();
			JSONObject json = responseBody.getObject();
			JSONObject data = json.getJSONObject("data");
			String accessTokenString = data.getString("access_token");
			return new AccessToken(accessTokenString);

		} catch (UnirestException | JSONException e) {
			throw new RuntimeException("Server error during Elance auth", e);
		}
	}

	public String getJobs(String accessToken, int i) {
		StringBuilder builder = new StringBuilder();
		builder.append("https://api.elance.com/api2/jobs?access_token=");
		builder.append(accessToken);
		// TODO: append keywords
		// builder.append("&keywords=").append(keywords);
		// example: keywords=php
		String url = builder.toString();
		try {
			HttpResponse<JsonNode> jsonResponse = Unirest.get(url).asJson();
			int responseStatus = jsonResponse.getStatus();
			if (responseStatus != 200) {
				throw new RuntimeException(jsonResponse.getStatusText());
			}
			String response = jsonResponse.getBody().getObject().toString();
			return response;
		} catch (UnirestException e) {
			throw new RuntimeException("Cannot get elance jobs", e);
		}

	}
}
