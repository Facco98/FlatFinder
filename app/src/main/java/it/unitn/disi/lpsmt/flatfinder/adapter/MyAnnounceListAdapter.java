package it.unitn.disi.lpsmt.flatfinder.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import it.unitn.disi.lpsmt.flatfinder.R;
import it.unitn.disi.lpsmt.flatfinder.activity.AnnounceDetailsActivity;
import it.unitn.disi.lpsmt.flatfinder.model.announce.Announce;
import it.unitn.disi.lpsmt.flatfinder.model.announce.Photo;

import java.util.List;

public class MyAnnounceListAdapter extends RecyclerView.Adapter<MyAnnounceListAdapter.MyAnnounceListViewHolder> {

    private static final String TAG = "MyAnnounceListAdapter";

    private List<Announce> announceList;
    private Context context;
    private Announce announce;

    public MyAnnounceListAdapter(List<Announce> announceList, Context context) {
        this.announceList = announceList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyAnnounceListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.annuncio_card_item, parent, false);
        MyAnnounceListViewHolder viewHolder = new MyAnnounceListViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyAnnounceListViewHolder holder, int position) {
        this.announce = this.announceList.get(position);

        holder.itemView.setOnClickListener(this::cardOnClick);

        List<Photo> photoList = announce.getPhotos();
        holder.viewPager.setAdapter(new PhotosAdapter(photoList, context));

        holder.txtPrezzo.setText(announce.getRentPerMonth()+"");
        holder.txtDimensione.setText(announce.getSize()+"");
        holder.txtNLocali.setText(announce.getnLocals()+"");
        holder.txtCategoria.setText(announce.getCategory().description);
        holder.txtIndirizzo.setText(announce.getAddress());
        // TODO: holder.btnAddToFavorite.setChecked();
        holder.btnAddToFavorite.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // aggiorna il valore del db con quello di isChecked
        });

    }

    /**
     * start AnnounceDetailsActivity on view click
     * @param view
     */
    private void cardOnClick(View view){
        Intent intent = new Intent(context, AnnounceDetailsActivity.class);
        intent.putExtra("announceID", announce.getId());
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return announceList.size();
    }

    public static class MyAnnounceListViewHolder extends RecyclerView.ViewHolder{

        private ViewPager viewPager;
        private TextView txtPrezzo, txtDimensione, txtNLocali, txtCategoria, txtIndirizzo;
        private ToggleButton btnAddToFavorite;

        public MyAnnounceListViewHolder(@NonNull View itemView) {
            super(itemView);

            this.setupUI(itemView);
        }

        private void setupUI(View view) {
            this.viewPager = view.findViewById(R.id.annuncio_card_viewpager_imageviewpager);
            this.txtPrezzo = view.findViewById(R.id.annuncio_card_lbl_prezzo);
            this.txtDimensione = view.findViewById(R.id.annuncio_card_lbl_dimensione);
            this.txtIndirizzo = view.findViewById(R.id.annuncio_card_lbl_indirizzo);
            this.txtNLocali = view.findViewById(R.id.annuncio_card_lbl_nlocali);
            this.txtCategoria = view.findViewById(R.id.annuncio_card_lbl_categoria);
            this.btnAddToFavorite = view.findViewById(R.id.annuncio_card_tglbtn_addToFavorite);
        }


    }
}
