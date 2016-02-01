package com.zarea.restconnection.webService;

import android.content.Context;

/**
 * Created by zarea on 6/26/15.
 */
public class ActivityCaller<T> implements Caller<T> {
    Context context;
    CallBackHandler caller;

    public ActivityCaller(Context context, CallBackHandler caller) {
        this.context = context;
        this.caller = caller;
    }

    /**
     *
     * @param example
     * @param responseCode
     */
    @Override
    public void onSuccess(T example, int responseCode) {
        caller.callBackOnSuccess(example, responseCode);
    }

    /**
     *
     * @param example
     * @param responseCode
     */
    @Override
    public void onFailing(T example, int responseCode) {
        caller.callBackOnFailing(example, responseCode);
    }
}