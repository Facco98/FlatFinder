package it.unitn.disi.lpsmt.flatfinder.task;

import android.os.AsyncTask;

public class Task<P, R> extends AsyncTask<P, Void, R> {

    private Progression<P, R> progression;
    private Completion<R> completion;
    private Exception e = null;


    public Task(Progression<P,R> progression, Completion<R> completion){

        this.progression = progression;
        this.completion = completion;

    }

    @Override
    protected R doInBackground(P... ps) {

        R result = null;
        try {
            result = this.progression.run(ps);
        } catch (Exception ex) {
            this.e = ex;
        }
        return result;
    }

    @Override
    protected void onPostExecute(R r) {
        this.completion.onComplete(r, this.e);
    }
}
