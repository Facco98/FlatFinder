package it.unitn.disi.lpsmt.flatfinder.fragment;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import it.unitn.disi.lpsmt.flatfinder.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RicercaFiltriStanzaFragment extends Fragment {

    private EditText txtAffittoMin;
    private EditText txtAffittoMax;
    private EditText txtDisponibilita;
    private Spinner spnArredamento;
    private Spinner spnConsumoEnergetico;
    private Spinner spnPiano;
    private Button btnImpostaFiltri;


    public RicercaFiltriStanzaFragment() {
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
        this.btnImpostaFiltri = view.findViewById(R.id.ricerca_filtri_stanza_btn_imposta);
        this.spnArredamento = view.findViewById(R.id.ricerca_filtri_stanza_spinner_arredamento);
        this.spnConsumoEnergetico = view.findViewById(R.id.ricerca_filtri_stanza_spinner_classeenergetica);
        this.spnPiano = view.findViewById(R.id.ricerca_filtri_stanza_spinner_piano);
        this.txtAffittoMax = view.findViewById(R.id.ricerca_filtri_stanza_txt_affittoMax);
        this.txtAffittoMin = view.findViewById(R.id.ricerca_filtri_stanza_txt_affittoMin);
        this.txtDisponibilita = view.findViewById(R.id.ricerca_filtri_stanza_txt_disponibilità);

        this.btnImpostaFiltri.setOnClickListener(this::btnImpostaFiltriOnClick);
    }

    private void btnImpostaFiltriOnClick(View view) {
    }
}
