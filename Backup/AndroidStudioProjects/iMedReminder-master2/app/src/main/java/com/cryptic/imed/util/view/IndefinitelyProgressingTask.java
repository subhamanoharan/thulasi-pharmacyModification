package com.cryptic.imed.util.view;

import android.app.ProgressDialog;
import android.content.Context;
import roboguice.util.RoboAsyncTask;

/**
 * @author sharafat
 */
public class IndefinitelyProgressingTask<T> extends RoboAsyncTask {
    private String dialogMsg;
    private OnTaskExecutionListener<T> onTaskExecutionListener;
    private ProgressDialog dialog;

    public IndefinitelyProgressingTask(Context context, String dialogMsg,
                                       OnTaskExecutionListener<T> onTaskExecutionListener) {
        this(context, dialogMsg, onTaskExecutionListener, true);
    }

    public IndefinitelyProgressingTask(Context context, String dialogMsg,
                                       OnTaskExecutionListener<T> onTaskExecutionListener,
                                       boolean isCancelable) {
        super(context);
        this.dialogMsg = dialogMsg;
        this.onTaskExecutionListener = onTaskExecutionListener;
        dialog = new ProgressDialog(context);
        dialog.setCancelable(isCancelable);
    }

    @Override
    protected void onPreExecute() {
        dialog.setIndeterminate(true);
        dialog.setMessage(dialogMsg);
        dialog.show();
    }

    @Override
    public T call() throws Exception {
        return onTaskExecutionListener.execute();
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void onSuccess(Object result) {
        onTaskExecutionListener.onSuccess((T) result);
    }

    @Override
    protected void onException(Exception e) {
        onTaskExecutionListener.onException(e);
    }

    @Override
    protected void onFinally() {
        if (dialog.isShowing())
            dialog.dismiss();
    }


    public static interface OnTaskExecutionListener<T> {
        T execute();
        void onSuccess(T result);
        void onException(Exception e);
    }
}
