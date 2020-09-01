package it.unitn.disi.lpsmt.flatfinder.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Adapter;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import it.unitn.disi.lpsmt.flatfinder.R;
import it.unitn.disi.lpsmt.flatfinder.adapter.MyAnnounceListAdapter;
import it.unitn.disi.lpsmt.flatfinder.adapter.MyAnnouncesAdapter;
import it.unitn.disi.lpsmt.flatfinder.model.User;
import it.unitn.disi.lpsmt.flatfinder.model.announce.Announce;
import it.unitn.disi.lpsmt.flatfinder.remote.RemoteAPI;
import it.unitn.disi.lpsmt.flatfinder.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyAnnouncesListFragment extends Fragment {

    private static final String TAG = "MyAnnouncesListFragment";

    private boolean showingActiveAnnounces;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter<MyAnnounceListAdapter.MyAnnounceListViewHolder> adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Announce> announceList;
    private User user;

    private AlertDialog alertDialog;

    public MyAnnouncesListFragment( boolean showingActiveAnnounces) {
        // Required empty public constructor

        this.showingActiveAnnounces = showingActiveAnnounces;
    }

    public MyAnnouncesListFragment(){
        this.showingActiveAnnounces = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.user = User.getCurrentUser();
        if( user == null ){

            Log.i(TAG, "USER IS NOT LOGGED IN");
            this.requireActivity().finish();

        }
        View view = inflater.inflate(R.layout.i_miei_annunci_list, container, false);

        setupUI(view);

        this.setRecyclerViewAdapter(view);
        //this.updateList();
        return view;
    }

    private void setRecyclerViewAdapter(View view) {
        this.layoutManager = new LinearLayoutManager(view.getContext());
        this.recyclerView.setLayoutManager(this.layoutManager);

        if(announceList == null){
            announceList = new ArrayList<>();
        }

        this.adapter = new MyAnnounceListAdapter(this.announceList, view.getContext());
        this.recyclerView.setAdapter(this.adapter);
    }

    private void setupUI(View view) {

        this.recyclerView = view.findViewById(R.id.miei_annunci_view_annunciList);
        this.alertDialog = Util.getDialog(this.requireContext(), TAG);
    }

    private void updateList() {

        Map<String, String> filters = new HashMap<>();
        filters.put("username_creatore", this.user.getSub());
        filters.put("attivo", ""+this.showingActiveAnnounces);
        Util.showDialog(this.alertDialog, TAG);
        RemoteAPI.getAnnounceList(filters, (announces, exception) -> {

            if( exception != null ){

                Toast.makeText(this.requireContext(), "Errore durante il caricamento dei dati", Toast.LENGTH_SHORT)
                        .show();
                exception.printStackTrace();

            } else if ( announces != null ){

                this.recyclerView.setAdapter(new MyAnnounceListAdapter(announces, this.requireContext()));
                Log.i(TAG, announces.toString());
                Log.i(TAG, "Data retrivered");
                Util.dismissDialog(this.alertDialog, TAG);

            }

        });

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "ON RESUME");
        this.updateList();
    }
}
