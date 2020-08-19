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
import it.unitn.disi.lpsmt.flatfinder.activity.SearchResultActivity;
import it.unitn.disi.lpsmt.flatfinder.model.Search;

import java.util.List;

public class SavedSearchListAdapter extends RecyclerView.Adapter<SavedSearchListAdapter.FavoriteAreaListViewHolder>{

    private static final String TAG = "FavoriteAreaListAdapter";

    private List<Search> list;
    private Context context;
    private Search search;

    public SavedSearchListAdapter(List<Search> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public SavedSearchListAdapter.FavoriteAreaListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ricerche_salvate_card_item, parent, false);
        SavedSearchListAdapter.FavoriteAreaListViewHolder viewHolder = new SavedSearchListAdapter.FavoriteAreaListViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SavedSearchListAdapter.FavoriteAreaListViewHolder holder, int position) {
        this.search = this.list.get(position);

        holder.itemView.setOnClickListener(this::cardOnClick);

        // todo get search name and category
        //holder.txtNomeRicerca.setText();
        //holder.txtCategoriaRicerca.setText();

        holder.btnModifica.setOnClickListener(this::btnModificaOnClick);
        holder.btnElimina.setOnClickListener(this::btnEliminaOnClick);

    }

    private void btnEliminaOnClick(View view) {
    }

    private void btnModificaOnClick(View view) {
    }

    /**
     * start search result activity on view click
     * @param view
     */
    private void cardOnClick(View view){
        Intent intent = new Intent(context, SearchResultActivity.class);
        // passare info sulla ricerca
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class FavoriteAreaListViewHolder extends RecyclerView.ViewHolder{

        private TextView txtNomeRicerca, txtCategoriaRicerca;
        private Button btnElimina, btnModifica;

        public FavoriteAreaListViewHolder(@NonNull View itemView) {
            super(itemView);

            this.setupUI(itemView);
        }

        private void setupUI(View view) {
            this.txtNomeRicerca = view.findViewById(R.id.ricerche_salvate_card_lbl_nomeRicerca);
            this.txtCategoriaRicerca = view.findViewById(R.id.ricerche_salvate_card_lbl_categoria);
            this.btnElimina = view.findViewById(R.id.ricerche_salvate_card_btn_elimina);
            this.btnModifica = view.findViewById(R.id.ricerche_salvate_card_btn_modifica);
        }
    }
}
