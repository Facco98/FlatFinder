package it.unitn.disi.lpsmt.flatfinder.task;

import it.unitn.disi.lpsmt.flatfinder.exception.EmailNotVerifiedException;

public interface Progression<P, R> {

    R run(P... params) throws Exception, EmailNotVerifiedException;

}
