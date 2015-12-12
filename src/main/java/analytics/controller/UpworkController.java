package analytics.controller;

import static spark.Spark.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Properties;

import org.json.JSONException;
import org.json.JSONObject;

import spark.Request;
import spark.Response;
import analytics.json.JsonTransformer;
import analytics.service.UpworkService;

import com.Upwork.api.Config;
import com.Upwork.api.OAuthClient;
import com.Upwork.api.Routers.Organization.Users;

public class UpworkController {
	private UpworkService upworkService;
	private Properties upworkProperties;

	public UpworkController(UpworkService upworkService) {
		Properties upworkProperties = new Properties();
		InputStream propertiesInputStream = UpworkController.class.getClassLoader()
				.getResourceAsStream("upwork.properties");
		try {
			upworkProperties.load(propertiesInputStream);
			propertiesInputStream.close();
		} catch (IOException e) {
			System.out.println("Cannot load upwork.properties");
			throw new RuntimeException(e);
		}

		this.upworkProperties = upworkProperties;
		this.upworkService = upworkService;
		initializeRoutes();
	}

	private void initializeRoutes() {
		before("/upwork", (request, response) -> {
			// TODO: Authentication via UpWork
				authenticate(request, response);
			});

		get("/upwork", (request, response) -> {
			OAuthClient client = new OAuthClient(new Config(upworkProperties));
			String aToken = request.session().attribute("upwork_token");
			String aSecret = request.session().attribute("upwork_secret");
			client.setTokenWithSecret(aToken, aSecret);

			JSONObject userInfoJson = null;
			try {
				// Get info of authenticated user
				Users users = new Users(client);
				userInfoJson = users.getMyInfo();

				// get my uid
				String myId = null;
				try {
					JSONObject user = userInfoJson.getJSONObject("user");
					myId = user.getString("id");
					System.out.println(myId);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			response.type("application/json");
			return userInfoJson.toString();
		});

	}

	private void authenticate(Request request, Response response) {
		String aToken = request.session().attribute("upwork_token");
		String aSecret = request.session().attribute("upwork_secret");

		OAuthClient client = new OAuthClient(new Config(upworkProperties));

		// authorize application and get access token
		if (aToken == null && aSecret == null) {

			final String oauth_verifier = request.queryParams("oauth_verifier");
			if (oauth_verifier == null) {
				String authzUrl = client.getAuthorizationUrl();
				System.out.println(authzUrl);
				response.redirect(authzUrl);
			} else {
				String verifier = null;
				try {
					verifier = URLDecoder.decode(oauth_verifier, "UTF-8");
				} catch (Exception e) {
					e.printStackTrace();
				}

				HashMap<String, String> token = client.getAccessTokenSet(verifier);

				request.session().attribute("upwork_token", token.get("token"));
				request.session().attribute("upwork_secret", token.get("secret"));
				System.out.println(token);
			}
		}
	}
}
