package sample.NetWork;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import sample.Model.Interface.BestSeller;
import sample.Model.Product;

import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.List;

public class AnalysisService {
    private String BASE_URL = "https://stormy-ridge-84291.herokuapp.com/analysis/";
    // A Gson object that we will use for conversion JSON strings
    // to objects and vice versa
    //
    private Gson gson = new Gson();

    public AnalysisService() {
    }
    public List<BestSeller> getBestSeller(String startDate, String endDate) throws Exception {
        HttpURLConnection connection = HttpConfig.makeRESTRequest(BASE_URL+"bestseller?startDate="+startDate+"&endDate="+endDate, "GET", null);
        HttpConfig.processResponseCode(connection);
        //	get received JSON string
        String receivedData = HttpConfig.getReceivedData(connection);
        System.out.println(receivedData);
        // Convert JSON string into a list of Products, and display them
        Type listType = new TypeToken<List<BestSeller>>(){}.getType();
        List<BestSeller> products = gson.fromJson(receivedData, listType);
        return products;
    }
}
