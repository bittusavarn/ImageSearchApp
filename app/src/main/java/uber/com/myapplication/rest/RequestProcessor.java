package uber.com.myapplication.rest;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RequestProcessor{
    private final int CONNECTION_TIMEOUT = 60000;
    private final int READ_TIMEOUT = 60000;

    private String TAG = RequestProcessor.class.getName();

    public String getResponse(String url) throws Exception {
        return processRequest(url, "GET");
    }

    private String processRequest(String url, String method) throws Exception {
        HttpURLConnection con = null;
        String resp = null;
        BufferedReader bufferedReader = null;
        try {
            con = (HttpURLConnection) new URL(url).openConnection();
            con.setDefaultUseCaches(false);
            con.setConnectTimeout(CONNECTION_TIMEOUT);
            con.setReadTimeout(READ_TIMEOUT);
            con.setRequestMethod(method);
            con.connect();
            int resposeCode = con.getResponseCode();

            if (resposeCode == HttpURLConnection.HTTP_OK) {
                bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream(), "iso-8859-1"), 8);
                String inputLine = null;
                StringBuffer response = new StringBuffer();
                while (((inputLine = bufferedReader.readLine()) != null)) {
                    response.append(inputLine);
                }
                resp = response.toString();
                System.out.println(TAG + " Success getting response ");
            } else {
                System.out.println(TAG + " Http Response Code " + resposeCode);
                throw new Exception("Error in reading Data");
            }

        } catch (MalformedURLException ex) {
            System.out.println(TAG + "Exception in processRequest " + ex.getMessage());
            throw new Exception("Incorrect Url");
        } catch (IOException ex) {
            System.out.println(TAG + "Exception in processRequest " + ex.getMessage());
            throw new Exception("Could not get Data from Server");
        } finally {
            con.disconnect();
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        }
        return resp;
    }


}
