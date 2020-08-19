package it.unitn.disi.lpsmt.flatfinder.fragment;

import android.os.Bundle;
import android.widget.Adapter;
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
import it.unitn.disi.lpsmt.flatfinder.model.announce.Announce;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyAnnouncesListFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Announce> announceList;

    public MyAnnouncesListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.i_miei_annunci_list, container, false);

        setupUI(view);

        this.setRecyclerViewAdapter(view);

        return view;
    }

    private void setRecyclerViewAdapter(View view) {
        this.layoutManager = new LinearLayoutManager(view.getContext());
        this.recyclerView.setLayoutManager(this.layoutManager);

        // TODO get announceList from database

        if(announceList == null){
            announceList = new ArrayList<>();
        }

        this.adapter = new MyAnnounceListAdapter(this.announceList, view.getContext());
        this.recyclerView.setAdapter(this.adapter);
    }

    private void setupUI(View view) {
        this.recyclerView = view.findViewById(R.id.miei_annunci_view_annunciList);
    }
}
