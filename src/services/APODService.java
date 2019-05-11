package services;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.APODBean;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class APODService {
    private static final String endpoint = "https://api.nasa.gov/planetary/apod?api_key=";
    private static final String apiKey = "MlYLzw4KhLpIhVZEL3ly4ZItTFfefzYvDpnFcIlb";

    private static final String url = endpoint + apiKey;

    public static APODBean getAPOD() throws IOException {
        APODBean apodBean = null;

        //Build HTTP client
        HttpClientBuilder hcBuilder = HttpClients.custom();
        HttpClient client = hcBuilder.build();

        //Build HTTP Get Request
        HttpGet request = new HttpGet(url);

        //Setting header parameters
        request.addHeader("Content-Type", "application/json");
        request.addHeader("Accept", "application/json");

        //Executing the call
        HttpResponse response = client.execute(request);

        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

        //Reading the response
        StringBuilder result = new StringBuilder();
        String line = "";
        while ((line = reader.readLine()) != null)
            result.append(line);

        //convert result String of JSON to a Bean
        ObjectMapper mapper = new ObjectMapper();

        /*Si en el Bean no incluyo todas las propiedades que me regresan el JSON el mapper va a crashear, por eso
         * se le pasa este flag, para que las ignore.
         * En este caso no me interesa el hdurl.
         */
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        apodBean = mapper.readValue(result.toString(), APODBean.class);

        return apodBean;
    }
}
