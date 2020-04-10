package it.unitn.disi.lpsmt.flatfinder.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;

public class User implements Serializable {

    @NonNull
    private String email;

    @NonNull
    private String name;

    @NonNull
    private String family_name;

    @Nullable
    private String phone_number;

    public User(@NonNull String email, @NonNull String name, @NonNull String family_name, @Nullable String phone_number){

        this.email = email;
        this.name = name;
        this.family_name = family_name;

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
}
