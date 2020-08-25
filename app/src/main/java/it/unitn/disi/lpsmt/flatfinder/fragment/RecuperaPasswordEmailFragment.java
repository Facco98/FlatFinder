package it.unitn.disi.lpsmt.flatfinder.fragment;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import it.unitn.disi.lpsmt.flatfinder.R;

public class RecuperaPasswordEmailFragment extends Fragment {

    private EditText txtEmail;
    private Button btnProcedi;

    public RecuperaPasswordEmailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recupera_password_email, container, false);

        setupUI(view);

        return view;
    }


    private void setupUI(View view) {
        this.txtEmail = view.findViewById(R.id.password_txt_email);
        this.btnProcedi = view.findViewById(R.id.password_btn_procedi);

        this.btnProcedi.setOnClickListener(this::btnProcediOnClick);
    }

    private void btnProcediOnClick(View view) {
        // replace password linearlayout with reimposta fragment
        Fragment fragment = new ReimpostaPasswordFragment();
        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.password_lyt_fragment, fragment, fragment.getClass().getSimpleName())
                .commit();
    }
}