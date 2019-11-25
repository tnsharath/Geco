package com.wintile.geco.Utils;

/**
 * Created by Sharath Kumar on 10/19/2017.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import android.util.Log;


public class JSONParser {

    private static final String LOG_TAG = "JSONParser";
    /**
     * Returns new URL object from the given string URL.
     */
    public URL createUrl(String stringUrl) {
        URL url;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "Error with creating URL", exception);
            return null;
        }
        Log.e("url", String.valueOf(url));
        return url;
    }
    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    public String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        Log.e(LOG_TAG, "pass1");
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            Log.e(LOG_TAG, "pass2");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();
            inputStream = urlConnection.getInputStream();
            jsonResponse = readFromStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "pass3" + e );
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                inputStream.close();
            }
        }
        Log.e(LOG_TAG, "pass4 " + jsonResponse);
        return jsonResponse;
    }
    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    public String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
}
