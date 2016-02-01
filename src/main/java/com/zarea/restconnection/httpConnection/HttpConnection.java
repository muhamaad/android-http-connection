package com.zarea.restconnection.httpConnection;

import android.content.Context;
import android.util.Log;

import com.zarea.restconnection.utilits.Misc;
import com.zarea.restconnection.utilits.ResponseCode;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zarea on 6/26/15.
 */
public class HttpConnection {
    static int responseCode = 1;
    Context context;

    /**
     *
     * @param url
     * @param params
     * @return
     */
    public static InputStream post(String url, HashMap<String, String> params) {
        int timeoutConnection = 1000;
        int timeoutSocket = 5000;
        HttpClient httpclient = new DefaultHttpClient();

        HttpPost httpPost = new HttpPost(url);
        try {
            JSONObject jsonObj = new JSONObject();
            for (String key : params.keySet()) {
                jsonObj.put(key, params.get(key));
            }
            StringEntity entity = new StringEntity(jsonObj.toString(), HTTP.UTF_8);
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
            httpPost.setParams(httpParameters);
            DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
            HttpResponse httpResponse = httpclient.execute(httpPost);
            responseCode = httpResponse.getStatusLine().getStatusCode();
            if (responseCode == 401) {
                return null;
            }
            HttpEntity httpEntity = httpResponse.getEntity();
            return httpEntity.getContent();
        } catch (Exception ex) {
            Log.e("doJSON", ex.getMessage());
        }

        return null;
    }

    /**
     *
     * @param url
     * @param params
     * @return
     * @throws UnsupportedEncodingException
     */
    public static ResponseHttpObject get(String url, HashMap<String, String> params) throws UnsupportedEncodingException {
        int timeoutConnection = 5000;
        int timeoutSocket = 5000;
        HttpClient httpclient = new DefaultHttpClient();

        try {
            HttpParams httpParameters = new BasicHttpParams();

            if (params.get("PostId") != null) {
                String id = params.get("PostId");
                url = url + "/" + id;
                params.remove("PostId");
            }

            String para = "";
            int i = 1;
            for (String key : params.keySet()) {
                if (i == params.size()) {
                    para = para + "" + key + "=" + params.get(key);
                } else {
                    para = para + "" + key + "=" + params.get(key) + "&";
                    i++;
                }

                httpParameters.setParameter(key, params.get(key));
            }
            url = url + "?" + para;
            HttpGet httpget = new HttpGet(url);
            httpget.setParams(httpParameters);
            httpget.setHeader("Accept", "application/json");
            httpget.setHeader("Content-type", "application/json");
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
            httpget.setParams(httpParameters);
            DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
            HttpResponse httpResponse = httpclient.execute(httpget);
            responseCode = httpResponse.getStatusLine().getStatusCode();
            HttpEntity httpEntity = httpResponse.getEntity();
            InputStream response = httpEntity.getContent();
            return new ResponseHttpObject(responseCode, response);

        } catch (Exception ex) {
            Log.e("doJSON", ex.getMessage());
        }

        return new ResponseHttpObject(ResponseCode.RESPONSE_ERROR_CODE, new ByteArrayInputStream(ResponseCode.RESPONSE_Error.getBytes("UTF-8")));
    }

    /**
     *
     * @param url
     * @param params
     * @return
     * @throws UnsupportedEncodingException
     */
    public static ResponseHttpObject postStringJson(String url, HashMap<String, String> params) throws UnsupportedEncodingException {
        int timeoutConnection = 1000;
        int timeoutSocket = 5000;
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        try {
            List<NameValuePair> paramsNameValuePair = new ArrayList<NameValuePair>();
            for (String key : params.keySet()) {
                paramsNameValuePair.add(new BasicNameValuePair(key, params.get(key)));
            }
            StringEntity entity = new UrlEncodedFormEntity(paramsNameValuePair, HTTP.UTF_8);
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
            httpPost.setParams(httpParameters);
            DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
            HttpResponse httpResponse = httpclient.execute(httpPost);
            responseCode = httpResponse.getStatusLine().getStatusCode();
            HttpEntity httpEntity = httpResponse.getEntity();
            InputStream response = httpEntity.getContent();
            return new ResponseHttpObject(responseCode, response);

        } catch (Exception ex) {
            Log.e("doJSON", ex.getMessage());
        }

        return new ResponseHttpObject(ResponseCode.RESPONSE_ERROR_CODE, new ByteArrayInputStream(ResponseCode.RESPONSE_Error.getBytes("UTF-8")));
    }

    /**
     *
     * @param url
     * @param params
     * @return
     * @throws UnsupportedEncodingException
     */
    public static ResponseHttpObject upLoadImage(String url, HashMap<String, String> params) throws UnsupportedEncodingException {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);
        HttpResponse response;
        MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

        File file = new File(Misc.compressImage(params.get("profilePicture")));
       /* String path = params.get("profilePicture");

        Bitmap b = BitmapFactory.decodeFile(params.get("profilePicture"));
        Bitmap out = Bitmap.createScaledBitmap(b,  480, 480, false);
//        Matrix matrix = new Matrix();
//        matrix.postRotate(90);
        Bitmap afterRotate = Bitmap.createBitmap(out, 0, 0,  480, 480);
        FileOutputStream fOut;
        try {
            fOut = new FileOutputStream(file);
            out.compress(Bitmap.CompressFormat.JPEG, 50, fOut);
            afterRotate.compress(Bitmap.CompressFormat.JPEG, 50, fOut);
            fOut.flush();
            fOut.close();
            b.recycle();
            out.recycle();
            afterRotate.recycle();
        } catch (Exception e) {
        }*/
        params.remove("profilePicture");
        try {
            for (String key : params.keySet()) {
                entity.addPart(key, new StringBody(params.get(key)));
            }

            httppost.setHeader("Accept", "application/json");
            entity.addPart("profilePicture", new FileBody(file));

            httppost.setEntity(entity);
            response = httpclient.execute(httppost);
            int responseCode = response.getStatusLine().getStatusCode();
            InputStream content = response.getEntity().getContent();
            return new ResponseHttpObject(responseCode, content);

        } catch (Exception ex) {
            Log.e("doJSON", ex.getMessage());
        }

        return new ResponseHttpObject(ResponseCode.RESPONSE_ERROR_CODE, new ByteArrayInputStream(ResponseCode.RESPONSE_Error.getBytes("UTF-8")));
    }

    /**
     *
     * @param url
     * @param params
     * @return
     */
    public static InputStream upLoadVideo(String url, HashMap<String, String> params) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);
        HttpResponse response;
        MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        File file = new File(params.get("userVideo"));
        params.remove("userVideo");
        try {
            for (String key : params.keySet()) {
                entity.addPart(key, new StringBody(params.get(key)));
            }

            httppost.setHeader("Accept", "application/json");
            entity.addPart("userVideo", new FileBody(file));

            httppost.setEntity(entity);
            response = httpclient.execute(httppost);
            int responseCode = response.getStatusLine().getStatusCode();
            InputStream content = response.getEntity().getContent();
            return content;
        } catch (ClientProtocolException e) {
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     *
     * @param url
     * @param params
     * @return
     * @throws UnsupportedEncodingException
     */
    public static ResponseHttpObject put(String url, HashMap<String, String> params) throws UnsupportedEncodingException {
        int timeoutConnection = 5000;
        int timeoutSocket = 10000;
        HttpClient httpclient = new DefaultHttpClient();
        String id = params.get("id");
        url = url + "/" + id;
        params.remove("id");
        List<NameValuePair> paramsNameValuePair = new ArrayList<NameValuePair>();
        try {
            HttpParams httpParameters = new BasicHttpParams();
            for (String key : params.keySet()) {
                paramsNameValuePair.add(new BasicNameValuePair(key, params.get(key)));
            }
            HttpPut httpPut = new HttpPut(url);
            StringEntity entity = new UrlEncodedFormEntity(paramsNameValuePair, HTTP.UTF_8);
            httpPut.setEntity(entity);
            httpPut.setHeader("Accept", "application/json");
            httpPut.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
            httpPut.setParams(httpParameters);
            DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
            HttpResponse httpResponse = httpclient.execute(httpPut);
            responseCode = httpResponse.getStatusLine().getStatusCode();
            HttpEntity httpEntity = httpResponse.getEntity();
            InputStream response = httpEntity.getContent();
            return new ResponseHttpObject(responseCode, response);

        } catch (Exception ex) {
            Log.e("doJSON", ex.getMessage());
        }

        return new ResponseHttpObject(ResponseCode.RESPONSE_ERROR_CODE, new ByteArrayInputStream(ResponseCode.RESPONSE_Error.getBytes("UTF-8")));
    }

    /**
     *
     * @param url
     * @param params
     * @return
     * @throws UnsupportedEncodingException
     */
    public static ResponseHttpObject delete(String url, HashMap<String, String> params) throws UnsupportedEncodingException {
        int timeoutConnection = 5000;
        int timeoutSocket = 10000;
        HttpClient httpclient = new DefaultHttpClient();
        String para = "";
        String id = params.get("id");
        url = url + "/" + id;
        params.remove("id");

        int i = 1;
        for (String key : params.keySet()) {
            if (i == params.size()) {
                para = para + "" + key + "=" + params.get(key);
            } else {
                para = para + "" + key + "=" + params.get(key) + "&";
                i++;
            }
        }
        url = url + "?" + para;
        try {
            HttpParams httpParameters = new BasicHttpParams();
            HttpDelete httpDelete = new HttpDelete(url);
            httpDelete.setHeader("Accept", "application/json");
            httpDelete.setHeader("Content-type", "application/json");
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
            httpDelete.setParams(httpParameters);
            DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
            HttpResponse httpResponse = httpclient.execute(httpDelete);
            responseCode = httpResponse.getStatusLine().getStatusCode();
            HttpEntity httpEntity = httpResponse.getEntity();
            InputStream response = httpEntity.getContent();
            return new ResponseHttpObject(responseCode, response);

        } catch (Exception ex) {
            Log.e("doJSON", ex.getMessage());
        }

        return new ResponseHttpObject(ResponseCode.RESPONSE_ERROR_CODE, new ByteArrayInputStream(ResponseCode.RESPONSE_Error.getBytes("UTF-8")));
    }

    /**
     *
     * @param url
     * @param params
     * @return
     */
    public static InputStream deletePic(String url, HashMap<String, String> params) {
        int timeoutConnection = 5000;
        int timeoutSocket = 10000;
        HttpClient httpclient = new DefaultHttpClient();
        String para = "";

        int i = 1;
        for (String key : params.keySet()) {
            if (i == params.size()) {
                para = para + "" + key + "=" + params.get(key);
            } else {
                para = para + "" + key + "=" + params.get(key) + "&";
                i++;
            }
        }
        url = url + "?" + para;
        try {
            HttpParams httpParameters = new BasicHttpParams();
            HttpDelete httpDelete = new HttpDelete(url);
            httpDelete.setHeader("Accept", "application/json");
            httpDelete.setHeader("Content-type", "application/json");
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
            DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
            HttpResponse httpResponse = httpclient.execute(httpDelete);
            responseCode = httpResponse.getStatusLine().getStatusCode();
            if (responseCode == 401) {
                return null;
            }
            HttpEntity httpEntity = httpResponse.getEntity();
            InputStream content = httpResponse.getEntity().getContent();
            return content;
        } catch (Exception ex) {
            Log.e("doJSON", ex.getMessage());
        }

        return null;
    }

    /**
     *
     * @param url
     * @param params
     * @return
     */
    public static InputStream deleteVideo(String url, HashMap<String, String> params) {
        int timeoutConnection = 5000;
        int timeoutSocket = 10000;
        HttpClient httpclient = new DefaultHttpClient();
        String para = "";

        int i = 1;
        for (String key : params.keySet()) {
            if (i == params.size()) {
                para = para + "" + key + "=" + params.get(key);
            } else {
                para = para + "" + key + "=" + params.get(key) + "&";
                i++;
            }
        }
        url = url + "?" + para;
        try {
            HttpParams httpParameters = new BasicHttpParams();
            HttpDelete httpDelete = new HttpDelete(url);
            httpDelete.setHeader("Accept", "application/json");
            httpDelete.setHeader("Content-type", "application/json");
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
            DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
            HttpResponse httpResponse = httpclient.execute(httpDelete);
            responseCode = httpResponse.getStatusLine().getStatusCode();
            if (responseCode == 401) {
                return null;
            }
            HttpEntity httpEntity = httpResponse.getEntity();
            InputStream content = httpResponse.getEntity().getContent();
            return content;
        } catch (Exception ex) {
            Log.e("doJSON", ex.getMessage());
        }

        return null;
    }

}
