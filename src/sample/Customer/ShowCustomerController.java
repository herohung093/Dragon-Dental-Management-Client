package sample.Customer;

import javafx.application.Platform;
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
import sample.Model.Customer;
import sample.Model.Interface.CustomerSold;
import sample.Model.Inventory;
import sample.Model.Product;
import sample.NetWork.AnalysisService;
import sample.NetWork.CustomerService;
import sample.NetWork.DataController;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static java.time.temporal.TemporalAdjusters.firstDayOfYear;
import static java.time.temporal.TemporalAdjusters.lastDayOfYear;

public class ShowCustomerController {
    @FXML
    MenuBar mainMenu;
    @FXML
    private TextField customerFilterTF = new TextField();

    @FXML
    private TableView<Customer> customerTable = new TableView<>();

    @FXML
    private TableColumn<Customer,String> customerCol = new TableColumn<>();

    @FXML
    private DatePicker startDate = new DatePicker();

    @FXML
    private DatePicker endDate= new DatePicker();

    @FXML
    private TableView<CustomerSold> detailTable= new TableView<>();

    @FXML
    private TableColumn<CustomerSold, String> productCol = new TableColumn<>();

    @FXML
    private TableColumn<CustomerSold, Integer> quantityCol = new TableColumn<>();
    @FXML
    private TextField productFilterTF = new TextField();
    @FXML
    private Label debtTF = new Label();


    CustomerService customerService = new CustomerService();
    AnalysisService analysisService = new AnalysisService();
    ObservableList<Customer> customerObservableList = FXCollections.observableArrayList();
    ObservableList<CustomerSold> customerSoldObservableList = FXCollections.observableArrayList();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    LocalDate now = LocalDate.now();
    LocalDate firstDay = now.with(firstDayOfYear()); //get start day of the current year
    LocalDate lastDay = now.with(lastDayOfYear());

    @FXML
    void initialize(){
        setupCustomerTable();
        loadCustomerData();
        setupDetailTable();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        customerTable.getSelectionModel().selectedItemProperty().addListener(observable ->{
            try{
                Runnable runnable = ()->{
                    try { if(customerTable.getSelectionModel().getSelectedItem()!=null){
                        if(startDate.getValue() != null){
                            customerSoldObservableList.setAll(analysisService.getCustomerSoldDetail(customerTable.getSelectionModel()
                                    .getSelectedItem().getId(),startDate.getValue()
                                    .format(formatter),endDate.getValue().format(formatter)));
                        }else {
                            customerSoldObservableList.setAll(analysisService.getCustomerSoldDetail(customerTable.getSelectionModel()
                                    .getSelectedItem().getId(),firstDay.format(formatter),lastDay.format(formatter)));
                        }


                    }
                        Platform.runLater(new Runnable() {
                            @Override public void run() {
                                try {
                                    debtTF.setText(String.valueOf(analysisService.getDebtByCustomerId(customerTable.getSelectionModel().getSelectedItem().getId())));
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
            catch (Exception e){
                alert.setContentText("Error: " + e.getMessage());
                alert.showAndWait();
            }
        } );
        getProductFilterTextField();
        getCustomerFilterTextField();
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


    private void setupDetailTable() {
        productCol.setCellValueFactory(new PropertyValueFactory<>("product"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        detailTable.setItems(customerSoldObservableList);
        detailTable.getColumns().clear();
        detailTable.getColumns().addAll(productCol,quantityCol);
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

    private void setupCustomerTable() {
        customerCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerTable.setItems(customerObservableList);
        customerTable.getColumns().clear();
        customerTable.getColumns().add(customerCol);
    }
    private void getProductFilterTextField() {
        FilteredList<CustomerSold> filteredList =
                new FilteredList<>(detailTable.getItems(), i -> true);
        SortedList<CustomerSold> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(detailTable.comparatorProperty());
        detailTable.setItems(sortedList);

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
    @FXML
    void findStock( ) throws IOException {
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
    private void moveToShowProducts(ActionEvent event) throws IOException{
        VBox showProductParent = FXMLLoader.load(getClass().getResource("/sample/Product/ProductView.fxml"));
        Scene showProductScene = new Scene(showProductParent);

        Stage window = (Stage) mainMenu.getScene().getWindow();
        window.setScene(showProductScene);
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
}
