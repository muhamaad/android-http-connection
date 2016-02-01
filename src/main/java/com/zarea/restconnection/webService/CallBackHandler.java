package com.zarea.restconnection.webService;

/**
 * Created by zarea on 6/26/15.
 */
public interface CallBackHandler<T> {
    public void callBackOnSuccess(T object , int responseCode);


    public void callBackOnFailing(T object , int responseCode);
}