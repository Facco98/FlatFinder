package it.unitn.disi.lpsmt.flatfinder;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.material.navigation.NavigationView;
import it.unitn.disi.lpsmt.flatfinder.model.User;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "Home Activity";
    public static final String USER_KEY = "User";

    @Nullable
    private User user;

    private Button btnCercaAnnuncio;
    private Button btnPubblicaAnnuncio;
    private NavigationView navigationView;
    private TextView lblCiao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        this.setupUI();
        Intent i = this.getIntent();
        if( i != null && i.hasExtra(USER_KEY) ){

            Bundle b = i.getBundleExtra(USER_KEY);
            if( b!= null ) {

                String nome = b.getString(User.NAME_KEY);
                String cognome = b.getString(User.FAMILY_NAME_KEY);
                String email = b.getString(User.EMAIL_KEY);
                String numeroTelefono = b.getString(User.PHONE_NUMBER_KEY);

                if( nome != null && cognome != null && email != null)
                    this.user = new User(email, nome, cognome, numeroTelefono);
            }

        }

        if( this.user == null )
            this.finish();
        else
            this.aggiornaViewsUtente();
    }

    private void btnCercaAnnuncioDidClick(View v){

        Log.d(TAG, "Button Cerca annuncio did click");

    }

    private void btnPubblicaAnnuncioDicClick(View v){
        Log.d(TAG, "Button Pubblica annuncio did click");
    }

    private void setupUI(){

        this.btnCercaAnnuncio = this.findViewById(R.id.home_btn_search);
        this.btnPubblicaAnnuncio = this.findViewById(R.id.home_btn_post);
        this.navigationView = this.findViewById(R.id.home_view_navigationView);
        View headerLayout = this.navigationView.inflateHeaderView(R.layout.sidebar_header);
        this.lblCiao = headerLayout.findViewById(R.id.sidebar_lbl_hi);


        this.btnCercaAnnuncio.setOnClickListener(this::btnCercaAnnuncioDidClick);
        this.btnPubblicaAnnuncio.setOnClickListener(this::btnPubblicaAnnuncioDicClick);

    }

    private void aggiornaViewsUtente() {

        assert this.user != null;
        String heading = this.getString(R.string.sidebar_lbl_hi_heading);
        StringBuilder builder = new StringBuilder();
        builder.append(heading).append(this.user.getName());

        this.lblCiao.setText(builder.toString());

    }

}
