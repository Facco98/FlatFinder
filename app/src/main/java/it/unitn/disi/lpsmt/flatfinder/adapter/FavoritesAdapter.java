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

    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;
    private Set<String> favorites;

    private final List<String> announceList;
    private final Context context;

    public FavoritesAdapter(List<String> announceList, Context context) {
        this.announceList = new ArrayList<>(announceList);
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences("annunci_preferiti", Context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();
    }

    @NonNull
    @Override
    public FavoritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.annuncio_card_item, parent, false);
        return new FavoritesAdapter.FavoritesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesAdapter.FavoritesViewHolder holder, int position) {
        this.favorites = sharedPreferences.getStringSet(FAVORITES_ARRAY, new HashSet<>());

        AlertDialog alertDialog = Util.getDialog(context, "Caricamento dell'annuncio in corso",TAG);
        Util.showDialog(alertDialog, TAG);

        holder.txtCategoria.setText("");
        holder.txtPrezzo.setText("");
        holder.txtIndirizzo.setText("");
        holder.txtNLocali.setText("");
        holder.txtDimensione.setText("");

        Map<String, String> filters = new HashMap<>();
        String announceId = announceList.get(position);
        filters.put("id", announceId);
        Log.d(TAG, "announceId: " + announceId);

        RemoteAPI.getAnnounceList(filters, (announces, exception) ->{
            if( exception != null ){

                Toast.makeText(context, "Errore durante il caricamento dei dati", Toast.LENGTH_SHORT)
                        .show();
                exception.printStackTrace();

            } else if( announces != null && announces.size() <= 0 ){
                // non viene ritornato nessun annuncio, apro un dialog
                showAlert(announceId, position);
            } else if ( announces != null && announces.size() > 0 ){
                // annuncio ancora presente
                Announce announce = announces.get(0);

                if(announce != null  && announce.isActive()){
                    Log.d(TAG, "onbind announceid: " + announce.getId());
                    holder.txtPrezzo.setText(announce.getRentPerMonth()+" €");
                    holder.txtDimensione.setText(announce.getSize()+" mq");
                    holder.txtNLocali.setText(announce.getnLocals()+" locali");
                    holder.txtCategoria.setText(announce.getCategory().description);
                    holder.txtIndirizzo.setText(announce.getAddress());

                    if(favorites.contains(announceId)){
                        holder.btnAddToFavorite.setChecked(true);
                    } else {
                        removeFromList(announceId, position);
                    }

                    holder.itemView.setOnClickListener((v) -> {
                        Intent intent = new Intent(context, AnnounceDetailsActivity.class);
                        intent.putExtra("announceID", announce.getId());
                        context.startActivity(intent);
                    });

                    holder.btnAddToFavorite.setOnCheckedChangeListener((buttonView, isChecked) -> {

                        if (!isChecked) {
                            Log.d(TAG, "is not Checked");

                            removeFromList(announceId, position);
                            Toast.makeText(context, "Lista aggiornata con successo", Toast.LENGTH_SHORT).show();
                        }

                    });
                } else {
                    showAlert(announceId, position);
                }
            }
        });

        Util.dismissDialog(alertDialog, TAG);
    }

    private void showAlert(String announceId, int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false)
                .setTitle("Aggiornamento lista dei preferiti")
                .setMessage("L'annuncio numero " +announceId+ " è stato eliminato o ritirato, cliccare sul bottone per aggiornare gli annunci")
                .setPositiveButton("Aggiorna la lista", (dialog, which) -> {
                    removeFromList(announceId, position);
                    dialog.dismiss();
                    Toast.makeText(context, "Lista aggiornata con successo", Toast.LENGTH_SHORT).show();
                }).create().show();
    }

    private void removeFromList(String announceId, int position){
        favorites.remove(announceId);
        editor.clear();
        editor.putStringSet(FAVORITES_ARRAY, favorites);
        editor.commit();
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
