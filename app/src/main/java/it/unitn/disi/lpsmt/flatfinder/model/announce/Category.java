package it.unitn.disi.lpsmt.flatfinder.model.announce;

public enum Category {

    A("Appartamento"),
    S("Camera singola"),
    D("Camera doppia"),
    O("Altro");

    public final String description;

    Category(String description){

        this.description = description;

    }

}
