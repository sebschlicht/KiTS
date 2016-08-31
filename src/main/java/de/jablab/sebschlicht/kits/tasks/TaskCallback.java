package de.jablab.sebschlicht.kits.tasks;

import android.os.AsyncTask;

/**
 * Completion callback of an asynchronous task.
 *
 * @author sebschlicht
 *
 * @param <T>
 *            type of additional information that may be passed on task
 *            completion
 */
public interface TaskCallback<T > {

    /**
     * Handles the completion of a task.
     *
     * @param result
     *            additional information passed by the task
     * @param taskClass
     *            class of the calling task
     */
    void handleResult(T result, Class<? extends AsyncTask<?, ?, ?>> taskClass);
}
