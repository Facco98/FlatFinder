package it.unitn.disi.lpsmt.flatfinder.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import it.unitn.disi.lpsmt.flatfinder.R;
import it.unitn.disi.lpsmt.flatfinder.activity.AnnounceDetailsActivity;
import it.unitn.disi.lpsmt.flatfinder.activity.SearchResultActivity;
import it.unitn.disi.lpsmt.flatfinder.model.Search;

import java.util.List;

public class SavedZoneListAdapter extends RecyclerView.Adapter<SavedZoneListAdapter.FavoriteAreaListViewHolder>{

    private static final String TAG = "FavoriteAreaListAdapter";

    private List<Search> list;
    private Context context;

    public SavedZoneListAdapter(List<Search> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public SavedZoneListAdapter.FavoriteAreaListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ricerche_salvate_card_item, parent, false);
        SavedZoneListAdapter.FavoriteAreaListViewHolder viewHolder = new SavedZoneListAdapter.FavoriteAreaListViewHolder(view);

        viewHolder.itemView.setOnClickListener((v)->{
            // todo get coordinates of the address
            String latitudeCenter = "", longitudeCenter = "";
            Intent intent = new Intent(context, SearchResultActivity.class);
            intent.putExtra("latitudineCentro", latitudeCenter);
            intent.putExtra("longitudineCentro", longitudeCenter);
            context.startActivity(intent);
        });

        viewHolder.btnElimina.setOnClickListener(this::btnEliminaOnClick);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SavedZoneListAdapter.FavoriteAreaListViewHolder holder, int position) {

        // todo get zona raggio and centro
        //holder.lblRaggio.setText();
        //holder.lblIndirizzo.setText();

    }

    private void btnEliminaOnClick(View view) {
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class FavoriteAreaListViewHolder extends RecyclerView.ViewHolder{

        private TextView lblIndirizzo, lblRaggio;
        private Button btnElimina;

        public FavoriteAreaListViewHolder(@NonNull View itemView) {
            super(itemView);

            this.setupUI(itemView);
        }

        private void setupUI(View view) {
            this.lblIndirizzo = view.findViewById(R.id.ricerche_salvate_card_lbl_indirizzo);
            this.lblRaggio = view.findViewById(R.id.ricerche_salvate_card_lbl_raggio);
            this.btnElimina = view.findViewById(R.id.ricerche_salvate_card_btn_elimina);
        }
    }
}
