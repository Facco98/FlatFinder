package it.unitn.disi.lpsmt.flatfinder.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

public class User implements Serializable{

    private static User currentUser = null;

    public static final String EMAIL_KEY = "email";
    public static final String NAME_KEY = "name";
    public static final String FAMILY_NAME_KEY = "family_name";
    public static final String PHONE_NUMBER_KEY = "phone_number";

    @NonNull
    private String email;

    @NonNull
    private String name;

    @NonNull
    private String family_name;

    @Nullable
    private String phone_number;

    @Nullable
    private String sub;

    @Nullable
    private Boolean male;

    @Nullable
    private String profileImg;

    public User(@NonNull String email, @NonNull String name, @NonNull String family_name, @Nullable String phone_number, @Nullable String sub, @Nullable Boolean male, @Nullable String profileImg){

        this.email = email;
        this.name = name;
        this.family_name = family_name;
        this.sub = sub;
        this.male = male;
        this.profileImg = profileImg;
        this.phone_number = phone_number;
    }


    @Nullable
    public Boolean getMale() {
        return male;
    }

    public void setMale(@Nullable Boolean male) {
        this.male = male;
    }

    @Nullable
    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(@Nullable String profileImg) {
        this.profileImg = profileImg;
    }



    @NonNull
    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getFamily_name() {
        return family_name;
    }

    public void setFamily_name(@NonNull String family_name) {
        this.family_name = family_name;
    }

    @Nullable
    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(@Nullable String phone_number) {
        this.phone_number = phone_number;
    }

    @Nullable
    public String getSub() {
        return sub;
    }

    public void setSub(@Nullable String sub) {
        this.sub = sub;
    }

    @Override
    @NonNull
    public String toString(){

        StringBuilder sb = new StringBuilder();
        sb.append("User[email=").append(this.email);
        sb.append(",name=").append(this.name);
        sb.append(",family_name=").append(this.family_name);
        sb.append(",phone_number").append(this.phone_number);
        sb.append("]");

        return sb.toString();

    }

    public Bitmap getProfileImgBmp(){

        byte[] bytes = Base64.decode(this.profileImg, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

    }



    public static void setCurrentUser(User u){

        currentUser = u;

    }

    @Nullable
    public static User getCurrentUser(){

        return currentUser;

    }

    public static boolean isSignedIn(){

        return currentUser != null;

    }



}
