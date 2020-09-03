package it.unitn.disi.lpsmt.flatfinder.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import it.unitn.disi.lpsmt.flatfinder.model.announce.Photo;

import java.util.ArrayList;
import java.util.List;

public class PhotosAdapter extends PagerAdapter {

    private List<Photo> photos;
    private Context context;

    public PhotosAdapter(@Nullable List<Photo> photos, @NonNull Context context) {


        if( photos == null )
            this.photos = new ArrayList<>();
        else
            this.photos = new ArrayList<>(photos);
        this.context = context;

    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        Photo photo = this.photos.get(position);
        ImageView imgView = new ImageView(this.context);
        ((Activity) this.context).registerForContextMenu(imgView);
        imgView.setOnLongClickListener(v -> {

                imgView.showContextMenu();
                Log.e("PhotosAdapter", "OnLongClick");
            return true;

        });
        imgView.setImageBitmap(photo.getBitmap());
        container.addView(imgView);
        return imgView;

    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    @Override
    public int getCount() {
        return this.photos.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.setPrimaryItem(container, position, object);
    }

    public void addItem(Photo photo){

        this.photos.add(photo);

    }

    public List<Photo> getItems(){

        return this.photos;

    }

    public void removeItem(int position){

        this.photos.remove(position);

    }
}
