package it.unitn.disi.lpsmt.flatfinder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import it.unitn.disi.lpsmt.flatfinder.R;
import it.unitn.disi.lpsmt.flatfinder.model.User;
import it.unitn.disi.lpsmt.flatfinder.model.announce.Photo;
import it.unitn.disi.lpsmt.flatfinder.remote.Authentication;
import it.unitn.disi.lpsmt.flatfinder.remote.RemoteAPI;

import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "Home Activity";
    public static final String USER_KEY = "User";

    @Nullable
    private User user;

    private Button btnCercaAnnuncio;
    private Button btnPubblicaAnnuncio;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private TextView lblCiao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        this.setupUI();
        this.user = User.getCurrentUser();
        if( this.user == null ) {
            Log.e(TAG, "USER IS NOT LOGGED IN");
            this.finish();
        }
        else {
            this.aggiornaViewsUtente();
        }
    }

    private void btnCercaAnnuncioDidClick(View v){

        Log.d(TAG, "Button Cerca annuncio did click");
        Intent i = new Intent(this, SearchActivity.class);
        this.startActivity(i);


    }

    private void btnPubblicaAnnuncioDicClick(View v){

        Log.d(TAG, "Button Pubblica annuncio did click");
        Intent i = new Intent(this, NewAnnounceActivity.class);
        this.startActivity(i);

    }

    private void setupUI(){

        this.btnCercaAnnuncio = this.findViewById(R.id.home_btn_search);
        this.btnPubblicaAnnuncio = this.findViewById(R.id.home_btn_post);
        this.navigationView = this.findViewById(R.id.home_view_navigationView);
        View headerLayout = this.navigationView.inflateHeaderView(R.layout.sidebar_header);
        this.lblCiao = headerLayout.findViewById(R.id.sidebar_lbl_hi);
        this.drawerLayout = this.findViewById(R.id.home_lyt_drawerLayout);
        Toolbar toolbar = this.findViewById(R.id.home_tlbr_toolbar);
        this.navigationView.getMenu().findItem(R.id.sidebar_item_home).setChecked(true);

        this.btnCercaAnnuncio.setOnClickListener(this::btnCercaAnnuncioDidClick);
        this.btnPubblicaAnnuncio.setOnClickListener(this::btnPubblicaAnnuncioDicClick);
        this.navigationView.setNavigationItemSelectedListener(this::menuItemDidClick);
        toolbar.setOnClickListener((view) -> this.drawerLayout.openDrawer(GravityCompat.START));


    }


    private void aggiornaViewsUtente() {


        String heading = this.getString(R.string.sidebar_lbl_hi_heading);
        StringBuilder builder = new StringBuilder();
        builder.append(heading).append(" ").append(this.user.getName());

        this.lblCiao.setText(builder.toString());



    }

    private boolean menuItemDidClick(@NonNull MenuItem menuItem){
        Log.i(TAG, menuItem.getTitle().toString() + " did Click");
        if( menuItem.isChecked() )
            return false;
        Intent i = null;
        switch( menuItem.getItemId() ){

            case R.id.sidebar_item_home:
                i = new Intent(this, HomeActivity.class);
                break;

            case R.id.sidebar_item_logout:
                this.handleLogout();
                break;

            case R.id.sidebar_item_pubblicaAnnuncio:
                i = new Intent(this, NewAnnounceActivity.class);
                break;

            case R.id.sidebar_item_cercaAlloggio:
                i = new Intent( this, SearchActivity.class);
                break;

            case R.id.sidebar_item_annunciPubblicati:
                i = new Intent(this, MyAnnouncesActivity.class);
                break;

            case R.id.sidebar_item_profilo:
                i = new Intent(this, ProfileActivity.class);
                break;

            case R.id.sidebar_item_AnnunciSalvati:
                i=new Intent(this, FavoritesActivity.class);
                break;

            default:
                Log.i(TAG, "NOT IMPLEMENTED YET");
        }

        if( i != null )
            this.intentNotNull(i);
        return true;

    }

    private void handleLogout(){

        Authentication.logout();
        User.setCurrentUser(null);
        this.recreate();

    }

    private void intentNotNull(@NonNull Intent i){

        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        this.drawerLayout.closeDrawer(GravityCompat.START);
        this.startActivity(i);

    }

}
