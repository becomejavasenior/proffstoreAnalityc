package analytics.model;

public final class AccessToken {

	private final String accessToken;

	public String getAccessToken() {
		return accessToken;
	}

	public AccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	@Override
	public String toString() {
		return "AccessToken [accessToken=" + accessToken + "]";
	}

}
