package uber.com.myapplication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import uber.com.myapplication.adapters.ImageRecyclerViewAdapter;
import uber.com.myapplication.listeners.InfiniteScrollListener;
import uber.com.myapplication.listeners.SearchListener;
import uber.com.myapplication.models.PhotoModel;
import uber.com.myapplication.models.Response;
import uber.com.myapplication.tasks.SearchTask;

public class ImageSearchActivity extends AppCompatActivity {
    private String TAG = ImageSearchActivity.class.getName();
    private ImageRecyclerViewAdapter recyclerViewAdapter;
    private InfiniteScrollListener scrollListener;
    private String searchText;
    private TextView searchHeader;
    private SearchTask searchTask ;
    private int totalPageForCurrentSearch;
    private SearchListener listener = new SearchListener() {
        @Override
        public void searchStarted() {

        }

        @Override
        public void SearchCompleted(final Response response, String searchtext, int pageNumber) {
            if(response!=null && response.isError()){
                searchHeader.setText(response.getEx().getMessage());
                return;
            }
            if (response != null && response.getPhotoResponse() != null) {
                List<PhotoModel> photos = response.getPhotoResponse().getPhotos();
                Log.i(TAG,"Result count = "+photos.size());
                totalPageForCurrentSearch = response.getPhotoResponse().getPages();
                searchHeader.setText(getString(R.string.search_header)+" "+searchtext);

                if (pageNumber == 1) {
                    if(photos == null || photos.size()==0){
                        searchHeader.setText(getString(R.string.no_result));
                        recyclerViewAdapter.setItems(new ArrayList<PhotoModel>());
                        return;
                    }
                    recyclerViewAdapter.setItems(photos);
                } else {
                    recyclerViewAdapter.addtems(response.getPhotoResponse().getPhotos());
                }
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_search);
        RecyclerView recyclerView = (findViewById(R.id.imageGrid));
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerViewAdapter = new ImageRecyclerViewAdapter(new ArrayList<PhotoModel>());
        recyclerView.setAdapter(recyclerViewAdapter);
        Button searchButton = findViewById(R.id.searchBtn);
        final EditText editText = findViewById(R.id.searchEditText);
        searchHeader = findViewById(R.id.serachHeadr);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchText = editText.getText().toString();
                if(searchText.trim().length()==0){
                    searchHeader.setText(getString(R.string.empty_validation_text));
                    return;
                }
                recyclerViewAdapter.setItems(new ArrayList<PhotoModel>());
                searchHeader.setText(getString(R.string.searching_text)+" "+searchText+"...");
                if(searchTask!=null && !(searchTask.getStatus()== AsyncTask.Status.FINISHED)){
                    searchTask.cancel(true);
                }

                searchTask = new SearchTask(listener);
                searchTask.execute(searchText);

            }
        });
        scrollListener = new InfiniteScrollListener(3) {
            @Override
            protected void loadNextPage(int page) {
                if(page < totalPageForCurrentSearch) {
                    searchHeader.setText(getString(R.string.loading_text)+" "+searchText+"...");
                    searchTask = new SearchTask(listener);
                    searchTask.execute(searchText, "" + page);
                }
            }
        };
        recyclerView.addOnScrollListener(scrollListener);


    }


}
