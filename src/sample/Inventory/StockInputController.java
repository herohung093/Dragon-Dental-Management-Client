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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import sample.Model.Interface.BestSeller;
import sample.Model.Order;
import sample.Model.ProductInput;
import sample.Model.Staff;
import sample.NetWork.ProductInputService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static java.time.temporal.TemporalAdjusters.firstDayOfYear;
import static java.time.temporal.TemporalAdjusters.lastDayOfYear;

public class StockInputController {
    @FXML
    MenuBar mainMenu;
    @FXML
    private TextField productFilterTF = new TextField();

    @FXML
    private DatePicker startDate = new DatePicker();

    @FXML
    private DatePicker endDate = new DatePicker();

    @FXML
    private Button searchBT = new Button();

    @FXML
    private TableView<ProductInput> inputTable = new TableView<>();

    @FXML
    private TableColumn<ProductInput,String> productCol = new TableColumn<>();

    @FXML
    private TableColumn<ProductInput, Integer> quantityCol = new TableColumn<>();

    @FXML
    private TableColumn<ProductInput, String> dateCol = new TableColumn<>("Date");

    @FXML
    private TableColumn<ProductInput, String> noteCol = new TableColumn<>();

    @FXML
    private TableColumn<ProductInput, Staff> staffCol= new TableColumn<>();

    @FXML
    private TableColumn<ProductInput, Long> idCol= new TableColumn<>();

    @FXML
    private TableColumn<ProductInput,String> updateAtCol= new TableColumn<>();

    ObservableList<ProductInput> productInputObservableList = FXCollections.observableArrayList();

    ProductInputService productInputService = new ProductInputService();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    LocalDate now = LocalDate.now();
    LocalDate firstDay = now.with(firstDayOfYear()); //get start day of the current year
    LocalDate lastDay = now.with(lastDayOfYear());
    @FXML void initialize(){
        setupTable();
        loadProductInputData();
        getProductFilterTextField();

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
    }

    private void loadProductInputData() {
        Runnable runnable = ()->{
            try {
                productInputObservableList.setAll(productInputService.getBetweenDate(firstDay.format(formatter),lastDay.format(formatter)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    private void setupTable() {
        productCol.setCellValueFactory(new PropertyValueFactory<>("product"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("createAt"));
        noteCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        staffCol.setCellValueFactory(new PropertyValueFactory<>("operator"));
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        updateAtCol.setCellValueFactory(new PropertyValueFactory<>("updateAt"));
        inputTable.setItems(productInputObservableList);
        inputTable.getColumns().clear();
        inputTable.getColumns().addAll(productCol,quantityCol,dateCol,noteCol,staffCol,idCol,updateAtCol);
    }
    private void getProductFilterTextField() {
        FilteredList<ProductInput> filteredList =
                new FilteredList<>(inputTable.getItems(), i -> true);
        SortedList<ProductInput> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(inputTable.comparatorProperty());
        inputTable.setItems(sortedList);

        productFilterTF.textProperty().addListener(((observable, oldValue, newValue) ->
        {
            filteredList.setPredicate(inventoryView ->
            {
                if (newValue == null || newValue.isEmpty()){
                    return true;
                }
                String filterString = newValue.toUpperCase();
                if(inventoryView.getProduct().toUpperCase().contains(filterString))
                    return true;
                else return false;
            });
        }));
    }
    @FXML
    private void searchBetweenDate(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        Runnable runnable = ()->{
            try {
                productInputObservableList.setAll( productInputService.getBetweenDate(startDate.getValue()
                        .format(formatter),endDate.getValue().format(formatter)));
            } catch (Exception e) {
                alert.setContentText(e.getMessage());
                alert.show();
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
    private void moveToShowCustomer() throws IOException {
        VBox findOrderParent = FXMLLoader.load(getClass().getResource("/sample/Customer/ShowCustomerView.fxml"));
        Scene findOrderScene = new Scene(findOrderParent);

        Stage window = (Stage) mainMenu.getScene().getWindow();
        window.setScene(findOrderScene);
        window.show();
    }


}
