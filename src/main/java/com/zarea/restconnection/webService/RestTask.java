package com.zarea.restconnection.webService;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zarea.restconnection.httpConnection.HttpConnection;
import com.zarea.restconnection.httpConnection.ResponseHttpObject;
import com.zarea.restconnection.utilits.ResponseCode;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * Created by zarea on 6/25/15.
 */
public class RestTask<T> extends AsyncTask<Object, Void, T> {
    private static HashMap<String, String> params;
    public Caller<T> delegate = null;
    String opration;
    String response;
    ResponseHttpObject responseHttpObject;
    private Class<T> tClass;
    private Context context;
    private String url;

    /**
     *
     * @param context
     * @param tClass
     * @param url
     * @param params
     * @param opration
     */
    public RestTask(Context context, Class<T> tClass, String url, HashMap<String, String> params, String opration) {
        this.context = context;
        this.tClass = tClass;
        this.url = url;
        this.params = params;
        this.opration = opration;
    }


    @Override
    protected T doInBackground(Object... params) {
        responseHttpObject = doJsonObject(url);
        ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
        T map = null;
        try {
            if (responseHttpObject.getResult() != null) {
                Log.d("response", responseHttpObject.getResult());
                map = mapper.readValue(responseHttpObject.getResult(), tClass);
                return map;
            }
        } catch (JsonParseException e) {
            e.printStackTrace();
            try {
                responseHttpObject.setResponseCode(ResponseCode.RESPONSE_ERROR_CODE);
                responseHttpObject.setResult(ResponseCode.MAPPER_ERROR);
                map = mapper.readValue(responseHttpObject.getResult(), tClass);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    @Override
    protected void onPostExecute(T example) {
        if (responseHttpObject.getResponseCode() == 200) {
            this.delegate.onSuccess(example , responseHttpObject.getResponseCode());
        } else if (responseHttpObject.getResponseCode() == 401) {
            Toast.makeText(context,"Unauthorized",Toast.LENGTH_SHORT).show();
        } else {
            this.delegate.onFailing(example , responseHttpObject.getResponseCode());
        }
    }
    public ResponseHttpObject doJsonObject(String url) {
        ResponseHttpObject responseHttpObject = new ResponseHttpObject();
        try {
            Method method = HttpConnection.class.getDeclaredMethod(opration, String.class, HashMap.class);
            responseHttpObject = (ResponseHttpObject) method.invoke(HttpConnection.class, url, params);
        } catch (Exception ex) {
            Log.e("doJSON", "Exception", ex);
            //Log.e("doJSON", ex.getMessage());
        }
        return responseHttpObject;
    }
}
