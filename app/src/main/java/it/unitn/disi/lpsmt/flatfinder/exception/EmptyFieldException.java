package it.unitn.disi.lpsmt.flatfinder.exception;

public class EmptyFieldException extends Exception {

    public final String field;

    public EmptyFieldException( String field ){

        super();
        this.field = field;

    }



}
