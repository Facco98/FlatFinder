package it.unitn.disi.lpsmt.flatfinder.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import it.unitn.disi.lpsmt.flatfinder.model.announce.Photo;

import java.util.List;

public class PhotosAdapter extends PagerAdapter {

    private List<Photo> photos;
    private Context context;

    public PhotosAdapter(@NonNull List<Photo> photos, @NonNull Context context) {

        this.photos = photos;
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
}
