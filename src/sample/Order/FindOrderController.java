package sample.Order;

import com.itextpdf.text.DocumentException;
import com.sun.xml.internal.bind.v2.TODO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.Model.Customer;
import sample.Model.Order;
import sample.Model.OrderLine;
import sample.NetWork.DataController;
import sample.NetWork.OrderService;
import sample.PDF.PdfExporting;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FindOrderController {
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
    @FXML
    Button payOrderBT = new Button();
    @FXML
    Button setCreditBT = new Button();
    int numberOfProducts = 0;
    float totalPrice = 0;
    ObservableList<Customer> customerObservableList = FXCollections.observableArrayList();
    ObservableList<OrderLine> orderLineObservableList = FXCollections.observableArrayList();
    ObservableList<Order> orderObservableList = FXCollections.observableArrayList();
    OrderService orderService = new OrderService();
    List<OrderLine> orderLines;


    public FindOrderController() {
    }

    @FXML
    public void initialize(){
        loadCustomerData();
        setupCustomerTable();
        setupOrderTable();
        setupOderLineTable();

        getCustomerFilterTextField();
        Alert alert = new Alert(Alert.AlertType.ERROR);

        orderTable.getSelectionModel().selectedItemProperty().addListener(observable -> {
            try{
                Runnable runnable = ()->{
                    try { if(orderTable.getSelectionModel().getSelectedItem()!=null){
                        orderLines = orderService.getOrderItems(orderTable.getSelectionModel().getSelectedItem().getId());
                        orderLineObservableList.setAll(orderLines);
                        numberOfProducts = setPromotionProductsLB(orderLines);
                        Platform.runLater(new Runnable() {
                            @Override public void run() {
                                promotionProductLB.setText(String.valueOf(numberOfProducts));
                                totalPriceLB.setText(String.valueOf(totalPrice));
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

    private void setupOrderTable() {
        orderIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        orderCustomerCol.setCellValueFactory(new PropertyValueFactory<>("customer"));
        orderCreateAtCol.setCellValueFactory(new PropertyValueFactory<>("createAt"));
        orderUpdateAtCol.setCellValueFactory(new PropertyValueFactory<>("updateAt"));
        orderPaidCol.setCellValueFactory(new PropertyValueFactory<>("paid"));
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
        discountCol.setCellValueFactory(new PropertyValueFactory<>("discount"));
        totalPriceCol.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        orderLineTable.setItems(orderLineObservableList);
        orderLineTable.getColumns().clear();
        orderLineTable.getColumns().addAll(productCol,quantityCol,priceCol,discountCol,totalPriceCol);

    }
    private void loadCustomerData() {
            Runnable runnable = ()->{
                try {
                    customerObservableList.setAll(DataController.getDataInstance().getCustomers());
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
    @FXML private void exportOrder() throws IOException, DocumentException {
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
        System.out.println("C:/Users/"+System.getProperty("user.name")+"/Documents/Orders/"+customerInfo.getName());
        pdfExporting.createPdf(orderTable.getSelectionModel().getSelectedItem(),totalPrice,setPromotionProductsLB(orderLines),orderLines, customerInfo);
        System.out.println("order printed");

    }
    @FXML
    private void payForOrder(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if(orderTable.getSelectionModel().getSelectedItem() != null && Float.valueOf(remainLB.getText()) > 0 && Float.valueOf(totalPriceLB.getText())>0) {
            try {
                if (Float.valueOf(remainLB.getText()) >= Float.valueOf(totalPriceLB.getText())) {
                    alert.setContentText(orderService.payForOrder(orderTable.getSelectionModel().getSelectedItem().getId(), Float.valueOf(totalPriceLB.getText())));
                    alert.show();
                    float remain = Float.valueOf(totalAmountTF.getText())- Float.valueOf(totalPriceLB.getText());
                    remainLB.setText(String.valueOf(remain));
                }else {
                    alert.setContentText(orderService.payForOrder(orderTable.getSelectionModel().getSelectedItem().getId(), Float.valueOf(remainLB.getText())));
                    alert.show();
                    remainLB.setText("0");
                }
            }
            catch(Exception e){
                    e.printStackTrace();
                }
        }
    }
    @FXML
    private void setCredit(){
        remainLB.setText(totalAmountTF.getText());
    }
}
