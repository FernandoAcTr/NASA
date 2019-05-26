package services.apod;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import controller.apod.APODBean;
import services.RequestException;
import services.Service;

import java.io.IOException;

public class APODService {

    private static final String endpoint = "https://api.nasa.gov/planetary/apod?api_key=";
    private static final String apiKey = "MlYLzw4KhLpIhVZEL3ly4ZItTFfefzYvDpnFcIlb";

    private static final String url = endpoint + apiKey;

    /**
     * Make a HTTP Request to he APOD Service
     * @return
     * @throws IOException
     * @throws RequestException
     */
    public static APODBean getAPOD() throws IOException, RequestException {
        APODBean apodBean = null;

        String JSONRes = Service.getRequest(url);

        if(JSONRes != null) { //this code indicate that all is fine

            //convert result String of JSON to a Bean
            ObjectMapper mapper = new ObjectMapper();

            /*Si en el Bean no incluyo todas las propiedades que me regresan el JSON el mapper va a crashear, por eso
             * se le pasa este flag, para que las ignore.
             * En este caso no me interesa el hdurl.
             */
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

            apodBean = mapper.readValue(JSONRes, APODBean.class);
        }

        return apodBean;
    }
}
