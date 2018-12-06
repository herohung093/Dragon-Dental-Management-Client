package sample.Inventory;


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
import sample.Main;
import sample.Model.Inventory;
import sample.Model.Product;
import sample.Model.ProductInput;
import sample.Model.Staff;
import sample.NetWork.DataController;
import sample.NetWork.InventoryService;
import sample.NetWork.StaffService;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.util.List;
import java.util.function.UnaryOperator;

public class InventoryController {
    List<Inventory> inventories;
    @FXML
    TextField filterTF = new TextField();
    @FXML
    TextField codeTF = new TextField();
    @FXML
    TextField stockTF = new TextField();
    @FXML
    TextArea descriptionTA = new TextArea();
    @FXML
    ChoiceBox<Staff> staffChoiceBox = new ChoiceBox<>();
    @FXML
    Button setStockBT = new Button();
    @FXML
    private MenuItem showProductsMenuItem;
    @FXML
    MenuBar mainMenu;

    @FXML
    TableView<Inventory> tableView = new TableView<Inventory>();
    @FXML
    TableColumn <Inventory, String> codeCol = new TableColumn<Inventory, String>("Product Code");
    @FXML
    TableColumn <Inventory, Integer> stockCol = new TableColumn<Inventory, Integer>("Stock");
    ObservableList<Inventory> inventoryObservableList = FXCollections.observableArrayList();
    ObservableList<Staff>  staffObservableList = FXCollections.observableArrayList();
    InventoryService inventoryService = new InventoryService();
    StaffService staffService = new StaffService();


    public InventoryController() {
    }

    @FXML
    public void initialize() throws Exception {
        loadInventoryDataData();
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
    }
    public void setupTableView() {

        codeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
        stockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));

        tableView.setItems(inventoryObservableList);
        tableView.getColumns().clear();
        tableView.getColumns().addAll(codeCol,stockCol);
        tableView.setOnMouseClicked((MouseEvent event)->{
            if(event.getButton().equals(MouseButton.PRIMARY)){
                codeTF.setText(tableView.getSelectionModel().getSelectedItem().getCode());
                stockTF.setText(String.valueOf(tableView.getSelectionModel().getSelectedItem().getStock()));
                staffChoiceBox.getSelectionModel().selectFirst(); // set Default Staff to 1st
            }
        });

         getFilterTextField();

    }
    public void getFilterTextField()
    {
        FilteredList<Inventory> filteredList =
                new FilteredList<>(tableView.getItems(), i -> true);
        SortedList<Inventory> sortedList = new SortedList<>(filteredList);
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
    public void setStock(){

        Alert alert = new Alert(Alert.AlertType.ERROR);
        try {

        if(codeTF.getText() == null || codeTF.getText().equals("") || stockTF.getText().equals("") || stockTF.getText() == null)
        {
            throw new Exception("Please make sure product code and stock are filled");
        }
        Product product = new Product(codeTF.getText());
        Staff staff = new Staff(staffChoiceBox.getSelectionModel().getSelectedItem().getName());
        int quantity = Integer.parseInt(stockTF.getText());
        ProductInput productInput = new ProductInput(product,descriptionTA.getText().trim(),quantity,staff);
        String response = inventoryService.updateInventory(productInput);
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(response);
        alert.show();
    } catch (Exception e) {

        alert.setContentText("Error: " + e.getMessage());
        alert.showAndWait();
    }
    refreshData();
}

    public void increaseStock(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        try {

            if(codeTF.getText() == null || codeTF.getText().equals("") || stockTF.getText().equals("") || stockTF.getText() == null)
            {
                throw new Exception("Please make sure product code and stock are filled");
            }
            Product product = new Product(codeTF.getText());
            Staff staff = new Staff(staffChoiceBox.getSelectionModel().getSelectedItem().getName());
            int quantity = Integer.parseInt(stockTF.getText());
            ProductInput productInput = new ProductInput(product,descriptionTA.getText().trim(),quantity,staff);
            String response = inventoryService.increaseStock(productInput);
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(response);
            alert.show();
        } catch (Exception e) {

            alert.setContentText("Error: " + e.getMessage());
            alert.showAndWait();
        }
        refreshData();
    }
    public void setupStaffCB(){
        staffChoiceBox.setItems(staffObservableList);
    }

    public void loadInventoryDataData(){
        Runnable runnable = ()->{
            try {
                inventoryObservableList.setAll(DataController.getDataInstance().getInventories());
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
                staffObservableList.setAll(DataController.getDataInstance().getStaff());
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
    public void refreshData(){
        Runnable runnable = ()->{
            try {
                inventoryObservableList.setAll(inventoryService.getAll());
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
    @FXML
    private void moveToShowProducts(ActionEvent event) throws IOException{

        VBox inventoryParent = FXMLLoader.load(getClass().getResource("/sample/Product/ProductView.fxml"));
        Scene inventoryScene = new Scene(inventoryParent);
        Stage window = (Stage) mainMenu.getScene().getWindow();
        window.setScene(inventoryScene);
        window.show();
    }

}
