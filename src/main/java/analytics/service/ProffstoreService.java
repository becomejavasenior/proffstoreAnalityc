package analytics.service;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONException;

public class ProffstoreService {

    private static final String CLIENT_CODE = "4bb6c9322667";
    private static String access_token = "";


    public String getAccessToken() throws UnirestException, JSONException {
        HttpResponse<JsonNode> jsonResponse  = Unirest.post("https://proffstore.com/api/v1/token")
                .header("x-client-code", CLIENT_CODE)
                .field("email", "apolonxviii@gmail.com")
                .field("pass", "ploqploq2")
                .asJson();
        String response = jsonResponse.getBody().getObject().toString();
        //TODO validate if error
        if(!jsonResponse.getBody().getObject().toString().equalsIgnoreCase("")) {
            access_token = jsonResponse.getBody().getObject().getString("access_token").toString();
        }
        return access_token;

    }
    public String getTaskList() throws UnirestException {
        HttpResponse<JsonNode> jsonResponse  = Unirest.get("https://proffstore.com/api/v1/tasks")
                .header("x-client-code", CLIENT_CODE)
                .asJson();
        String response = jsonResponse.getBody().getObject().toString();

        return response;

    }

}
