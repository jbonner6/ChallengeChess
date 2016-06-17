package com.jnbonner.familymap.Server;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by James on 3/15/2016.
 */
public class HttpClient {
    String authToken;

    /**
     * Performs a get request to the specified URL
     * @param url   The URL to which the get request should be performed
     * @return      The server response string
     */
    public String getURL(URL url){
        try {
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.addRequestProperty("Authorization", authToken);
            connection.connect();

            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                // Get response body input stream
                // Map<String, List<String>> headers = connection.getHeaderFields();
                InputStream responseBody = connection.getInputStream();

                // Read response body bytes
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length = 0;
                while ((length = responseBody.read(buffer)) != -1){
                    baos.write(buffer, 0, length);
                }

                // convert response body bytes to a string
                String responseBodyData = baos.toString();
                return responseBodyData;
            }
            else{
                return String.valueOf(connection.getResponseCode());
            }

        }
        catch (Exception e){
            Log.e("HttpClient", e.getMessage(), e);
        }
        return "null return";
    }

    /**
     * Performs a post request to the specified URL
     * @param url   The URL to which the post request should be performed
     * @return      The server response string
     */
    public String postURL(URL url, String postData){

        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.connect();

            OutputStream requestBody = connection.getOutputStream();
            requestBody.write(postData.getBytes());
            requestBody.close();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // Get response body input stream
                // Map<String, List<String>> headers = connection.getHeaderFields();
                InputStream responseBody = connection.getInputStream();

                // Read response body bytes
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length = 0;
                while ((length = responseBody.read(buffer)) != -1) {
                    baos.write(buffer, 0, length);
                }

                // convert response body bytes to a string and return it
                return baos.toString();
            }
        }
        catch (Exception e){
            Log.e("HttpClient", e.getMessage(), e);
        }
        return "Invalid Server Host";
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
