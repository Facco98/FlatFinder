package it.unitn.disi.lpsmt.flatfinder.fragment;

import android.os.Bundle;
import android.widget.*;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import it.unitn.disi.lpsmt.flatfinder.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RicercaFiltriAppartamentoFragment extends Fragment {

    private EditText txtAffittoMin;
    private EditText txtAffittoMax;
    private EditText txtDisponibilita;
    private Spinner spnArredamento;
    private Spinner spnConsumoEnergetico;
    private Spinner spnPiano;
    private Spinner spnDimensioneMin;
    private Spinner spnDimensioneMax;
    private RadioGroup rdgLocali;
    private RadioGroup rdgBagni;
    private Button btnImpostaFiltri;

    public RicercaFiltriAppartamentoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ricerca_filtri_stanza, container, false);

        initUI(view);

        return view;
    }

    private void initUI(View view) {
        this.btnImpostaFiltri = view.findViewById(R.id.ricerca_filtri_appartamento_btn_imposta);
        this.spnArredamento = view.findViewById(R.id.ricerca_filtri_appartamento_spinner_arredamento);
        this.spnConsumoEnergetico = view.findViewById(R.id.ricerca_filtri_appartamento_spinner_classeenergetica);
        this.spnPiano = view.findViewById(R.id.ricerca_filtri_appartamento_spinner_piano);
        this.spnDimensioneMax = view.findViewById(R.id.ricerca_filtri_appartamento_spinner_dimensioneMax);
        this.spnDimensioneMin = view.findViewById(R.id.ricerca_filtri_appartamento_spinner_dimensioneMin);
        this.txtAffittoMax = view.findViewById(R.id.ricerca_filtri_appartamento_txt_affittoMax);
        this.txtAffittoMin = view.findViewById(R.id.ricerca_filtri_appartamento_txt_affittoMin);
        this.txtDisponibilita = view.findViewById(R.id.ricerca_filtri_appartamento_txt_disponibilità);
        this.rdgLocali = view.findViewById(R.id.ricerca_filtri_appartamento_rdgroup_nlocali);
        this.rdgBagni = view.findViewById(R.id.ricerca_filtri_appartamento_rdgroup_nbagni);

        this.btnImpostaFiltri.setOnClickListener(this::btnImpostaFiltriOnClick);
    }

    private void btnImpostaFiltriOnClick(View view) {
    }
}
