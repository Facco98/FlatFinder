package it.unitn.disi.lpsmt.flatfinder.fragment;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import it.unitn.disi.lpsmt.flatfinder.R;

public class ReimpostaPasswordFragment extends Fragment {

    private Button btnReimposta;
    private EditText txtPassword, txtPasswordConferma;

    public ReimpostaPasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reimposta_password, container, false);

        setupUI(view);

        return view;
    }

    private void setupUI(View view) {
        this.btnReimposta = view.findViewById(R.id.password_btn_reimposta);
        this.txtPassword = view.findViewById(R.id.password_txt_password);
        this.txtPasswordConferma = view.findViewById(R.id.password_txt_passwordRipeti);

        this.btnReimposta.setOnClickListener(this::btnReimpostaOnClick);

    }

    private void btnReimpostaOnClick(View view) {
        String password = this.txtPassword.getText().toString();
        String confermaPassword = this.txtPasswordConferma.getText().toString();

        if (!password.equals(confermaPassword)) {

            Toast.makeText(this.getContext(), R.string.password_non_corrispondenti, Toast.LENGTH_LONG).show();

        }

        DialogFragment parent = (DialogFragment) getParentFragment();
        parent.dismiss();
    }
}