package uber.com.myapplication.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.nfc.Tag;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.net.URL;
import java.util.List;

import uber.com.myapplication.R;
import uber.com.myapplication.models.PhotoModel;
import uber.com.myapplication.rest.PhotoLoader;

public class ImageRecyclerViewAdapter extends RecyclerView.Adapter<ImageRecyclerViewAdapter.ImageViewHolder> {
    private String TAG = ImageRecyclerViewAdapter.class.getName();

    private List<PhotoModel> items;
    private PhotoLoader photoLoader;
    public ImageRecyclerViewAdapter(List<PhotoModel> models){
      this.items = models;
      photoLoader = new PhotoLoader();
    }

    @Override
    public ImageRecyclerViewAdapter.ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.image_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(ImageViewHolder viewHolder, int position) {
        PhotoModel photoModel = items.get(position);
        photoLoader.displayImage(viewHolder.imageView,String.format("http://farm%d.static.flickr.com/%s/%s_%s.jpg", photoModel.getFarm(), photoModel.getServer(), photoModel.getId(), photoModel.getSecret()));
    }

    public void addtems(List<PhotoModel> items) {
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void setItems(List<PhotoModel> items){
        this.items= items;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public  class ImageViewHolder extends RecyclerView.ViewHolder{
      public ImageView imageView;

      public ImageViewHolder(View view){
          super(view);
          imageView = view.findViewById(R.id.photo);
      }
    }
}
