package com.zarea.restconnection.utilits;

/**
 * Created by zarea on 9/18/15.
 */
public class ResponseCode {
    public static String RESPONSE_400 = "Bad Request";
    public static String RESPONSE_401 = "Unauthorized";
    public static String RESPONSE_404 = "Not Found";
    public static String RESPONSE_500 = "Internal Server Error";
    public static String RESPONSE_Error = "{\"error\": \"Connection error, no internet\"}";
    public static String MAPPER_ERROR = "{\"error\": \"Unable to perform action\"}";
    public static int RESPONSE_ERROR_CODE = -1;
}
