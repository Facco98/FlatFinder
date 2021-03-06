package it.unitn.disi.lpsmt.flatfinder.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import it.unitn.disi.lpsmt.flatfinder.R;
import it.unitn.disi.lpsmt.flatfinder.activity.AnnounceDetailsActivity;
import it.unitn.disi.lpsmt.flatfinder.activity.ModifyAnnounceActivity;
import it.unitn.disi.lpsmt.flatfinder.fragment.EliminaAnnuncioDialogFragment;
import it.unitn.disi.lpsmt.flatfinder.listener.DeleteFragmentListener;
import it.unitn.disi.lpsmt.flatfinder.model.announce.Announce;
import it.unitn.disi.lpsmt.flatfinder.model.announce.Photo;

import java.util.ArrayList;
import java.util.List;

public class MyAnnounceListAdapter extends RecyclerView.Adapter<MyAnnounceListAdapter.MyAnnounceListViewHolder>
implements DeleteFragmentListener {

    private static final String TAG = "MyAnnounceListAdapter";

    private List<Announce> announceList;
    private Context context;
    MyAnnounceListViewHolder viewHolder;

    public MyAnnounceListAdapter(List<Announce> announceList, Context context) {
        this.announceList = new ArrayList<>(announceList);
        this.context = context;
    }

    @NonNull
    @Override
    public MyAnnounceListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.i_miei_annunci_card_item, parent, false);
        /*MyAnnounceListViewHolder*/ viewHolder = new MyAnnounceListViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyAnnounceListViewHolder holder, int position) {
        Announce announce = this.announceList.get(position);
        System.out.println("onBind View Holder: " + this.announceList.get(position));

        holder.txtPrezzo.setText(announce.getRentPerMonth()+" €");
        holder.txtDimensione.setText(announce.getSize()+" mq");
        holder.txtNLocali.setText(announce.getnLocals()+" locali");
        holder.txtCategoria.setText(announce.getCategory().description);
        holder.txtIndirizzo.setText(announce.getAddress());

        holder.itemView.setOnClickListener((v)->{
            System.out.println("adapter position: " + viewHolder.getAdapterPosition());
            System.out.println("layout position: " + viewHolder.getLayoutPosition());

            Intent intent = new Intent(context, AnnounceDetailsActivity.class);
            intent.putExtra("announceID", announce.getId());
            context.startActivity(intent);
        });
        holder.btnModifica.setOnClickListener((v) -> {

            Intent intent = new Intent(context, ModifyAnnounceActivity.class);
            intent.putExtra("announceID", announce.getId());
            context.startActivity(intent);

        });
        holder.btnElimina.setOnClickListener((v) -> {

            Log.d(TAG, "Button Elimina did click");
            AppCompatActivity activity = (AppCompatActivity) this.context;
            EliminaAnnuncioDialogFragment eliminaAnnuncioDialogFragment = new EliminaAnnuncioDialogFragment(announce, context, this);
            eliminaAnnuncioDialogFragment.show(activity.getSupportFragmentManager(), TAG);

        });

    }

    @Override
    public int getItemCount() {
        return announceList.size();
    }

    @Override
    public void announceDeleted(Announce announce) {
        this.announceList.remove(announce);
        this.notifyDataSetChanged();
    }

    public static class MyAnnounceListViewHolder extends RecyclerView.ViewHolder{

        private TextView txtPrezzo, txtDimensione, txtNLocali, txtCategoria, txtIndirizzo;
        private Button btnElimina, btnModifica;

        public MyAnnounceListViewHolder(@NonNull View itemView) {
            super(itemView);

            this.setupUI(itemView);
        }

        private void setupUI(View view) {
            this.txtPrezzo = view.findViewById(R.id.miei_annunci_card_lbl_prezzo);
            this.txtDimensione = view.findViewById(R.id.miei_annunci_card_lbl_dimensione);
            this.txtIndirizzo = view.findViewById(R.id.miei_annunci_card_lbl_indirizzo);
            this.txtNLocali = view.findViewById(R.id.miei_annunci_card_lbl_nlocali);
            this.txtCategoria = view.findViewById(R.id.miei_annunci_card_lbl_categoria);
            this.btnElimina = view.findViewById(R.id.miei_annunci_card_btn_elimina);
            this.btnModifica = view.findViewById(R.id.miei_annunci_card_btn_modifica);
        }
    }
}
