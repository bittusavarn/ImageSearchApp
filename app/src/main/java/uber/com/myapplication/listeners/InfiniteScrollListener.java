package uber.com.myapplication.listeners;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

/**
 * This class will listen for scrollListener and will work
 * for GridLayoutManager only
 */
public abstract class InfiniteScrollListener extends RecyclerView.OnScrollListener {
    private int threshold ;
    private int currentPage=1;
    private int lastCount ;
    private boolean loading = true;

    public boolean isLoading() {
        return loading;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }

    private String TAG = InfiniteScrollListener.class.getName();
    public InfiniteScrollListener(int threshold){
        this.threshold = threshold;
    }
    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
       int lastVisibleItemPosition = 0;
       int totalCount = recyclerView.getLayoutManager().getItemCount();
       if(recyclerView.getLayoutManager() instanceof GridLayoutManager) {
          lastVisibleItemPosition = ((GridLayoutManager)recyclerView.getLayoutManager()).findLastVisibleItemPosition();
       }

        if(totalCount < lastCount){
           currentPage = 1;
           lastCount = totalCount;
            if (totalCount == 0) {
                this.loading = true;
            }
        }
        if (loading && (totalCount > lastCount)) {
            loading = false;
            lastCount = totalCount;
        }
        if (!loading && lastVisibleItemPosition + threshold > totalCount) {
            currentPage++;
            loadNextPage(currentPage);
            loading = true;
        }
        Log.d(TAG,"onScrolled lastVisibleItemCount"+lastVisibleItemPosition+" totalItemCount "+totalCount+",lastCount"+lastCount+","+loading);
    }

    protected abstract void loadNextPage(int page);
}
