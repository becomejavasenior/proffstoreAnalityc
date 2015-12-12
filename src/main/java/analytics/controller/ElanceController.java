package analytics.controller;

import static spark.Spark.*;
import freemarker.core.ReturnInstruction.Return;
import spark.Request;
import spark.Response;
import analytics.json.JsonTransformer;
import analytics.model.AccessToken;
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
			authenticate(request, response);
		});

		get("/elance", (request, response) -> {
			AccessToken accessTokenString = request.session().attribute(ACCESS_TOKEN_KEY);
			return accessTokenString.getAccessToken();
		}, new JsonTransformer());

	}

	private void authenticate(Request request, Response response) {
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
	}
}
