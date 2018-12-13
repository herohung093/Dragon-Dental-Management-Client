package sample.NetWork;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import sample.Model.Interface.BestSeller;
import sample.Model.Interface.Debter;
import sample.Model.Interface.SoldProductQuantity;
import sample.Model.Product;

import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.List;

public class AnalysisService {
    private String BASE_URL = UrlConfig.APP_BASE_URL+"analysis/";
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
        // Convert JSON string into a list of Products, and display them
        Type listType = new TypeToken<List<BestSeller>>(){}.getType();
        List<BestSeller> products = gson.fromJson(receivedData, listType);
        return products;
    }

    public List<SoldProductQuantity> getSoldDetail(String product,String startDate, String endDate) throws Exception {
        HttpURLConnection connection = HttpConfig.makeRESTRequest(BASE_URL+"soldproduct/"+product+"?startDate="+startDate+"&endDate="+endDate, "GET", null);
        HttpConfig.processResponseCode(connection);
        //	get received JSON string
        String receivedData = HttpConfig.getReceivedData(connection);
        // Convert JSON string into a list of Products, and display them
        Type listType = new TypeToken<List<SoldProductQuantity>>(){}.getType();
        List<SoldProductQuantity> soldDetails = gson.fromJson(receivedData, listType);
        return soldDetails;
    }

    public List<Debter> getAllDebter(String startDate, String endDate) throws Exception {
        HttpURLConnection connection = HttpConfig.makeRESTRequest(BASE_URL+"dept"+"?startDate="+startDate+"&endDate="+endDate, "GET", null);
        HttpConfig.processResponseCode(connection);
        //	get received JSON string
        String receivedData = HttpConfig.getReceivedData(connection);
        System.out.println(receivedData);
        // Convert JSON string into a list of Products, and display them
        Type listType = new TypeToken<List<Debter>>(){}.getType();
        List<Debter> debters = gson.fromJson(receivedData, listType);

        return debters;
    }
}