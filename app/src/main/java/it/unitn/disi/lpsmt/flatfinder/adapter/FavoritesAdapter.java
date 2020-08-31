package it.unitn.disi.lpsmt.flatfinder.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import it.unitn.disi.lpsmt.flatfinder.R;
import it.unitn.disi.lpsmt.flatfinder.activity.AnnounceDetailsActivity;
import it.unitn.disi.lpsmt.flatfinder.model.announce.Announce;
import it.unitn.disi.lpsmt.flatfinder.remote.RemoteAPI;
import it.unitn.disi.lpsmt.flatfinder.util.Util;

import java.util.*;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder>{
    private static final String TAG = "FavoritesAdapter";
    private static final String FAVORITES_ARRAY = "favorite_announces";

    private final List<String> announceList;
    private final Context context;

    public FavoritesAdapter(List<String> announceList, Context context) {
        this.announceList = new ArrayList<>(announceList);
        this.context = context;
    }

    @NonNull
    @Override
    public FavoritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.annuncio_card_item, parent, false);
        return new FavoritesAdapter.FavoritesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesAdapter.FavoritesViewHolder holder, int position) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("annunci_preferiti", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> favorites = sharedPreferences.getStringSet(FAVORITES_ARRAY, new HashSet<>());

        AlertDialog alertDialog = Util.getDialog(context, TAG);
        Util.showDialog(alertDialog, TAG);

        Map<String, String> filters = new HashMap<>();
        filters.put("id", announceList.get(position));
        RemoteAPI.getAnnounceList(filters, (announces, exception) ->{
            if( exception != null ){

                Toast.makeText(context, "Errore durante il caricamento dei dati", Toast.LENGTH_SHORT)
                        .show();
                exception.printStackTrace();

            } else if( announces != null && announces.size() <= 0 ){
                // non viene ritornato nessun annuncio, rimuovo l'annuncio dai preferiti
                favorites.remove(announceList.get(position));
                editor.clear();
                editor.putStringSet(FAVORITES_ARRAY, favorites);
                editor.commit();

                removeFromList(position);
            } else if ( announces != null && announces.size() > 0 ){
                // annuncio ancora presente
                Announce announce = announces.get(0);
                Log.i(TAG, "id=" + announces.get(0).getId() + ", " +announces.toString());
                Log.i(TAG, "Data retrivered");

                if(announce != null){
                    Log.d(TAG, "onbind announceid: " + announce.getId());
                    holder.txtPrezzo.setText(announce.getRentPerMonth()+" €");
                    holder.txtDimensione.setText(announce.getSize()+" mq");
                    holder.txtNLocali.setText(announce.getnLocals()+" locali");
                    holder.txtCategoria.setText(announce.getCategory().description);
                    holder.txtIndirizzo.setText(announce.getAddress());


                    String announceId = announce.getId() + "";
                    Log.d(TAG, "announceId: " + announceId);

                    if(favorites.contains(announceId)){
                        holder.btnAddToFavorite.setChecked(true);
                    } else {
                        removeFromList(position);
                    }


                    holder.itemView.setOnClickListener((v) -> {
                        Intent intent = new Intent(context, AnnounceDetailsActivity.class);
                        intent.putExtra("announceID", announce.getId());
                        context.startActivity(intent);
                    });

                    holder.btnAddToFavorite.setOnCheckedChangeListener((buttonView, isChecked) -> {

                        if (!isChecked) {
                            Log.d(TAG, "is not Checked");
                            favorites.remove(announceId);
                            editor.clear();
                            editor.putStringSet(FAVORITES_ARRAY, favorites);
                            editor.commit();

                            removeFromList(position);
                        }

                    });
                }
            }
        });

        Util.dismissDialog(alertDialog, TAG);
    }

    private void removeFromList(int position){
        announceList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, announceList.size());
    }

    @Override
    public int getItemCount() {
        return this.announceList.size();
    }

    public static class FavoritesViewHolder extends RecyclerView.ViewHolder {

        private TextView txtPrezzo, txtDimensione, txtNLocali, txtCategoria, txtIndirizzo;
        private ToggleButton btnAddToFavorite;

        public FavoritesViewHolder(@NonNull View itemView) {
            super(itemView);
            this.setupUI(itemView);
        }

        private void setupUI(View view) {
            this.txtPrezzo = view.findViewById(R.id.annuncio_card_lbl_prezzo);
            this.txtDimensione = view.findViewById(R.id.annuncio_card_lbl_dimensione);
            this.txtIndirizzo = view.findViewById(R.id.annuncio_card_lbl_indirizzo);
            this.txtNLocali = view.findViewById(R.id.annuncio_card_lbl_nlocali);
            this.txtCategoria = view.findViewById(R.id.annuncio_card_lbl_categoria);
            this.btnAddToFavorite = view.findViewById(R.id.annuncio_card_tglbtn_addToFavorite);

        }
    }
}
