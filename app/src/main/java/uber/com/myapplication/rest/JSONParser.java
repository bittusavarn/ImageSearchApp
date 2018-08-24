package uber.com.myapplication.rest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import uber.com.myapplication.models.PhotoModel;
import uber.com.myapplication.models.PhotoResponse;

public class JSONParser {

    private String TAG = JSONParser.class.getName();
    public PhotoResponse parsePhotoResponse(String jsonResponse) throws Exception{
        try {
            PhotoResponse response = new PhotoResponse();
            JSONObject jsonObject =  new JSONObject(jsonResponse);
            JSONObject photoObject = (JSONObject) jsonObject.get("photos");
            response.setPage(photoObject.getInt("page"));
            response.setPages(photoObject.getInt("pages"));
            response.setPerpage(photoObject.getInt("perpage"));
            response.setPerpage(photoObject.getInt("total"));
            List<PhotoModel> photoModels = new ArrayList<>();
            JSONArray photoArray = (JSONArray)photoObject.get("photo");
            for(int i=0;i<photoArray.length();i++){
                JSONObject photoModelJson = (JSONObject) photoArray.get(i);
                PhotoModel photoModel =  new PhotoModel();
                photoModel.setTitle(photoModelJson.getString("title"));
                photoModel.setFarm(photoModelJson.getInt("farm"));
                photoModel.setServer(photoModelJson.getString("server"));
                photoModel.setSecret(photoModelJson.getString("secret"));
                photoModel.setOwner(photoModelJson.getString("owner"));
                photoModel.setId(photoModelJson.getString("id"));
                photoModels.add(photoModel);
            }
            response.setPhotos(photoModels);
            return response;
        }
        catch (JSONException ex){
            System.out.println(TAG + " Exception in parsePhotoResponse " + ex.getMessage());
            throw new Exception("Invalid Response");
        }

    }
}
