package it.unitn.disi.lpsmt.flatfinder.model.announce;

public enum FornitureStatus {

    A("Arredato"),
    P("Parzialmente arredato"),
    N("Non arredato");

    public final String description;

    FornitureStatus(String description){

        this.description = description;

    }

}
