package sample.Order;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.Model.*;
import sample.NetWork.DataController;
import sample.NetWork.InventoryService;
import sample.NetWork.OrderService;
import sample.NetWork.ProductService;

import java.util.ArrayList;

public class CreateOrderController {
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
    InventoryService inventoryService = new InventoryService();
    OrderService orderService = new OrderService();
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
                staffObservableList.setAll(DataController.getDataInstance().getStaff());
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
                customerObservableList.setAll(DataController.getDataInstance().getCustomers());
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
        discountCol.setCellValueFactory(new PropertyValueFactory<>("discount"));
        totalPriceCol.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
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
}
