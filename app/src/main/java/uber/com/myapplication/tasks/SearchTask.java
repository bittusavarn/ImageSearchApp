package uber.com.myapplication.tasks;

import android.os.AsyncTask;
import android.util.Log;

import uber.com.myapplication.listeners.SearchListener;
import uber.com.myapplication.models.Response;
import uber.com.myapplication.rest.JSONParser;
import uber.com.myapplication.rest.RequestProcessor;

public class SearchTask extends AsyncTask<String,Object,Response> {
   private String TAG = SearchTask.class.getName();
   private  SearchListener searchListener;
   private String searchText;
   private int pageNumber = 1;
    public  SearchTask(SearchListener listener){
        searchListener = listener;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (searchListener != null)
            searchListener.searchStarted();
    }

    @Override
    protected Response doInBackground(String... params) {
        Response response = new Response();
        RequestProcessor requestProcessor = new RequestProcessor();
        try {
            searchText = params[0];
            String url = "https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=3e7cc266ae2b0e0d78e279ce8e361736&format=json&nojsoncallback=1&safe_search=1&text=" + params[0];
            if (params.length == 2) {
                pageNumber = Integer.parseInt(params[1]);
                url = url + "&page=" + params[1];
            }

            Log.i(TAG,"Searching for URl "+url);

            String responseText = requestProcessor.getResponse(url);
            response.setPhotoResponse(new JSONParser().parsePhotoResponse(responseText));
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
            response.setEx(ex);
            response.setError(true);
        }
        return response;
    }

    @Override
    protected void onPostExecute(Response response) {
        super.onPostExecute(response);
        if (searchListener != null)
            searchListener.SearchCompleted(response,searchText,pageNumber);
    }
}
