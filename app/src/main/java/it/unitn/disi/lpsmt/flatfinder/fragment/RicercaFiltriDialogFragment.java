package it.unitn.disi.lpsmt.flatfinder.fragment;

import android.os.Bundle;
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

    private Spinner spnTipologia;
    private Spinner spnCategoria;
    private ImageButton btnChiudi;

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

        return view;
    }

    private void initUI(View view){
        this.spnCategoria = view.findViewById(R.id.ricerca_filtri_spinner_categoria);
        this.spnTipologia = view.findViewById(R.id.ricerca_filtri_spinner_tipologia_struttura);
        this.btnChiudi = view.findViewById(R.id.ricerca_filtri_btn_chiudi);

        btnChiudi.setOnClickListener(this::btnChiudiOnClick);

        /*spnCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String categoria = spnCategoria.getSelectedItem().toString();
                int fragmentId = R.id.ricerca_filtri_view_fragment;
                Fragment fragment;

                if(categoria.equals("Appartamento") || categoria.equals("Altro")){
                    fragment = new RicercaFiltriAppartamentoFragment();
                } else {
                    fragment = new RicercaFiltriStanzaFragment();
                }

                getChildFragmentManager().
                        beginTransaction().
                        replace(fragmentId, fragment).
                        commit();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

         */

    }

    private void btnChiudiOnClick(View view) {
        this.dismiss();
    }
}
