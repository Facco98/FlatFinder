package it.unitn.disi.lpsmt.flatfinder.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import it.unitn.disi.lpsmt.flatfinder.R;
import it.unitn.disi.lpsmt.flatfinder.activity.AnnounceDetailsActivity;
import it.unitn.disi.lpsmt.flatfinder.model.announce.Announce;
import it.unitn.disi.lpsmt.flatfinder.model.announce.Photo;

import java.util.List;

public class AnnounceListAdapter extends RecyclerView.Adapter<AnnounceListAdapter.AnnounceListViewHolder>{

    private static final String TAG = "AnnounceListAdapter";

    private List<Announce> announceList;
    private Context context;
    private Announce announce;

    public AnnounceListAdapter(List<Announce> announceList, Context context) {
        this.announceList = announceList;
        this.context = context;
    }

    @NonNull
    @Override
    public AnnounceListAdapter.AnnounceListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.annuncio_card_item, parent, false);
        AnnounceListAdapter.AnnounceListViewHolder viewHolder = new AnnounceListAdapter.AnnounceListViewHolder(view);

        viewHolder.itemView.setOnClickListener((v)->{
            this.announce = this.announceList.get(viewHolder.getAdapterPosition());
            Intent intent = new Intent(context, AnnounceDetailsActivity.class);
            intent.putExtra("announceID", announce.getId());
            context.startActivity(intent);
        });

        // TODO: holder.btnAddToFavorite.setChecked();
        viewHolder.btnAddToFavorite.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // aggiorna il valore del db con quello di isChecked
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AnnounceListAdapter.AnnounceListViewHolder holder, int position) {
        this.announce = this.announceList.get(position);

        holder.txtPrezzo.setText(announce.getRentPerMonth()+"");
        holder.txtDimensione.setText(announce.getSize()+"");
        holder.txtNLocali.setText(announce.getnLocals()+"");
        holder.txtCategoria.setText(announce.getCategory().description);
        holder.txtIndirizzo.setText(announce.getAddress());

    }

    @Override
    public int getItemCount() {
        return announceList.size();
    }

    public static class AnnounceListViewHolder extends RecyclerView.ViewHolder{

        private TextView txtPrezzo, txtDimensione, txtNLocali, txtCategoria, txtIndirizzo;
        private ToggleButton btnAddToFavorite;

        public AnnounceListViewHolder(@NonNull View itemView) {
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
