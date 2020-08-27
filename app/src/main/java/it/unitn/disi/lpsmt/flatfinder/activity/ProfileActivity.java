package it.unitn.disi.lpsmt.flatfinder.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import it.unitn.disi.lpsmt.flatfinder.R;
import it.unitn.disi.lpsmt.flatfinder.model.User;
import it.unitn.disi.lpsmt.flatfinder.model.announce.Photo;
import it.unitn.disi.lpsmt.flatfinder.remote.Authentication;
import it.unitn.disi.lpsmt.flatfinder.util.Util;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";

    private ImageView imgProfileImage;
    private EditText txtNome, txtCognome, txtEmail, txtTelefono;
    private RadioGroup rdgSesso;
    private Button btnSalva, btnCambiaImmagineProfilo;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.profilo);
        this.user = User.getCurrentUser();
        if( this.user == null ){

            Log.i(TAG, "USER IS NOT LOGGED IN");
            this.finish();

        }
        this.setupUI();
    }

    private void setupToolbar() {
        Toolbar toolbar = this.findViewById(R.id.profilo_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
    }

    private void setupUI() {

        setupToolbar();

        this.imgProfileImage = this.findViewById(R.id.profilo_img_immagineDiProfilo);
        if( this.user.getProfileImg() != null )
            this.imgProfileImage.setImageBitmap(this.user.getProfileImgBmp());
        this.txtNome = this.findViewById(R.id.profilo_txt_nome);
        this.txtNome.setText(this.user.getName());
        this.txtCognome = this.findViewById(R.id.profilo_txt_cognome);
        this.txtCognome.setText(this.user.getFamily_name());
        this.txtEmail = this.findViewById(R.id.profilo_txt_email);
        this.txtEmail.setText(this.user.getEmail());
        this.txtTelefono = this.findViewById(R.id.profilo_txt_numeroTelefono);
        this.txtTelefono.setText(this.user.getPhone_number());
        this.rdgSesso = this.findViewById(R.id.profilo_rdgroup_sesso);
        if( this.user.getMale() != null && this.user.getMale() )
            this.rdgSesso.check(R.id.profilo_rdgroup_sesso_male);
        else if( this.user.getMale() != null )
            this.rdgSesso.check(R.id.profilo_rdgroup_sesso_female);
        this.btnSalva = this.findViewById(R.id.profilo_btn_salva);
        this.btnCambiaImmagineProfilo = this.findViewById(R.id.profilo_btn_cambiaImmagine);

        this.btnCambiaImmagineProfilo.setOnClickListener(this::btnCambiaImmagineProfiloOnClick);
        this.btnSalva.setOnClickListener(this::btnSalvaOnClick);
    }

    private void btnCambiaImmagineProfiloOnClick(View view) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/png");
        this.startActivityForResult(intent, Util.SELECT_IMAGE);
    }

    private void btnSalvaOnClick(View view) {

        // ritorna alla pagina home
        switch(this.rdgSesso.getCheckedRadioButtonId()){

            case R.id.profilo_rdgroup_sesso_male:
                this.user.setMale(true);
                break;

            case R.id.profilo_rdgroup_sesso_female:
                this.user.setMale(false);
                break;

            default:
                Log.e(TAG, "Invalid value: " + this.rdgSesso.getCheckedRadioButtonId());

        }
        this.user.setFamily_name(this.txtCognome.getText().toString());
        this.user.setName(this.txtNome.getText().toString());
        this.user.setPhone_number(this.txtTelefono.getText().toString());
        Authentication.updateProfile(this.user, (value, err) -> {

            if( err != null ) {
                Toast.makeText(this, R.string.profile_error_update, Toast.LENGTH_SHORT).show();
                err.printStackTrace();
            }
            else if ( value != null ) {
                Toast.makeText(this, R.string.profile_updated, Toast.LENGTH_SHORT).show();
                this.finish();
            }

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == Util.SELECT_IMAGE && data != null && data.getData() != null) {

                try {
                    InputStream is = this.getContentResolver().openInputStream(data.getData());
                    Bitmap bmp = BitmapFactory.decodeStream(is);
                    is.close();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    this.user.setProfileImg(Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT));
                    this.imgProfileImage.setImageBitmap(bmp);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}