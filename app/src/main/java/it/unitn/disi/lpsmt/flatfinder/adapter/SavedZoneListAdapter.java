package it.unitn.disi.lpsmt.flatfinder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import it.unitn.disi.lpsmt.flatfinder.R;
import it.unitn.disi.lpsmt.flatfinder.model.Zone;
import it.unitn.disi.lpsmt.flatfinder.remote.RemoteAPI;

import java.util.ArrayList;
import java.util.List;

public class SavedZoneListAdapter extends RecyclerView.Adapter<SavedZoneListAdapter.FavoriteAreaListViewHolder>{

    private static final String TAG = "FavoriteAreaListAdapter";

    private List<Zone> list;
    private Context context;

    public SavedZoneListAdapter(List<Zone> list, Context context) {
        this.list = new ArrayList<>(list);
        this.context = context;
    }

    @NonNull
    @Override
    public SavedZoneListAdapter.FavoriteAreaListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ricerche_salvate_card_item, parent, false);
        SavedZoneListAdapter.FavoriteAreaListViewHolder viewHolder = new SavedZoneListAdapter.FavoriteAreaListViewHolder(view);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteAreaListViewHolder holder, int position) {
        holder.populateUI(this.list.get(position));
        holder.btnElimina.setOnClickListener((view) -> {

            RemoteAPI.deleteZone(this.list.get(position), (string, ex) -> {

                if( ex != null )
                    ex.printStackTrace();
                else {
                    this.list.remove(position);
                    this.notifyDataSetChanged();
                }

            });

        });
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

        public void populateUI(Zone zone){

            this.lblIndirizzo.setText(zone.getAddress());
            this.lblRaggio.setText(""+zone.getMaxDistance() + " km");


        }
    }
}
