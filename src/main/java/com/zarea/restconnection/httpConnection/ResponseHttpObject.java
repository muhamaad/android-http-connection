package com.zarea.restconnection.httpConnection;


import com.zarea.restconnection.utilits.ResponseCode;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

/**
 * Created by zarea on 12/19/15.
 */
public class ResponseHttpObject {

    private int responseCode;
    private InputStream response = null;
    private String result;

    public ResponseHttpObject(int responseCode, InputStream response) {
        this.response = response;
        this.responseCode = responseCode;

        InputStreamToString();
    }

    public ResponseHttpObject() {
        this.responseCode = -1;
        try {
            this.response = new ByteArrayInputStream(ResponseCode.RESPONSE_Error.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
        }

        InputStreamToString();
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public InputStream getResponse() {
        return response;
    }

    public void setResponse(InputStream response) {
        this.response = response;
    }

    private void InputStreamToString() {
        StringWriter writer = new StringWriter();
        try {
            IOUtils.copy(this.response, writer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            this.result = "Connection error";
        }
        this.result = writer.toString();

        /*
        try {
            BufferedReader BuffRead = new BufferedReader(new InputStreamReader(
                    this.response, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;

            while ((line = BuffRead.readLine()) != null) {
                sb.append(line + "\n");
            }

            this.response.close();
            this.result = sb.toString();

        } catch (Exception ex) {
            this.result = "Connection error";
        }
        */
    }
}