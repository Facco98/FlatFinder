package it.unitn.disi.lpsmt.flatfinder.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import it.unitn.disi.lpsmt.flatfinder.R;
import it.unitn.disi.lpsmt.flatfinder.activity.AnnounceDetailsActivity;
import it.unitn.disi.lpsmt.flatfinder.model.announce.Announce;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AnnounceListAdapter extends RecyclerView.Adapter<AnnounceListAdapter.AnnounceListViewHolder>{

    private static final String TAG = "AnnounceListAdapter";

    private static final String FAVORITES_ARRAY = "favorite_announces";

    private List<Announce> announceList;
    private Context context;

    public AnnounceListAdapter(List<Announce> announceList, Context context) {
        this.announceList = new ArrayList<>(announceList);
        this.context = context;
    }

    @NonNull
    @Override
    public AnnounceListAdapter.AnnounceListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.annuncio_card_item, parent, false);
        return new AnnounceListAdapter.AnnounceListViewHolder(view, announceList, context);
    }

    @Override
    public void onBindViewHolder(@NonNull AnnounceListAdapter.AnnounceListViewHolder holder, int position) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("annunci_preferiti", Context.MODE_PRIVATE);
        Set<String> favorites = sharedPreferences.getStringSet(FAVORITES_ARRAY, new HashSet<>());
        Announce announce = this.announceList.get(position);
        String announceId = announce.getId()+"";

        holder.txtPrezzo.setText(announce.getRentPerMonth()+" €");
        holder.txtDimensione.setText(announce.getSize()+" mq");
        holder.txtNLocali.setText(announce.getnLocals()+" locali");
        holder.txtCategoria.setText(announce.getCategory().description);
        holder.txtIndirizzo.setText(announce.getAddress());

        Log.d(TAG, "favorites: " + favorites.toString());
        holder.btnAddToFavorite.setChecked(favorites.contains(announceId));
    }

    @Override
    public int getItemCount() {
        return announceList.size();
    }

    public static class AnnounceListViewHolder extends RecyclerView.ViewHolder{

        private Context context;
        private List<Announce> announceList;
        private TextView txtPrezzo, txtDimensione, txtNLocali, txtCategoria, txtIndirizzo;
        private ToggleButton btnAddToFavorite;

        public AnnounceListViewHolder(@NonNull View itemView, List<Announce> announceList, Context context) {
            super(itemView);
            this.context = context;
            this.announceList = announceList;
            this.setupUI(itemView);
        }

        private void setupUI(View view) {
            this.txtPrezzo = view.findViewById(R.id.annuncio_card_lbl_prezzo);
            this.txtDimensione = view.findViewById(R.id.annuncio_card_lbl_dimensione);
            this.txtIndirizzo = view.findViewById(R.id.annuncio_card_lbl_indirizzo);
            this.txtNLocali = view.findViewById(R.id.annuncio_card_lbl_nlocali);
            this.txtCategoria = view.findViewById(R.id.annuncio_card_lbl_categoria);
            this.btnAddToFavorite = view.findViewById(R.id.annuncio_card_tglbtn_addToFavorite);

            view.setOnClickListener((v)->{
                Announce announce = this.announceList.get(getAdapterPosition());
                Intent intent = new Intent(context, AnnounceDetailsActivity.class);
                intent.putExtra("announceID", announce.getId());
                context.startActivity(intent);
            });

            this.btnAddToFavorite.setOnCheckedChangeListener((buttonView, isChecked) -> {
                SharedPreferences sharedPreferences = context.getSharedPreferences("annunci_preferiti", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Announce announce = this.announceList.get(getAdapterPosition());
                String announceId = announce.getId() + "";
                Log.d(TAG, "announceId: "+announceId);

                Set<String> favorites = sharedPreferences.getStringSet(FAVORITES_ARRAY, new HashSet<>());

                if(isChecked){
                    Log.d(TAG, "isChecked");
                    favorites.add(announceId+"");

                } else {
                    Log.d(TAG, "is not Checked");
                    favorites.remove(announceId);
                }
                editor.clear();
                editor.putStringSet(FAVORITES_ARRAY, favorites);
                editor.commit();
                Log.d(TAG, "preferiti: " + favorites.toString());
                Log.d(TAG, "sharedpref: " + sharedPreferences.getStringSet(FAVORITES_ARRAY, null));

            });

        }


    }
}
