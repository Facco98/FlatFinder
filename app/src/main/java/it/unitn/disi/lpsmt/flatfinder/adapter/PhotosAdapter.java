package it.unitn.disi.lpsmt.flatfinder.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import it.unitn.disi.lpsmt.flatfinder.model.announce.Photo;

import java.util.ArrayList;
import java.util.List;

public class PhotosAdapter extends PagerAdapter {

    private List<Photo> photos;
    private Context context;

    public PhotosAdapter(@Nullable List<Photo> photos, @NonNull Context context) {

        if( this.photos == null )
            this.photos = new ArrayList<>();
        this.context = context;

    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        Photo photo = this.photos.get(position);
        ImageView imgView = new ImageView(this.context);
        imgView.setImageBitmap(photo.getBitmap());
        container.addView(imgView);
        return imgView;

    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeViewAt(position);
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
}
