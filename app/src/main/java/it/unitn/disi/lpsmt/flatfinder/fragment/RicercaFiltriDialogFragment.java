package it.unitn.disi.lpsmt.flatfinder.fragment;

import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.FragmentContainerView;
import it.unitn.disi.lpsmt.flatfinder.R;

import java.util.HashMap;
import java.util.Map;

public class RicercaFiltriDialogFragment extends DialogFragment {

    private static final String TAG = "RicercaFiltriDialogFragment";

    private Spinner spnTipologia;
    private Spinner spnCategoria;
    private ImageButton btnChiudi;
    private SeekBar skbrRangeSlider;
    private TextView lblRaggio;
    private Button btnImpostaFiltri;
    private EditText txtAffittoMin;
    private EditText txtAffittoMax;
    private EditText txtDisponibilita;
    private Spinner spnArredamento;
    private Spinner spnConsumoEnergetico;
    private Spinner spnDimensioneMin;
    private Spinner spnDimensioneMax;
    private RadioGroup rdgLocali;
    private RadioGroup rdgBagni;
    private RadioButton rdbtnLocaliChecked, rdbtnBagniChecked;

    private Map<String, String> filters;

    private FilterCompletion completion;

    public RicercaFiltriDialogFragment( FilterCompletion completion ) {

        this.completion = completion;

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullscreenDialogTheme);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ricerca_filtri, container, false);

        initUI(view);

        return view;
    }

    private String getSpinnerValue(Spinner spinner){
        String value = spinner.getSelectedItem().toString();

        if(value.equals("-")){
            value = "";
        }

        return value;
    }

    private String getNumberOnly(String s){
        return s.replace("+", "");
    }

    private void updateMap(String key, String value){
        if(!value.isEmpty()){
            filters.put(key, value);
        }
    }

    private void getUserInput() {
        String arredamento = getSpinnerValue(spnArredamento);
        String consumo = getSpinnerValue(spnConsumoEnergetico);
        String dimMax = getSpinnerValue(spnDimensioneMax);
        String dimMin = getSpinnerValue(spnDimensioneMin);
        String affittoMax = txtAffittoMax.toString();
        String affittoMin = txtAffittoMin.toString();
        String disponibilita = txtDisponibilita.toString();
        String nlocaliMin = getNumberOnly(rdbtnLocaliChecked.getText().toString());
        String nbagniMin = getNumberOnly(rdbtnBagniChecked.getText().toString());
        String raggio = skbrRangeSlider.getProgress()+"";
        String categoria = getSpinnerValue(spnCategoria);
        String tipologia = getSpinnerValue(spnTipologia);

        updateMap("arredamento", arredamento);
        updateMap("classe_energetica", consumo);
        updateMap("dimensionemax", dimMax);
        updateMap("dimensionemin", dimMin);
        updateMap("affitto_mensilemax", affittoMax);
        updateMap("affitto_mensilemin", affittoMin);
        updateMap("inizio_disponibilita", disponibilita);
        updateMap("numero_localimin", nlocaliMin);
        updateMap("numero_bagni", nbagniMin);
        updateMap("raggio", raggio);
        updateMap("categoria", categoria);
        updateMap("tipologia", tipologia);

    }

    private void initUI(View view){
        this.spnCategoria = view.findViewById(R.id.ricerca_filtri_spinner_categoria);
        this.spnTipologia = view.findViewById(R.id.ricerca_filtri_spinner_tipologia_struttura);
        this.btnChiudi = view.findViewById(R.id.ricerca_filtri_btn_chiudi);
        this.skbrRangeSlider = view.findViewById(R.id.ricerca_filtri_skbr_rangeSlider);
        this.lblRaggio = view.findViewById(R.id.ricerca_filtri_lbl_raggio);
        this.btnImpostaFiltri = view.findViewById(R.id.ricerca_filtri_btn_imposta);

        this.spnArredamento = view.findViewById(R.id.ricerca_filtri_spinner_arredamento);
        this.spnConsumoEnergetico = view.findViewById(R.id.ricerca_filtri_spinner_classeenergetica);
        this.spnDimensioneMax = view.findViewById(R.id.ricerca_filtri_spinner_dimensioneMax);
        this.spnDimensioneMin = view.findViewById(R.id.ricerca_filtri_spinner_dimensioneMin);
        this.txtAffittoMax = view.findViewById(R.id.ricerca_filtri_txt_affittoMax);
        this.txtAffittoMin = view.findViewById(R.id.ricerca_filtri_txt_affittoMin);
        this.txtDisponibilita = view.findViewById(R.id.ricerca_filtri_txt_disponibilità);
        this.rdgLocali = view.findViewById(R.id.ricerca_filtri_rdgroup_nlocali);
        this.rdgBagni = view.findViewById(R.id.ricerca_filtri_rdgroup_nbagni);

        this.rdgLocali.check(R.id.ricerca_filtri_rdbtn_nlocaliDefault);
        this.rdgBagni.check(R.id.ricerca_filtri_rdbtn_nbagniDefault);

        this.btnChiudi.setOnClickListener(this::btnChiudiOnClick);
        this.btnImpostaFiltri.setOnClickListener(this::btnImpostaFiltriOnClick);
        this.rdgLocali.setOnCheckedChangeListener(this::rdgLocaliOnChange);
        this.rdgBagni.setOnCheckedChangeListener(this::rdgBagniOnChange);

        this.skbrRangeSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String raggio = "";

                switch(progress){
                    case -2: raggio = "250 m"; break;
                    case -1: raggio = "500 m"; break;
                    case 0: raggio = "750 m"; break;
                    default: raggio = progress + " Km";
                }

                lblRaggio.setText(raggio);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    private void rdgBagniOnChange(RadioGroup radioGroup, int i) {
        this.rdbtnBagniChecked = radioGroup.findViewById(i);
    }

    private void rdgLocaliOnChange(RadioGroup radioGroup, int i) {
        this.rdbtnLocaliChecked = radioGroup.findViewById(i);
    }

    private void btnImpostaFiltriOnClick(View view) {
        filters = new HashMap<>();
        getUserInput();
        this.completion.onFilterChooseComplete(filters);
        this.dismiss();
    }

    private void btnChiudiOnClick(View view) {
        this.dismiss();
    }
}
