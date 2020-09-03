package it.unitn.disi.lpsmt.flatfinder.task;

import androidx.annotation.Nullable;

public interface Completion<T> {

    void onComplete(@Nullable T param, @Nullable Exception exception);

}
