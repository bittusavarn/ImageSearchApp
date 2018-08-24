package uber.com.myapplication.listeners;

import java.util.List;

import uber.com.myapplication.models.PhotoModel;
import uber.com.myapplication.models.Response;

public interface SearchListener {
    public void searchStarted();
    public void SearchCompleted(Response response,String searchtext,int pageNumber);
}
