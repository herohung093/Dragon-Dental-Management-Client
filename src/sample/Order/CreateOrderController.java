package sample.Order;

import com.itextpdf.text.DocumentException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sample.Model.*;
import sample.Model.Interface.CurrencyCell;
import sample.NetWork.*;
import sample.PDF.PdfExporting;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CreateOrderController {
    @FXML
    MenuBar mainMenu;
    @FXML
    TableView<Customer> customerTable = new TableView<Customer>();
    @FXML
    TableColumn<Customer,String> customerNameCol = new TableColumn<Customer,String>();
    @FXML
    TableView<Inventory> productTable = new TableView<Inventory>();
    @FXML
    TableColumn<Inventory,String> productCodeCol = new TableColumn<Inventory,String>();
    @FXML
    TableColumn<Inventory,Integer> productPriceCol = new TableColumn<Inventory,Integer>();
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
    ObservableList<Inventory> productObservableList = FXCollections.observableArrayList();
    ObservableList<Staff> staffObservableList = FXCollections.observableArrayList();
    ObservableList<Customer> customerObservableList = FXCollections.observableArrayList();
    ObservableList<OrderLine> orderLineObservableList = FXCollections.observableArrayList();
    ArrayList<OrderLine> orderLines = new ArrayList<>();
    @FXML
    TextField customerFilterTF = new TextField();
    @FXML
    TextField productFilterTF = new TextField();
    @FXML
    TextField quantityTF = new TextField();
    @FXML
    TextField priceTF = new TextField();
    @FXML
    TextField paidTF = new TextField();
    @FXML
    Button addToOderBT = new Button();
    @FXML
    Button createOrderBT = new Button();
    @FXML
    TextField discountTF = new TextField();
    @FXML
    TextArea noteTA = new TextArea();
    @FXML
    CheckBox instalmentCB = new CheckBox();
    @FXML
    Button deletedItemBT = new Button();
    @FXML
    ChoiceBox<Staff> staffChoiceBox = new ChoiceBox<>();
    @FXML
    Button printOrderBT = new Button();
    InventoryService inventoryService = new InventoryService();
    OrderService orderService = new OrderService();
    StaffService staffService = new StaffService();
    CustomerService customerService = new CustomerService();
    public CreateOrderController() {
    }
    @FXML
    public void initialize(){
        loadProductData();
        loadStaffData();
        loadCustomerData();

        setupCustomerTable();
        setupProductTable();
        setupOderLineTable();
        setupStaffCB();

        getCustomerFilterTextField();
        getProductFilterTextField();
        orderLineObservableList.addAll(orderLines);
        deletedItemBT.setDisable(true);
        orderLineTable.getSelectionModel().selectedItemProperty().addListener(observable -> {
            deletedItemBT.setDisable(false);
        });

    }


    private void loadProductData(){
        Runnable runnable = ()->{
            try {

                productObservableList.setAll(inventoryService.getAll());
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
    private void loadStaffData(){
        Runnable runnable = ()->{
            try {
                staffObservableList.setAll(staffService.getAll());
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
    private void loadCustomerData(){
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
    private void setupCustomerTable(){
        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerTable.setItems(customerObservableList);
        customerTable.getColumns().clear();
        customerTable.getColumns().add(customerNameCol);

    }
    private void setupProductTable(){
        productCodeCol.setCellValueFactory( new PropertyValueFactory<>("code"));
        productPriceCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productTable.setItems(productObservableList);
        productTable.getColumns().clear();
        productTable.getColumns().addAll(productCodeCol,productPriceCol);
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

    private void getProductFilterTextField() {
        FilteredList<Inventory> filteredList =
                new FilteredList<>(productTable.getItems(), i -> true);
        SortedList<Inventory> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(productTable.comparatorProperty());
        productTable.setItems(sortedList);

        productFilterTF.textProperty().addListener(((observable, oldValue, newValue) ->
        {
            filteredList.setPredicate(inventoryView ->
            {
                if (newValue == null || newValue.isEmpty()){
                    return true;
                }
                String filterString = newValue.toUpperCase();
                if(inventoryView.getCode().toUpperCase().contains(filterString))
                    return true;
                else return false;
            });
        }));
    }
    @FXML
    public void addToOrder(){
        if(productTable.getSelectionModel().getSelectedItem() != null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            try {
                if(quantityTF.equals("") || quantityTF.getText().equals(""))
                    throw new Exception("please fill up Quantity and Price");
            }
            catch (Exception e){
                alert.setContentText("Error: " + e.getMessage());
                alert.showAndWait();
            }
            OrderLine orderLine = new OrderLine(productTable.getSelectionModel().getSelectedItem().getCode(),
                    Integer.valueOf(quantityTF.getText()),Float.valueOf(priceTF.getText()), Integer.valueOf(discountTF.getText()));
            orderLineObservableList.add(orderLine);
            orderLines.add(orderLine);
            //quantityTF.clear();
            //priceTF.setText("0");
            //discountTF.setText("0");

        }
    }

    @FXML
    public void createOrder(){
        if(customerTable.getSelectionModel().getSelectedItem() != null && orderLines.size()>0){

            Alert alert = new Alert(Alert.AlertType.ERROR);
            try {

                Order order = new Order(customerTable.getSelectionModel().getSelectedItem().getName()
                        , staffChoiceBox.getSelectionModel().getSelectedItem().getName());
                order.setOrderLines(orderLines);
                order.setPaid(Float.valueOf(paidTF.getText()));
                order.setNote(noteTA.getText());
                order.setInstalment(instalmentCB.isSelected());

                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText(orderService.createOrder(order));
                alert.show();
            } catch (Exception e) {

                alert.setContentText("Error: " + e.getMessage());
                alert.showAndWait();
            }
            loadProductData();
        }
    }
    public void setupStaffCB(){
        staffChoiceBox.setItems(staffObservableList);
        staffChoiceBox.getSelectionModel().selectFirst();
    }
    public void deleteItem(){
        OrderLine orderLine = orderLineTable.getSelectionModel().getSelectedItem();
        orderLineObservableList.remove(orderLine);
        orderLines.remove(orderLine);
    }
    @FXML
    private void exportOrder() throws IOException, DocumentException {

        PdfExporting pdfExporting = new PdfExporting("C:/Users/"+System.getProperty("user.name")+"/Documents/Orders/"+customerTable.getSelectionModel().getSelectedItem().getName());
        File dir = new File("C:/Users/"+System.getProperty("user.name")+"/Documents/Orders/"+customerTable.getSelectionModel().getSelectedItem().getName()) ;
        if(!dir.exists())
            dir.mkdirs();
        Order order = new Order(customerTable.getSelectionModel().getSelectedItem().getName()
                , staffChoiceBox.getSelectionModel().getSelectedItem().getName());
        order.setOrderLines(orderLines);
        order.setPaid(Float.valueOf(paidTF.getText()));
        order.setNote(noteTA.getText());
        order.setInstalment(instalmentCB.isSelected());
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        order.setCreateAt(now.format(formatter));
        pdfExporting.createPdf(order,getTotalPrice(),getTotalPromotedProduct(),orderLines, customerTable.getSelectionModel().getSelectedItem(),null);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Order is saved at directory "+dir.getPath());
        alert.show();
    }

    private int getTotalPromotedProduct(){
        int total=0;
        for(OrderLine orderLine: orderLines){
            if (orderLine.getPrice()==0)
                total = total + orderLine.getQuantity();
        }
        return total;
    }

    private float getTotalPrice(){
        float total =0;
        for(OrderLine orderLine: orderLines){
            total = total + orderLine.getTotalPrice();
        }
        return total;
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
    private void moveToFindOrder() throws IOException {
        VBox findOrderParent = FXMLLoader.load(getClass().getResource("/sample/Order/FindOrder.fxml"));
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
    private void moveToMixProduct() throws IOException {
        VBox findOrderParent = FXMLLoader.load(getClass().getResource("/sample/Inventory/MixProductView.fxml"));
        Scene findOrderScene = new Scene(findOrderParent);

        Stage window = (Stage) mainMenu.getScene().getWindow();
        window.setScene(findOrderScene);
        window.show();
    }
}
