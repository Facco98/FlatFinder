package it.unitn.disi.lpsmt.flatfinder.fragment;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import it.unitn.disi.lpsmt.flatfinder.R;

public class RicercaFiltriFragment extends Fragment {

    private Spinner spnTipologia;
    private Spinner spnCategoria;
    private Button btnAvanti;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ricerca_filtri, container, false);

        initUI(view);

        return view;
    }

    private void initUI(View view){
        this.spnCategoria = view.findViewById(R.id.ricerca_filtri_spinner_categoria);
        this.spnTipologia = view.findViewById(R.id.ricerca_filtri_spinner_tipologia_struttura);
        this.btnAvanti = view.findViewById(R.id.ricerca_filtri_btn_avanti);

        this.btnAvanti.setOnClickListener(this::btnAvantiOnClick);

    }

    private void btnAvantiOnClick(View view) {
        String categoria = this.spnCategoria.getSelectedItem().toString();
        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        int currentFragmentId = ((ViewGroup)getView().getParent()).getId();
        Fragment nextFragment;

        if(categoria.equals("Appartamento") || categoria.equals("Altro")){
            nextFragment = new RicercaFiltriAppartamentoFragment();
        } else {
            nextFragment = new RicercaFiltriStanzaFragment();
        }

        activity.getSupportFragmentManager().beginTransaction().replace(currentFragmentId, nextFragment).addToBackStack(null).commit();
    }
}
