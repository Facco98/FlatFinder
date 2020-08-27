package it.unitn.disi.lpsmt.flatfinder.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import it.unitn.disi.lpsmt.flatfinder.R;
import org.w3c.dom.Text;

import java.text.DecimalFormat;

public class SalvaRicercaDialogFragment extends DialogFragment{

    private static final String TAG = "SalvaRicercaDialogFragment";

    private TextView lblIndirizzo, lblRaggio;
    private CheckBox chkboxApp, chkboxEmail;
    private ImageButton btnChiudi;
    private Button btnSalva;

    public SalvaRicercaDialogFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullscreenDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ricerca_salva, container, false);

        this.setupUI(view);

        return view;
    }

    private void setIndirizzoAndRaggio(String indirizzo, String raggio){
        this.lblIndirizzo.setText(indirizzo);
        this.lblRaggio.setText("Entro " + raggio + " Km");
    }

    private void setupUI(View view) {
        this.lblIndirizzo = view.findViewById(R.id.ricerca_salva_zona_lbl_indirizzo);
        this.lblRaggio = view.findViewById(R.id.ricerca_salva_zona_lbl_raggio);
        this.chkboxApp = view.findViewById(R.id.ricerca_salva_zona_chkbox_notificheApp);
        this.chkboxEmail = view.findViewById(R.id.ricerca_salva_zona_chkbox_notificheEmail);
        this.btnChiudi = view.findViewById(R.id.ricerca_salva_zona_btn_chiudi);
        this.btnSalva = view.findViewById(R.id.ricerca_salva_zona_btn_salva);

        this.btnChiudi.setOnClickListener(this::btnChiudiOnClick);
        this.btnSalva.setOnClickListener(this::btnSalvaOnClick);
    }

    private void btnSalvaOnClick(View view) {

        this.dismiss();
    }

    private void btnChiudiOnClick(View view) {
        this.dismiss();
    }
}
