package it.unitn.disi.lpsmt.flatfinder.task;

public interface Progression<P, R> {

    R run(P... params) throws Exception;

}
