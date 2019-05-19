package services.techport;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import services.RequestException;
import services.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TechportService {

    private final String endpointProjectsList;
    private final String apiKey;
    private final String endpointProject;

    public TechportService(){
        apiKey = "MlYLzw4KhLpIhVZEL3ly4ZItTFfefzYvDpnFcIlb";
        endpointProjectsList = "https://api.nasa.gov/techport/api/projects?api_key=" + apiKey;
        endpointProject = "https://api.nasa.gov/techport/api/projects/id_project?api_key=" + apiKey;

    }

    public String getListRequest() throws RequestException, IOException {
        return Service.getRequest(endpointProjectsList);
    }

    public ArrayList<Integer> getAllProjectsID(String JSONResult) throws JSONException {

        ArrayList<Integer> listID = new ArrayList<>();

        JSONObject jsonObject = new JSONObject(JSONResult);

        JSONObject objProjects = jsonObject.getJSONObject("projects");
        JSONArray listProjects = objProjects.getJSONArray("projects");

        JSONObject project;
        int id;

        for (int i = 0; i < listProjects.length(); i++) {
            project = listProjects.getJSONObject(i);
            id = project.getInt("id");
            listID.add(id);
        }

        return listID;
    }

    public Project getProject(String idProject) throws IOException, JSONException, RequestException {
        String JSONReslt = Service.getRequest(endpointProject.replace("id_project", idProject));
        JSONObject jsonObject = new JSONObject(JSONReslt);
        JSONObject objProject = jsonObject.getJSONObject("project");

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        Project project = mapper.readValue(objProject.toString(), Project.class);

        return project;
    }

}
