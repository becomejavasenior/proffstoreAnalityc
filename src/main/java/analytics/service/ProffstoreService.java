package analytics.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONException;
import org.json.JSONObject;

import javax.print.attribute.IntegerSyntax;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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

    public String getCategoriesList() throws UnirestException {
        HttpResponse<JsonNode> jsonResponse  = Unirest.get("https://proffstore.com/api/v1/categories")
                .header("x-client-code", CLIENT_CODE)
                .asJson();
        String response = jsonResponse.getBody().getObject().toString();

        return response;

    }

    public Map<String, Integer> getCategoriesNodeList() throws UnirestException, JSONException {
        HttpResponse<JsonNode> jsonResponse  = Unirest.get("https://proffstore.com/api/v1/categories")
                .header("x-client-code", CLIENT_CODE)
                .asJson();
        String response = jsonResponse.getBody().getObject().toString();
        Map<String, Integer> categories = new HashMap<String, Integer>();

        for(int i = 0; i <jsonResponse.getBody().getObject().getJSONObject("categories").names().length(); i++){
            String catNodeName = jsonResponse.getBody().getObject().getJSONObject("categories").names().getString(i);
            String catName = jsonResponse.getBody().getObject().getJSONObject("categories").getJSONObject(catNodeName).get("name").toString();
            int catId = Integer.parseInt(jsonResponse.getBody().getObject().getJSONObject("categories").getJSONObject(catNodeName).get("id").toString());
            //System.out.println("catName= "+catName+ ": catId=" +catId);
            categories.put(catName,catId);

        }

        return categories;

    }

    public String getAvarageProjectAmount() throws UnirestException, JSONException {
        Map<String, Long> avarageAmount = new HashMap<>();
        Map<String, Integer> categoriesMap = getCategoriesNodeList();
        Collection<Integer> catIds = categoriesMap.values();
        for(Map.Entry<String, Integer> entry:categoriesMap.entrySet()) {
            int categoryId = entry.getValue();
            System.out.println("categoryId="+categoryId);
            HttpResponse<JsonNode> jsonResponse  = Unirest.get("https://proffstore.com/api/v1/tasks?category="+categoryId+"&portion=50")
                    .header("x-client-code", CLIENT_CODE)
                    .asJson();
            if(jsonResponse.getBody().getObject().has("error")) {
                avarageAmount.put(entry.getKey(), 0l);
                continue;
            }
            String total = jsonResponse.getBody().getObject().getJSONObject("paging").get("total").toString();

            int totalInt = Integer.parseInt(total);
            System.out.println("totalInt="+totalInt);
            int counted = 0;
            double totalAmount = 0;
            int page = 1;
            while(totalInt-counted >0){
                HttpResponse<JsonNode> json  = Unirest.get("https://proffstore.com/api/v1/tasks?category="+categoryId+"&portion=50&page="+page)
                        .header("x-client-code", CLIENT_CODE)
                        .asJson();
                for(int i = 0; i <jsonResponse.getBody().getObject().getJSONObject("tasks").names().length(); i++){
                    String taskName = jsonResponse.getBody().getObject().getJSONObject("tasks").names().getString(i);
                    double taskAmount = Double.parseDouble(jsonResponse.getBody().getObject().getJSONObject("tasks").getJSONObject(taskName).get("amount").toString());
                    totalAmount += taskAmount;
                    System.out.println(i);
                }
                System.out.println(totalAmount);
                counted = counted + 50;
                page++;
            }
            avarageAmount.put(entry.getKey(), Math.round(totalAmount/totalInt));
            System.out.println("Category: "+entry.getKey() +", avarage="+String.valueOf(totalAmount/totalInt));

        }
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        String json = gson.toJson(avarageAmount);
        return json;

    }

    public String getTasByCategory() throws UnirestException {
        HttpResponse<JsonNode> jsonResponse  = Unirest.get("https://proffstore.com/api/v1/tasks?category=10&portion=50")
                .header("x-client-code", CLIENT_CODE)
                .asJson();
        String response = jsonResponse.getBody().getObject().toString();

        return response;

    }


}
