package sample.Order;

import com.itextpdf.text.DocumentException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import sample.Model.Customer;
import sample.Model.Interface.CurrencyCell;
import sample.Model.Order;
import sample.Model.OrderLine;
import sample.NetWork.AnalysisService;
import sample.NetWork.CustomerService;
import sample.NetWork.DataController;
import sample.NetWork.OrderService;
import sample.PDF.PdfExporting;
import sample.PDF.PdfMonthlyExporting;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

public class FindOrderController {
    @FXML
    MenuBar mainMenu;
    @FXML
    TableView<Customer> customerTable = new TableView<Customer>();
    @FXML
    TableColumn<Customer,String> customerNameCol = new TableColumn<Customer,String>();
    @FXML
    TableView<OrderLine> orderLineTable = new TableView<OrderLine>();
    @FXML
    TableColumn<OrderLine,String> productCol = new TableColumn<OrderLine, String>();
    @FXML
    TableColumn<OrderLine,Integer> quantityCol = new TableColumn<OrderLine, Integer>();
    @FXML
    TableColumn<OrderLine,Float> priceCol = new TableColumn<OrderLine, Float>();
    @FXML
    TableColumn<OrderLine,Integer> discountCol = new TableColumn<OrderLine,Integer>();
    @FXML
    TableColumn<OrderLine,Float> totalPriceCol = new TableColumn<OrderLine, Float>();
    @FXML
    TableView<Order> orderTable = new TableView<Order>();
    @FXML
    TableColumn<Order,Long> orderIdCol = new TableColumn<>();
    @FXML
    TableColumn<Order,String> orderCustomerCol = new TableColumn<>();
    @FXML
    TableColumn<Order, String> orderCreateAtCol = new TableColumn<>();
    @FXML
    TableColumn<Order, String> orderUpdateAtCol = new TableColumn<>();
    @FXML
    TableColumn<Order,Float> orderPaidCol = new TableColumn<>();
    @FXML
    TableColumn<Order,String> orderNoteCol = new TableColumn<>();
    @FXML
    TableColumn<Order,String> orderStaffCol = new TableColumn<>();
    @FXML
    TableColumn<Order,Boolean> orderInstalmentCol = new TableColumn<>();
    @FXML
    TextField customerFilterTF = new TextField();
    @FXML
    Button findByCustomerBT = new Button();
    @FXML
    Button showAllOrderBT = new Button();
    @FXML
    Button showUnPaidOrderBT = new Button();
    @FXML
    Button findBetween = new Button();
    @FXML
    Label totalPriceLB = new Label();
    @FXML
    Label promotionProductLB = new Label();
    @FXML
    DatePicker startDate = new DatePicker();
    @FXML
    DatePicker endDate = new DatePicker();
    @FXML Button updateOrderBT = new Button();
    @FXML
    Button exportOrderBT = new Button();
    @FXML
    TextField totalAmountTF = new TextField();
    @FXML
    Label remainLB = new Label();
    float remainCached =0;
    @FXML
    Button payOrderBT = new Button();
    @FXML
    Button setCreditBT = new Button();
    @FXML
    private TableView<Order> selectedOrderTable = new TableView<>();

    @FXML
    private TableColumn<Order,Long> selectedIdCol = new TableColumn<>();

    @FXML
    private TableColumn<Order,String> customerCol = new TableColumn<>();

    @FXML
    private TableColumn<Order,String> createAtCol= new TableColumn<>();

    @FXML
    private TableColumn<Order,Float> paidCol= new TableColumn<>();

    @FXML
    private Button removeOrderBT = new Button();

    @FXML
    private Button generateInvoiceBT = new Button();
    @FXML
    private Button addOrderBT =  new Button();

    ObservableList<Order> selectedOrderObservableList = FXCollections.observableArrayList();
    ArrayList<Order> selectedOrders = new ArrayList<>();
    ArrayList<Float> totalPrices = new ArrayList<>();
    int numberOfProducts = 0;
    float totalPrice = 0;
    float totalPriceCached = 0;
    ObservableList<Customer> customerObservableList = FXCollections.observableArrayList();
    ObservableList<OrderLine> orderLineObservableList = FXCollections.observableArrayList();
    ObservableList<Order> orderObservableList = FXCollections.observableArrayList();
    OrderService orderService = new OrderService();
    AnalysisService analysisService = new AnalysisService();
    CustomerService customerService = new CustomerService();
    List<OrderLine> orderLines;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    NumberFormat currency = NumberFormat.getCurrencyInstance(Locale.US);
    public FindOrderController() {
    }

    @FXML
    public void initialize(){
        loadCustomerData();
        setupCustomerTable();
        setupOrderTable();
        setupOderLineTable();
        setupSelectedOrderTable();
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
        getCustomerFilterTextField();
        //selectedOrders.addAll(selectedOrders);
        Alert alert = new Alert(Alert.AlertType.ERROR);
        addOrderBT.setDisable(true);
        orderTable.getSelectionModel().selectedItemProperty().addListener(observable -> {
            addOrderBT.setDisable(false);
            try{
                Runnable runnable = ()->{
                    try { if(orderTable.getSelectionModel().getSelectedItem()!=null){
                        orderLines = orderService.getOrderItems(orderTable.getSelectionModel().getSelectedItem().getId());
                        orderLineObservableList.setAll(orderLines);
                        numberOfProducts = setPromotionProductsLB(orderLines);
                        Platform.runLater(new Runnable() {
                            @Override public void run() {
                                promotionProductLB.setText(String.valueOf(numberOfProducts));
                                totalPriceLB.setText(currency.format(totalPrice));
                                totalPriceCached = totalPrice;
                                totalPrice = 0;
                            }
                        });
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

        });
    }

    private void setupSelectedOrderTable() {
        selectedIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerCol.setCellValueFactory(new PropertyValueFactory<>("customer"));
        createAtCol.setCellValueFactory(new PropertyValueFactory<>("createAt"));
        paidCol.setCellValueFactory(new PropertyValueFactory<>("paid"));
        paidCol.setCellFactory(tc -> new CurrencyCell());

        selectedOrderTable.setItems(selectedOrderObservableList);
        selectedOrderTable.getColumns().clear();
        selectedOrderTable.getColumns().addAll(selectedIdCol,customerCol,createAtCol,paidCol);
    }

    private void setupOrderTable() {
        orderIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        orderCustomerCol.setCellValueFactory(new PropertyValueFactory<>("customer"));
        orderCreateAtCol.setCellValueFactory(new PropertyValueFactory<>("createAt"));
        orderUpdateAtCol.setCellValueFactory(new PropertyValueFactory<>("updateAt"));
        orderPaidCol.setCellValueFactory(new PropertyValueFactory<>("paid"));
        orderPaidCol.setCellFactory(tc -> new CurrencyCell());
        orderNoteCol.setCellValueFactory(new PropertyValueFactory<>("note"));
        orderInstalmentCol.setCellValueFactory(new PropertyValueFactory<>("isInstalment"));
        orderStaffCol.setCellValueFactory(new PropertyValueFactory<>("staff"));
        orderTable.setItems(orderObservableList);
        orderTable.getColumns().clear();
        orderTable.getColumns().addAll(orderIdCol,orderCustomerCol,orderCreateAtCol,orderUpdateAtCol,
                orderPaidCol,orderNoteCol,orderInstalmentCol,orderStaffCol);
    }

    private void getCustomerFilterTextField() {
        FilteredList<Customer> filteredList =
                new FilteredList<>(customerTable.getItems(), i -> true);
        SortedList<Customer> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(customerTable.comparatorProperty());
        customerTable.setItems(sortedList);

        customerFilterTF.textProperty().addListener(((observable, oldValue, newValue) ->
        {
            filteredList.setPredicate(inventoryView ->
            {
                if (newValue == null || newValue.isEmpty()){
                    return true;
                }
                String filterString = newValue.toUpperCase();
                if(inventoryView.getName().toUpperCase().contains(filterString))
                    return true;
                else return false;
            });
        }));
    }

    private void setupCustomerTable() {
        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerTable.setItems(customerObservableList);
        customerTable.getColumns().clear();
        customerTable.getColumns().add(customerNameCol);
    }
    private void setupOderLineTable() {
        productCol.setCellValueFactory(new PropertyValueFactory<>("product"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceCol.setCellFactory(tc -> new CurrencyCell());
        discountCol.setCellValueFactory(new PropertyValueFactory<>("discount"));
        totalPriceCol.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        totalPriceCol.setCellFactory(tc -> new CurrencyCell());
        orderLineTable.setItems(orderLineObservableList);
        orderLineTable.getColumns().clear();
        orderLineTable.getColumns().addAll(productCol,quantityCol,priceCol,discountCol,totalPriceCol);

    }
    private void loadCustomerData() {
            Runnable runnable = ()->{
                try {
                    customerObservableList.setAll(customerService.getAll());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };
            Thread thread = new Thread(runnable);
            thread.start();
    }
    @FXML
    private void findOrderByCustomerBT(){
        if (customerTable.getSelectionModel().getSelectedItem() !=null){
            Runnable runnable = ()->{
                try {
                    orderObservableList.setAll(orderService.getOrderByCustomer(customerTable.getSelectionModel().getSelectedItem().getId()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };
            Thread thread = new Thread(runnable);
            thread.start();
        }
    }
    @FXML
    private void showAllOrders(){
        Runnable runnable = ()->{
            try {
                orderObservableList.setAll(orderService.getAll());
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
    private int setPromotionProductsLB(List<OrderLine> orderLines){
        ArrayList<OrderLine> list = new ArrayList<>();
        int numberOfProducts = 0;
        if(orderLines!=null){
            list.addAll(orderLines);
            for (OrderLine orderLine: list){
                if(orderLine.getPrice() == 0 )
                    numberOfProducts = numberOfProducts +orderLine.getQuantity();
                totalPrice = totalPrice+orderLine.getTotalPrice();
            }
            if(orderLines.size()==0)
                totalPrice = 0;
        }

        return numberOfProducts;
    }
    @FXML
    private void getUnpaidOrder(){
        Runnable runnable = ()->{
            try {

                orderObservableList.setAll(orderService.getUnPaidOrder());
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
    @FXML
    private void findOrderBetweenDate(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Runnable runnable = ()->{
            try {
                orderObservableList.setAll(orderService.getBetweenDate(startDate.getValue().format(formatter),endDate.getValue().format(formatter)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    @FXML
    private void exportOrder() throws IOException, DocumentException {
        List<Customer> customers = new ArrayList<>();
        customers.addAll(DataController.getDataInstance().getCustomers());
        Customer customerInfo = new Customer();
        for(Customer customer: customers){
            if(customer.getName().equalsIgnoreCase(orderTable.getSelectionModel().getSelectedItem().getCustomer())){
                customerInfo = customer;
            }
        }
        PdfExporting pdfExporting = new PdfExporting("C:/Users/"+System.getProperty("user.name")+"/Documents/Orders/"+customerInfo.getName());
        File dir = new File("C:/Users/"+System.getProperty("user.name")+"/Documents/Orders/"+customerInfo.getName()) ;
        if(!dir.exists())
            dir.mkdirs();
        pdfExporting.createPdf(orderTable.getSelectionModel().getSelectedItem(), totalPriceCached,setPromotionProductsLB(orderLines),orderLines, customerInfo);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Order is saved at directory "+dir.getPath());
        alert.show();
    }
    @FXML
    private void generateMonthlyInvoice() throws IOException, DocumentException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        PdfMonthlyExporting pdfMonthlyExporting = new PdfMonthlyExporting("C:/Users/"+System.getProperty("user.name")+"/Documents/Orders/"+customerTable.getSelectionModel().getSelectedItem().getName());
        File dir = new File("C:/Users/"+System.getProperty("user.name")+"/Documents/Orders/"+customerTable.getSelectionModel().getSelectedItem().getName()) ;
        if(!dir.exists())
            dir.mkdirs();
        Runnable runnable = ()->{
            try {
                if(selectedOrderTable.getSelectionModel().getSelectedItem()!=null){
                    String month = selectedOrderTable.getSelectionModel().getSelectedItem().getCreateAt().substring(3);
                    pdfMonthlyExporting.createPdf(selectedOrders,totalPrices, analysisService.getDebtByCustomerId(customerTable.getSelectionModel().getSelectedItem().getId()),customerTable.getSelectionModel().getSelectedItem(),month);
                    Platform.runLater(new Runnable() {
                        @Override public void run() {
                            alert.setContentText("Order is saved at directory "+dir.getPath());
                            alert.show();
                        }
                    });
                }


            } catch (Exception e) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        alert.setContentText(e.getMessage());
                        alert.showAndWait();
                    }
                });

            }
        };
        Thread thread = new Thread(runnable);
        thread.start();


    }
    @FXML
    private void removeOrderFromList(){
        if(selectedOrderTable.getSelectionModel().getSelectedItem()!=null){
            selectedOrders.remove(selectedOrderTable.getSelectionModel().getSelectedItem());
            totalPrices.remove(selectedOrderTable.getSelectionModel().getSelectedIndex());
            selectedOrderObservableList.setAll(selectedOrders);
        }

    }
    @FXML
    private void addOrderToList(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        for(Order order: selectedOrders){
            try{
                if(order.getId() == orderTable.getSelectionModel().getSelectedItem().getId())
                    throw new Exception("Order ID already exists");
            }catch (Exception e){
                alert.setContentText(e.getMessage());
                alert.show();
                return;
            }
        }
        selectedOrders.add(orderTable.getSelectionModel().getSelectedItem());
        selectedOrderObservableList.setAll(selectedOrders);
        totalPrices.add(totalPriceCached);

    }
    private float getTotalPrices(){
        float total = 0;
        for(Float price: totalPrices){
            total = total + price;
        }
        return total;
    }
    @FXML
    private void moveToUpdateOrder(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/sample/Order/UpdateOrderView.fxml"));
        try {
            Parent root = (Parent) fxmlLoader.load();
            UpdateOrderController updateOrderController = fxmlLoader.getController();
            updateOrderController.setOrder(orderTable.getSelectionModel().getSelectedItem());
            Stage stage = new Stage();
            stage.setTitle("Update Order");

            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @FXML
    private void payForOrder(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if(orderTable.getSelectionModel().getSelectedItem() != null && remainCached > 0 && totalPriceCached>0 && totalPriceCached!= orderTable.getSelectionModel().getSelectedItem().getPaid()) {
            try {
                if (remainCached >= totalPriceCached) {
                    if(totalPriceCached > orderTable.getSelectionModel().getSelectedItem().getPaid()){
                        alert.setContentText(orderService.payForOrder(orderTable.getSelectionModel().getSelectedItem().getId(), totalPriceCached - orderTable.getSelectionModel().getSelectedItem().getPaid()));
                        remainCached = remainCached - (totalPriceCached - orderTable.getSelectionModel().getSelectedItem().getPaid());
                        remainLB.setText(currency.format(remainCached));
                        alert.show();
                    } else {
                        alert.setContentText(orderService.payForOrder(orderTable.getSelectionModel().getSelectedItem().getId(), totalPriceCached));
                        alert.show();
                        float remain = Float.valueOf(totalAmountTF.getText())- totalPriceCached;
                        remainCached = remain;
                        remainLB.setText(currency.format(remain));
                    }

                }else {
                    alert.setContentText(orderService.payForOrder(orderTable.getSelectionModel().getSelectedItem().getId(), remainCached));
                    alert.show();
                    remainLB.setText("0");
                    remainCached =0;
                }
                if (customerTable.getSelectionModel().getSelectedItem() !=null){
                    Runnable runnable = ()->{
                        try {
                            orderObservableList.setAll(orderService.getOrderByCustomer(customerTable.getSelectionModel().getSelectedItem().getId()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    };
                    Thread thread = new Thread(runnable);
                    thread.start();
                }
            }
            catch(Exception e){
                    e.printStackTrace();
                }
        }
    }
    @FXML
    private void setCredit(){
        remainCached = Float.valueOf(totalAmountTF.getText());
        remainLB.setText(currency.format(remainCached));
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
}
