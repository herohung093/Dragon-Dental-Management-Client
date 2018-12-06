package sample.NetWork;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpConfig {
     // Constant for valid content types for these web services
     //

    public static HttpURLConnection makeRESTRequest(String resourceUrl, String method,
                                              String data) throws Exception
    {
        URL url = new URL(resourceUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestProperty("Content-Type", "application/json"+ "; charset=utf-8");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestMethod(method.toUpperCase());


        // Sent data with the request if request method is POST or PUT
        if(method.toUpperCase().equals("POST")
                || method.toUpperCase().equals("PUT"))
        {
            PrintWriter out = new PrintWriter(connection.getOutputStream());
            out.print(data);
            out.flush();
        }

        return connection;
    }

    //	Helper method to  get received data
    public   static String getReceivedData(HttpURLConnection connection) throws Exception
    {
        BufferedReader br = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), "utf-8"));

        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = br.readLine()) != null)
        {
            sb.append(line + "\n");
        }
        br.close();

        return sb.toString();
    }
    // Helper method to act on the repsonse code
    public static  void processResponseCode(HttpURLConnection connection) throws Exception {
        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK)    // error code returned
        {
            String errMsg = connection.getResponseCode() + ": "
                    + connection.getResponseMessage();

            System.out.println(">>> error code and message: " + errMsg);
            throw new Exception(errMsg);
        }
    }
}
