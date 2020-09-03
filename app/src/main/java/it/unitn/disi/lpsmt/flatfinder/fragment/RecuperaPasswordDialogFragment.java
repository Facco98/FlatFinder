package it.unitn.disi.lpsmt.flatfinder.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.fragment.app.DialogFragment;
import it.unitn.disi.lpsmt.flatfinder.R;
import it.unitn.disi.lpsmt.flatfinder.remote.Authentication;

public class RecuperaPasswordDialogFragment extends DialogFragment {

    private ImageButton btnChiudi;
    private EditText txtEmail;
    private Button btnProcedi;


    public RecuperaPasswordDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullscreenDialogTheme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recupera_password_dialog, container, false);

        this.setupUI(view);

        return view;
    }

    private void setupUI(View view) {
        this.btnChiudi = view.findViewById(R.id.password_btn_chiudi);
        this.txtEmail = view.findViewById(R.id.password_txt_email);
        this.btnProcedi = view.findViewById(R.id.password_btn_procedi);

        this.btnProcedi.setOnClickListener(this::btnProcediOnClick);
        this.btnChiudi.setOnClickListener(this::btnChiudiOnClick);
    }

    private void btnChiudiOnClick(View view) {
        this.dismiss();
    }

    private void btnProcediOnClick(View view) {
        if(TextUtils.isEmpty(txtEmail.getText())){
            Toast.makeText(this.requireContext(), "Campo email vuoto", Toast.LENGTH_SHORT).show();
        } else {
            Authentication.forgotPassword(this.txtEmail.getText().toString(), (res, err) -> {

                if( err != null )
                    err.printStackTrace();
                else if ( res != null )
                    Toast.makeText(this.requireContext(), R.string.email_inviata_verifica, Toast.LENGTH_SHORT).show();

            });
            this.dismiss();
        }
    }


}