package sample.Controller;

import com.google.gson.Gson;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import sample.Main;
import sample.Model.Customer;
import sample.Model.Interface.*;
import sample.Model.Inventory;
import sample.NetWork.AnalysisService;
import static java.time.temporal.TemporalAdjusters.firstDayOfYear;
import static java.time.temporal.TemporalAdjusters.lastDayOfYear;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Controller {
    @FXML
    MenuBar mainMenu;

    @FXML
    private TableView<BestSeller> bestSellerTable = new TableView<BestSeller>();
    @FXML
    private TableColumn<BestSeller, String> productCol = new TableColumn<BestSeller, String>();
    @FXML
    private TableColumn<BestSeller, Integer> totalSoldCol = new TableColumn<BestSeller, Integer>();
    AnalysisService analysisService = new AnalysisService();
    ObservableList<BestSeller> bestSellerObservableList = FXCollections.observableArrayList();
    @FXML
    TextField productFilterTF = new TextField();
    @FXML
    Button setDateBT = new Button();
    @FXML
    DatePicker startDate = new DatePicker();
    @FXML
    DatePicker endDate = new DatePicker();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    LocalDate now = LocalDate.now();
    LocalDate firstDay = now.with(firstDayOfYear()); //get start day of the current year
    LocalDate lastDay = now.with(lastDayOfYear());
    @FXML
    TableView<SoldProductQuantity> soldDetailTable = new TableView<>();
    @FXML
    TableColumn<SoldProductQuantity,String> customerCol = new TableColumn<>();
    @FXML
    TableColumn<SoldProductQuantity,Integer> quantityCol = new TableColumn<>();
    @FXML
    TableColumn<SoldProductQuantity,Long> orderIdCol = new TableColumn<>();
    ObservableList<SoldProductQuantity> soldProductQuantityObservableList = FXCollections.observableArrayList();
    @FXML
    TableView<Debter> debterTable = new TableView<>();
    @FXML
    TableColumn<Debter,String> custCol = new TableColumn<>();
    @FXML
    TableColumn<Debter,Float> totalCol = new TableColumn<>();
    @FXML
    TableColumn<Debter,Float> paidCol = new TableColumn<>();
    @FXML
    TableColumn<Debter,Long> orderCol = new TableColumn<>();
    @FXML
    TableColumn<Debter,String> dateCol = new TableColumn<>();
    ObservableList<Debter> debterObservableList = FXCollections.observableArrayList();
    NumberFormat currency = NumberFormat.getNumberInstance();

    @FXML
    Label incomeLB = new Label();
    @FXML
    Label incomeTitleLB = new Label();
    @FXML
    private TableView<TopCustomer> topCustomerTable = new TableView<>();

    @FXML
    private TableColumn<TopCustomer,String> topCustomerNameCol = new TableColumn<>();

    @FXML
    private TableColumn<TopCustomer,Float> topCustomerPaidCol = new TableColumn<>();
    ObservableList<TopCustomer> topCustomerObservableList = FXCollections.observableArrayList();
    ObservableList<Float> chartDataObservableList = FXCollections.observableArrayList();
    ArrayList<Float> chartData = new ArrayList<>();
    @FXML
    private CategoryAxis xAxis = new CategoryAxis();
    @FXML
    final NumberAxis yAxis = new NumberAxis();
    @FXML
    LineChart<String,Number> lineChart = new LineChart<String,Number>(xAxis, yAxis);

    @FXML
    public void createStaff(){

    }

    public Controller() {
        loadBestSellerData();
        loadDebterData();
    }

    private void setupSoldDetailTable() {
        customerCol.setCellValueFactory(new PropertyValueFactory<>("customer"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        orderIdCol.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        soldDetailTable.setItems(soldProductQuantityObservableList);
        soldDetailTable.getColumns().clear();
        soldDetailTable.getColumns().addAll(customerCol,quantityCol,orderIdCol);
    }

    private void setupBestSellerTable() {
        productCol.setCellValueFactory(new PropertyValueFactory<>("productCode"));
        totalSoldCol.setCellValueFactory(new PropertyValueFactory<>("totalSold"));
        bestSellerTable.setItems(bestSellerObservableList);
        bestSellerTable.getColumns().clear();
        bestSellerTable.getColumns().addAll(productCol,totalSoldCol);
    }

    private void loadBestSellerData() {
        Runnable runnable = ()->{
            try {

                bestSellerObservableList.setAll( analysisService
                        .getBestSeller(firstDay.format(formatter),lastDay.format(formatter)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    @FXML
    private void findStock(ActionEvent event) throws IOException {
        VBox inventoryParent = FXMLLoader.load(getClass().getResource("/sample/Inventory/InventoryView.fxml"));
        Scene inventoryScene = new Scene(inventoryParent);

        Stage window = (Stage) mainMenu.getScene().getWindow();
        window.setScene(inventoryScene);
        window.show();
    }
    @FXML
    private void moveToShowProducts(ActionEvent event) throws IOException{
        VBox showProductParent = FXMLLoader.load(getClass().getResource("/sample/Product/ProductView.fxml"));
        Scene showProductScene = new Scene(showProductParent);

        Stage window = (Stage) mainMenu.getScene().getWindow();
        window.setScene(showProductScene);
        window.show();
    }
    @FXML
    private void moveToCreateCustomer() throws IOException {
        VBox createOrderParent = FXMLLoader.load(getClass().getResource("/sample/Customer/CreateCustomerView.fxml"));
        Scene createOrderScene = new Scene(createOrderParent);

        Stage window = (Stage) mainMenu.getScene().getWindow();
        window.setScene(createOrderScene);
        window.show();
    }
    @FXML
    private void moveToCreateOrder() throws IOException {
        VBox createOrderParent = FXMLLoader.load(getClass().getResource("/sample/Order/CreateOrderView.fxml"));
        Scene createOrderScene = new Scene(createOrderParent);

        Stage window = (Stage) mainMenu.getScene().getWindow();
        window.setScene(createOrderScene);
        window.show();
    }

    @FXML
    private void moveToFindOrder() throws IOException {
        VBox findOrderParent = FXMLLoader.load(getClass().getResource("/sample/Order/FindOrder.fxml"));
        Scene findOrderScene = new Scene(findOrderParent);

        Stage window = (Stage) mainMenu.getScene().getWindow();
        window.setScene(findOrderScene);
        window.show();
    }
    @FXML
    private void moveToMixProduct() throws IOException {
        VBox findOrderParent = FXMLLoader.load(getClass().getResource("/sample/Inventory/MixProductView.fxml"));
        Scene findOrderScene = new Scene(findOrderParent);

        Stage window = (Stage) mainMenu.getScene().getWindow();
        window.setScene(findOrderScene);
        window.show();
    }
    @FXML
    private void moveToStockInputHistory() throws IOException {
        VBox findOrderParent = FXMLLoader.load(getClass().getResource("/sample/Inventory/StockInputHistoryView.fxml"));
        Scene findOrderScene = new Scene(findOrderParent);

        Stage window = (Stage) mainMenu.getScene().getWindow();
        window.setScene(findOrderScene);
        window.show();
    }
    @FXML
    private void moveToShowCustomer() throws IOException {
        VBox findOrderParent = FXMLLoader.load(getClass().getResource("/sample/Customer/ShowCustomerView.fxml"));
        Scene findOrderScene = new Scene(findOrderParent);

        Stage window = (Stage) mainMenu.getScene().getWindow();
        window.setScene(findOrderScene);
        window.show();
    }
    @FXML
    void initialize() throws InterruptedException {
        setupBestSellerTable();
        startDate.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate object) {
                if (object != null) {
                    return formatter.format(object);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, formatter);
                } else {
                    return null;
                }
            }
        });
        endDate.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate object) {
                if (object != null) {
                    return formatter.format(object);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, formatter);
                } else {
                    return null;
                }
            }
        });
        getProductFilterTextField();
        setupSoldDetailTable();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        bestSellerTable.getSelectionModel().selectedItemProperty().addListener(observable ->{
            try{
                Runnable runnable = ()->{
                    try { if(bestSellerTable.getSelectionModel().getSelectedItem()!=null){
                        if(startDate.getValue() != null){
                            soldProductQuantityObservableList.setAll(analysisService.getSoldDetail(bestSellerTable.getSelectionModel()
                                    .getSelectedItem().getProductCode(),startDate.getValue()
                                    .format(formatter),endDate.getValue().format(formatter)));
                        }else {
                            soldProductQuantityObservableList.setAll(analysisService.getSoldDetail(bestSellerTable.getSelectionModel()
                                    .getSelectedItem().getProductCode(),firstDay.format(formatter),lastDay.format(formatter)));
                        }

                    }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                };
                Thread thread = new Thread(runnable);
                thread.start();
            }
            catch (Exception e){
                alert.setContentText("Error: " + e.getMessage());
                alert.showAndWait();
            }
        } );
        setupTopCustomerTable();
        loadTopCustomerData();
        setupDebterTable();
        loadDebterData();

        loadChartData();

        incomeTitleLB.setText("Income From "+firstDay.format(formatter)+" To "+lastDay.format(formatter));
        Runnable runnable = ()->{
            try {
                Platform.runLater(new Runnable() {
                    @Override public void run() {
                        try {
                            incomeLB.setText(currency.format(analysisService.getIncomeByTime(firstDay.format(formatter),lastDay.format(formatter))));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        Thread.sleep(5000);
        setupChart();
    }

    private void loadChartData() {
        Runnable runnable = ()->{
            try {
                chartDataObservableList.setAll(analysisService.getChartData(String.valueOf(firstDay.getYear())));
                //chartData.addAll(analysisService.getChartData(String.valueOf(firstDay.getYear())));

            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();

    }

    private void setupChart() {

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Income");

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Month");
        XYChart.Series<String,Number> aSeries = new XYChart.Series();
        for(int i=0; i< chartDataObservableList.size();i++){
            aSeries.getData().add(new XYChart.Data( String.valueOf(i+1), chartDataObservableList.get(i)));

        }
        lineChart.getData().add(aSeries);


        System.out.println(aSeries.getData().get(5));
    }

    private void loadTopCustomerData() {
        Runnable runnable = ()->{
            try {
                topCustomerObservableList.setAll(analysisService.getTopCustomer(firstDay.format(formatter),lastDay.format(formatter)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    private void setupTopCustomerTable() {
        topCustomerNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        topCustomerPaidCol.setCellValueFactory(new PropertyValueFactory<>("totalPaid"));
        topCustomerPaidCol.setCellFactory(tc -> new CurrencyCell());
        topCustomerTable.setItems(topCustomerObservableList);
        topCustomerTable.getColumns().clear();
        topCustomerTable.getColumns().addAll(topCustomerNameCol,topCustomerPaidCol);
    }

    private void loadDebterData() {
        Runnable runnable = ()->{
            try {
                List<Debter> debters = new ArrayList<>();
                debters = analysisService
                        .getAllDebter(firstDay.format(formatter),lastDay.format(formatter));

                debterObservableList.setAll(debters);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    private void setupDebterTable() {
        custCol.setCellValueFactory(new PropertyValueFactory<Debter,String>("customer"));
        totalCol.setCellValueFactory(new PropertyValueFactory<Debter,Float>("total"));
        paidCol.setCellValueFactory(new PropertyValueFactory<Debter,Float>("paid"));
        orderCol.setCellValueFactory(new PropertyValueFactory<Debter,Long>("orderId"));
        dateCol.setCellValueFactory(new PropertyValueFactory<Debter,String>("date"));
        debterTable.setItems(debterObservableList);
        debterTable.getColumns().clear();
        debterTable.getColumns().addAll(custCol,totalCol,paidCol,orderCol,dateCol);
    }

    @FXML
    void setDate(){
        Runnable runnable = ()->{
            try {
                bestSellerObservableList.setAll( analysisService.getBestSeller(startDate.getValue()
                        .format(formatter),endDate.getValue().format(formatter)));
                topCustomerObservableList.setAll( analysisService.getTopCustomer(startDate.getValue()
                        .format(formatter),endDate.getValue().format(formatter)));
                Platform.runLater(new Runnable() {
                    @Override public void run() {
                        try {
                            incomeLB.setText(currency.format(analysisService.getIncomeByTime(startDate.getValue().format(formatter),endDate.getValue().format(formatter))));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }
    private void getProductFilterTextField() {
        FilteredList<BestSeller> filteredList =
                new FilteredList<>(bestSellerTable.getItems(), i -> true);
        SortedList<BestSeller> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(bestSellerTable.comparatorProperty());
        bestSellerTable.setItems(sortedList);

        productFilterTF.textProperty().addListener(((observable, oldValue, newValue) ->
        {
            filteredList.setPredicate(inventoryView ->
            {
                if (newValue == null || newValue.isEmpty()){
                    return true;
                }
                String filterString = newValue.toUpperCase();
                if(inventoryView.getProductCode().toUpperCase().contains(filterString))
                    return true;
                else return false;
            });
        }));
    }
}
