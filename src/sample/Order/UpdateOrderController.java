package sample.Order;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.Model.Interface.CurrencyCell;
import sample.Model.Inventory;
import sample.Model.Order;
import sample.Model.OrderLine;
import sample.NetWork.DataController;
import sample.NetWork.InventoryService;
import sample.NetWork.OrderService;

import java.util.ArrayList;
import java.util.List;

public class UpdateOrderController {
    @FXML
    private Label idLB= new Label();

    @FXML
    private Label staffLB= new Label();

    @FXML
    private Label createAtLB= new Label();

    @FXML
    private Label customerLB= new Label();

    @FXML
    private Label updateAtLB= new Label();

    @FXML
    private TextArea noteTA = new TextArea();

    @FXML
    private TextField paidTF= new TextField();

    @FXML
    private TextField productFilterTF= new TextField();

    @FXML
    private TableView<Inventory> productTable = new TableView<>();

    @FXML
    private TableColumn<Inventory,String> productCodeCol = new TableColumn<>();

    @FXML
    private TableColumn<Inventory,Integer> stockCol= new TableColumn<>();

    @FXML
    private TextField quantityTF= new TextField();

    @FXML
    private TextField priceTF= new TextField();

    @FXML
    private TextField discountTF= new TextField();

    @FXML
    private Button addToOrderBT= new Button();

    @FXML
    private TableView<OrderLine> orderLineTable = new TableView<>();

    @FXML
    private TableColumn<OrderLine, String> productCol= new TableColumn<>();

    @FXML
    private TableColumn<OrderLine, Integer> quantityCol= new TableColumn<>();

    @FXML
    private TableColumn<OrderLine, Float> priceCol= new TableColumn<>();

    @FXML
    private TableColumn<OrderLine, Integer> discountCol= new TableColumn<>();

    @FXML
    private TableColumn<OrderLine, Float> totalPriceCol= new TableColumn<>();

    @FXML
    private Button deleteItemBT= new Button();

    @FXML
    private Button oKBT= new Button();

    @FXML
    private Button cancelBT= new Button();

    @FXML
    private Button refreshInventoryBT = new Button();
    Order order;
    List<OrderLine> orderLines = new ArrayList<>();
    ObservableList<Inventory> productObservableList = FXCollections.observableArrayList();
    ObservableList<OrderLine> orderLineObservableList = FXCollections.observableArrayList();

    InventoryService inventoryService = new InventoryService();
    OrderService orderService = new OrderService();
    public UpdateOrderController() {

    }

    public void setOrder(Order order){
        this.order = order;

        idLB.setText(String.valueOf(order.getId()));
        staffLB.setText(order.getStaff());
        createAtLB.setText(order.getCreateAt());
        updateAtLB.setText(order.getUpdateAt());
        customerLB.setText(order.getCustomer());
        noteTA.setText(order.getNote());
        paidTF.setText(String.valueOf(order.getPaid()));

        loadProductData();
        loadOrderLineData();
    }

    @FXML
    public void initialize(){
        setupProductTable();
        setupOderLineTable();
        getProductFilterTextField();
    }
    private void setupProductTable(){
        productCodeCol.setCellValueFactory( new PropertyValueFactory<>("code"));
        stockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productTable.setItems(productObservableList);
        productTable.getColumns().clear();
        productTable.getColumns().addAll(productCodeCol,stockCol);
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
    private void loadOrderLineData(){
        Runnable runnable = ()->{
            try {
                orderLines = orderService.getOrderItems(order.getId());
                orderLineObservableList.setAll(orderLines);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
    @FXML
    private void deleteItem(){
        if(orderLineTable.getSelectionModel().getSelectedItem()!=null){
            OrderLine orderLine = orderLineTable.getSelectionModel().getSelectedItem();
            orderLineObservableList.remove(orderLine);
            orderLines.remove(orderLine);
        }
    }
    @FXML
    public void addToOrder(){
        if(productTable.getSelectionModel().getSelectedItem() != null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            try {
                if(quantityTF.equals("") || quantityTF.getText().equals(""))
                    throw new Exception("please fill up determine quantity");
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
    public void updateOrder(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        order.setOrderLines(orderLines);
        order.setPaid(Float.valueOf(paidTF.getText()));
        order.setNote(noteTA.getText());
        try{
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(orderService.updateOrder(order));
            alert.show();
        }
        catch (Exception e){
            alert.setContentText("Error: " + e.getMessage());
            alert.showAndWait();
        }
    }
    @FXML
    public void closeUpdateWindow(ActionEvent event){
        Stage stage = (Stage) cancelBT.getScene().getWindow();
        stage.close();

    }

    @FXML
    public void refreshInventory(){
        loadProductData();
    }
}
