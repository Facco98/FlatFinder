package it.unitn.disi.lpsmt.flatfinder.fragment;

import android.media.Image;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import it.unitn.disi.lpsmt.flatfinder.R;

public class RecuperaPasswordDialogFragment extends DialogFragment {

    private ImageButton btnChiudi;


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
        inflateFragment();

        return view;
    }

    private void inflateFragment() {
        Fragment fragment = new RecuperaPasswordEmailFragment();
        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.password_lyt_fragment, fragment, fragment.getClass().getSimpleName())
                .commit();
    }

    private void setupUI(View view) {
        this.btnChiudi = view.findViewById(R.id.password_btn_chiudi);

        this.btnChiudi.setOnClickListener(this::btnChiudiOnClick);
    }

    private void btnChiudiOnClick(View view) {
        this.dismiss();
    }


}