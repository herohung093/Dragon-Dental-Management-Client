package sample.NetWork;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import sample.Model.Product;
import sample.Model.Staff;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class StaffService {
    private String BASE_URL = "https://stormy-ridge-84291.herokuapp.com/staff/";

    // A Gson object that we will use for conversion JSON strings
    // to objects and vice versa
    //
    private Gson gson = new Gson();
    // Helper method to make a REST request
    public StaffService(){

    }
    public List<Staff> getAll() throws Exception
    {
        HttpURLConnection connection = HttpConfig.makeRESTRequest(BASE_URL, "GET", null);

        HttpConfig.processResponseCode(connection);

        //	get received JSON string
        String receivedData = HttpConfig.getReceivedData(connection);

        // Convert JSON string into a list of Products, and display them
        Type listType = new TypeToken<List<Staff>>(){}.getType();
        List<Staff> staff = gson.fromJson(receivedData, listType);

        return staff;
    }
}
