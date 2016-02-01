package com.zarea.restconnection.webService;

/**
 * Created by zarea on 6/25/15.
 */
public interface Caller<T> {
    /**
     *
     * @param example
     * @param responseCode
     */
    void onSuccess(T example , int responseCode);

    /**
     *
     * @param example
     * @param responseCode
     */
    void onFailing(T example , int responseCode);
}