package analitycs;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.Test;

/**
 * Created by apolonxviii on 13.12.15.
 */

public class ProffstoreServiceTest {

    private static final String CLIENT_CODE = "25c05ed2daad";
    @Test
    public void testAuth(){

    }

    private void initializeRoutes() throws UnirestException {
        HttpResponse<JsonNode> jsonResponse  = Unirest.post("https://proffstore.com/api/v1/token")
                .header("x-client-code", CLIENT_CODE)
                .asJson();
        System.out.println("jsonResponse="+jsonResponse);


    }
}
