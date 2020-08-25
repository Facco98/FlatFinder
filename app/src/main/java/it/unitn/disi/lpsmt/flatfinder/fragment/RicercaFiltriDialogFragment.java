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

public class RicercaFiltriDialogFragment extends DialogFragment {

    private static final String TAG = "RicercaFiltriDialogFragment";

    private Spinner spnTipologia;
    private Spinner spnCategoria;
    private ImageButton btnChiudi;
    private SeekBar skbrRangeSlider;
    private TextView lblRaggio;
    private Button btnImpostaFiltri;

    public RicercaFiltriDialogFragment() {
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

        inflateFragment();

        return view;
    }

    private void inflateFragment() {
        Fragment fragment = new RicercaFiltriAppartamentoFragment();
        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.ricerca_filtri_view_fragment, fragment, fragment.getClass().getSimpleName())
                .commit();
    }

    private void initUI(View view){
        this.spnCategoria = view.findViewById(R.id.ricerca_filtri_spinner_categoria);
        this.spnTipologia = view.findViewById(R.id.ricerca_filtri_spinner_tipologia_struttura);
        this.btnChiudi = view.findViewById(R.id.ricerca_filtri_btn_chiudi);
        this.skbrRangeSlider = view.findViewById(R.id.ricerca_filtri_skbr_rangeSlider);
        this.lblRaggio = view.findViewById(R.id.ricerca_filtri_lbl_raggio);
        this.btnImpostaFiltri = view.findViewById(R.id.ricerca_filtri_btn_imposta);

        this.btnChiudi.setOnClickListener(this::btnChiudiOnClick);
        this.btnImpostaFiltri.setOnClickListener(this::btnImpostaFiltriOnClick);
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


        spnCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String categoria = spnCategoria.getSelectedItem().toString();
                int fragmentId = R.id.ricerca_filtri_view_fragment;
                Fragment fragment;

                if(categoria.equals("Appartamento") || categoria.equals("Altro")){
                    Log.d(TAG, "Appartamento or Altro selected");
                    fragment = new RicercaFiltriAppartamentoFragment();
                } else {
                    Log.d(TAG, "Stanza selected");
                    fragment = new RicercaFiltriStanzaFragment();
                }

                getChildFragmentManager().
                        beginTransaction().
                        replace(fragmentId, fragment, fragment.getClass().getSimpleName()).
                        commit();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void btnImpostaFiltriOnClick(View view) {
    }

    private void btnChiudiOnClick(View view) {
        this.dismiss();
    }
}
