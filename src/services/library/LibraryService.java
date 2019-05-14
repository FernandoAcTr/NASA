package services.library;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import services.RequestException;
import services.Service;

import java.io.IOException;

public class LibraryService {

   private final String baseURl = "https://images-api.nasa.gov/search?q=";

    public String getURL(String search, String page, boolean image, boolean video, boolean audio, String yearStart, String yearEnd) throws IOException, RequestException {

        String url = baseURl;

        //image, video and audio
        //String url = "https://images-api.nasa.gov/search?q=Orion&page=3&media_type=image,video,audio&year_start=1920&year_end=2019";

        url += search.replaceAll(" ", "%20");
        url += "&page=" + page;
        url += "&media_type=";
        if(image) url += "image";
        if(video) url += ",video";
        if(audio) url += ",audio";
        url += "&year_start=" + yearStart;
        url += "&year_end=" + yearEnd;

        return url;
    }

    public String getRequest(String url) throws IOException, RequestException {
        return Service.getRequest(url);
    }

    public LibraryCollection getLibraryCollection(String JSONResult) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        LibraryCollection library = mapper.readValue(JSONResult, LibraryCollection.class);

        return library;
    }

}
