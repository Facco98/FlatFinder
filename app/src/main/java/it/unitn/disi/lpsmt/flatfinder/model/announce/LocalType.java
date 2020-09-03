package it.unitn.disi.lpsmt.flatfinder.model.announce;

public enum LocalType {

    P("Privato"),
    S("Studentato"),
    O("Altro");

    public final String description;

    LocalType(String description){

        this.description = description;

    }

    public String toString(){

        return this.description;

    }

}
