package sample.Product;

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
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sample.Model.Inventory;
import sample.Model.Product;
import sample.Model.ProductInput;
import sample.Model.Staff;
import sample.NetWork.DataController;
import sample.NetWork.InventoryService;
import sample.NetWork.ProductService;
import sample.NetWork.StaffService;

import java.io.IOException;
import java.util.function.UnaryOperator;

public class ProductController {
    @FXML
    TextField filterTF = new TextField();
    @FXML
    TextField codeTF = new TextField();
    @FXML
    TextField nameTF = new TextField();
    @FXML
    TextField stockTF = new TextField();
    @FXML
    TextArea descriptionTA = new TextArea();
    @FXML
    TextField unitTF = new TextField();
    @FXML
    TextField priceTF = new TextField();
    @FXML
    Button addProductBT = new Button();
    @FXML
    ChoiceBox<Staff> staffChoiceBox = new ChoiceBox<>();
    @FXML
    TableView<Product> tableView = new TableView<Product>();
    @FXML
    TableColumn<Product, String> codeCol = new TableColumn<Product, String>("Product Code");
    @FXML
    TableColumn <Product, String> nameCol = new TableColumn<Product, String>("Name");
    @FXML
    TableColumn <Product, Float> priceCol = new TableColumn<Product, Float>("Base Price");
    @FXML
    TableColumn <Product, String> unitCol = new TableColumn<Product, String>("Unit");
    ObservableList<Product> productObservableList = FXCollections.observableArrayList();
    ObservableList<Staff> staffObservableList = FXCollections.observableArrayList();

    StaffService staffService = new StaffService();
    ProductService productService = new ProductService();

    @FXML
    MenuBar mainMenu;
    @FXML
    MenuItem showInventory;
    public ProductController() {
    }
    @FXML
    public void initialize() throws Exception {
        loadProductData();
        loadStaffData();
        setupTableView();
        setupStaffCB();

        UnaryOperator<TextFormatter.Change> filter = change -> {
            String text = change.getText();

            if (text.matches("[0-9]*")) {
                return change;
            }

            return null;
        };
        TextFormatter<String> textFormatter = new TextFormatter<>(filter);
        stockTF.setTextFormatter(textFormatter);
        //priceTF.setTextFormatter(textFormatter);
    }
    public void setupTableView() throws Exception {

        codeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        unitCol.setCellValueFactory(new PropertyValueFactory<>("unit"));

        tableView.setItems(productObservableList);
        tableView.getColumns().clear();
        tableView.getColumns().addAll(codeCol,nameCol,priceCol,unitCol);
        tableView.setOnMouseClicked((MouseEvent event)->{
            if(event.getButton().equals(MouseButton.PRIMARY)){

                staffChoiceBox.getSelectionModel().selectFirst(); // set Default Staff to 1st
            }
        });

        getFilterTextField();

    }

    public void loadProductData(){
        Runnable runnable = ()->{
            try {

                productObservableList.setAll(productService.getAll());
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
    public void loadStaffData(){
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
    private void getFilterTextField() {
        FilteredList<Product> filteredList =
                new FilteredList<>(tableView.getItems(), i -> true);
        SortedList<Product> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedList);

        filterTF.textProperty().addListener(((observable, oldValue, newValue) ->
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
    public void addNewProduct(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        try {
            if(codeTF.getText() == null || codeTF.getText().equals("") || stockTF.getText().equals("") || stockTF.getText() == null|| nameTF.getText().equals("") || nameTF.getText() == null ||unitTF.getText().equals("")||priceTF.getText().equals("")||descriptionTA.getText().equals(""))
            {
                throw new Exception("Make sure all the fields are filled");
            }

            Product product = new Product(codeTF.getText(),nameTF.getText(),Float.valueOf(priceTF.getText().trim()),unitTF.getText().trim());
            Staff staff = new Staff(staffChoiceBox.getSelectionModel().getSelectedItem().getName());
            int quantity = Integer.parseInt(stockTF.getText());
            ProductInput productInput = new ProductInput(product,descriptionTA.getText().trim(),quantity,staff);

            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(productService.addProduct(productInput));
            alert.show();
        } catch (Exception e) {

            alert.setContentText("Error: " + e.getMessage());
            alert.showAndWait();
        }
        Runnable runnable = ()->{
            try {

                productObservableList.setAll(productService.getAll());
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
    public void setupStaffCB(){
        staffChoiceBox.setItems(staffObservableList);
        staffChoiceBox.getSelectionModel().selectFirst();
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
