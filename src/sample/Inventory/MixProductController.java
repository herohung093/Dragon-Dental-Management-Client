package sample.Inventory;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sample.Model.Product;
import sample.Model.ProductInput;
import sample.Model.Staff;
import sample.NetWork.InventoryService;

import java.io.IOException;
import java.util.ArrayList;

public class MixProductController {
    @FXML
    private MenuBar mainMenu;



    @FXML
    private Label NN1LB = new Label();

    @FXML
    private TextField NN1TF = new TextField();

    @FXML
    private Label NN2SLB= new Label();

    @FXML
    private TextField NN2STF= new TextField();

    @FXML
    private Label NN0LB= new Label();

    @FXML
    private TextField NN0TF= new TextField();

    @FXML
    private Label NN3STLB= new Label();

    @FXML
    private TextField NN3STTF= new TextField();

    @FXML
    private ChoiceBox<String> NNCB =new ChoiceBox<>();

    @FXML
    private Button NNBT=new Button();

    @FXML
    private Label NTC1LB= new Label();

    @FXML
    private TextField NTC1TF= new TextField();

    @FXML
    private Label NTC2SLB= new Label();

    @FXML
    private TextField NTC2STF= new TextField();

    @FXML
    private Label NTC0LB= new Label();

    @FXML
    private TextField NTC0TF= new TextField();

    @FXML
    private Label NTC3STLB= new Label();

    @FXML
    private TextField NTC3STTF= new TextField();

    @FXML
    private Label NTCA1LB= new Label();

    @FXML
    private TextField NTCA1TF= new TextField();

    @FXML
    private Label NTCA2LB= new Label();

    @FXML
    private TextField NTCA2TF= new TextField();

    @FXML
    private Label NTCA3LB= new Label();

    @FXML
    private TextField NTCA3TF= new TextField();

    @FXML
    private ChoiceBox<String> NTCCB=new ChoiceBox<>();

    @FXML
    private Button NTCBT =new Button();
    ArrayList<String> nn = new ArrayList<>();
    ArrayList<String> ntc = new ArrayList<>();
    InventoryService inventoryService = new InventoryService();
    @FXML
    void initialize(){
        nn.add("NN-64M");
        nn.add("NN-37M");
        nn.add("NN-10M");

        ntc.add("NTC-64M");
        ntc.add("NTC-37M");
        ntc.add("NTC-10M");

        NNCB.getItems().addAll(nn);
        NTCCB.getItems().addAll(ntc);
    }
    public void mixNN(){
        int total = 0;
        if(Integer.valueOf(NN1TF.getText()) !=0){
            total = total +Integer.valueOf(NN1TF.getText());
            ProductInput productInput = new ProductInput(new Product(NN1LB.getText()),"Making "+NNCB.getValue(),Integer.valueOf(NN1TF.getText()),new Staff("Mai Thi Vu"));
            try {
                inventoryService.decreaseStock(productInput);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(Integer.valueOf(NN2STF.getText()) !=0){
            total = total +Integer.valueOf(NN2STF.getText());
            ProductInput productInput = new ProductInput(new Product(NN2SLB.getText()),"Making "+NNCB.getSelectionModel().getSelectedItem(),Integer.valueOf(NN2STF.getText()),new Staff("Mai Thi Vu"));
            try {
                inventoryService.decreaseStock(productInput);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(Integer.valueOf(NN0TF.getText()) !=0){
            total = total +Integer.valueOf(NN0TF.getText());
            ProductInput productInput = new ProductInput(new Product(NN0LB.getText()),"Making "+NNCB.getSelectionModel().getSelectedItem(),Integer.valueOf(NN0TF.getText()),new Staff("Mai Thi Vu"));
            try {
                inventoryService.decreaseStock(productInput);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(Integer.valueOf(NN3STTF.getText()) !=0){
            total = total +Integer.valueOf(NN3STTF.getText());
            ProductInput productInput = new ProductInput(new Product(NN3STLB.getText()),"Making "+NNCB.getSelectionModel().getSelectedItem(),Integer.valueOf(NN3STTF.getText()),new Staff("Mai Thi Vu"));
            try {
                inventoryService.decreaseStock(productInput);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ProductInput productInput = new ProductInput(new Product (NNCB.getSelectionModel().getSelectedItem()),"Mix NN",total,new Staff("Mai Thi Vu"));
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        try {
            alert.setContentText(inventoryService.increaseStock(productInput));
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
            alert.setContentText(e.getMessage());
            alert.show();
        }
    }
    public void mixNTC(){
        int total = 0;
        if(Integer.valueOf(NTC1TF.getText()) !=0){
            total = total + Integer.valueOf(NTC1TF.getText());
            ProductInput productInput = new ProductInput(new Product(NTC1LB.getText()),"Making "+NTCCB.getSelectionModel().getSelectedItem(),Integer.valueOf(NTC1TF.getText()),new Staff("Mai Thi Vu"));
            try {
                inventoryService.decreaseStock(productInput);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(Integer.valueOf(NTC2STF.getText()) !=0){
            total = total + Integer.valueOf(NTC2STF.getText());
            ProductInput productInput = new ProductInput(new Product(NTC2SLB.getText()),"Making "+NTCCB.getSelectionModel().getSelectedItem(),Integer.valueOf(NTC2STF.getText()),new Staff("Mai Thi Vu"));
            try {
                inventoryService.decreaseStock(productInput);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(Integer.valueOf(NTC0TF.getText()) !=0){
            total = total + Integer.valueOf(NTC0TF.getText());
            ProductInput productInput = new ProductInput(new Product(NTC0LB.getText()),"Making "+NTCCB.getSelectionModel().getSelectedItem(),Integer.valueOf(NTC0TF.getText()),new Staff("Mai Thi Vu"));
            try {
                inventoryService.decreaseStock(productInput);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(Integer.valueOf(NTC3STTF.getText()) !=0){
            total = total + Integer.valueOf(NTC3STTF.getText());
            ProductInput productInput = new ProductInput(new Product(NTC3STLB.getText()),"Making "+NTCCB.getSelectionModel().getSelectedItem(),Integer.valueOf(NTC3STTF.getText()),new Staff("Mai Thi Vu"));
            try {
                inventoryService.decreaseStock(productInput);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(Integer.valueOf(NTCA1TF.getText()) !=0){
            total = total + Integer.valueOf(NTCA1TF.getText());
            ProductInput productInput = new ProductInput(new Product(NTCA1LB.getText()),"Making "+NTCCB.getSelectionModel().getSelectedItem(),Integer.valueOf(NTCA1TF.getText()),new Staff("Mai Thi Vu"));
            try {
                inventoryService.decreaseStock(productInput);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(Integer.valueOf(NTCA2TF.getText()) !=0){
            total = total + Integer.valueOf(NTCA2TF.getText());
            ProductInput productInput = new ProductInput(new Product(NTCA2LB.getText()),"Making "+NTCCB.getSelectionModel().getSelectedItem(),Integer.valueOf(NTCA2TF.getText()),new Staff("Mai Thi Vu"));
            try {
                inventoryService.decreaseStock(productInput);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(Integer.valueOf(NTCA3TF.getText()) !=0){
            total = total + Integer.valueOf(NTCA3TF.getText());
            ProductInput productInput = new ProductInput(new Product(NTCA3LB.getText()),"Making "+NTCCB.getSelectionModel().getSelectedItem(),Integer.valueOf(NTCA3TF.getText()),new Staff("Mai Thi Vu"));
            try {
                inventoryService.decreaseStock(productInput);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ProductInput productInput = new ProductInput(new Product (NTCCB.getSelectionModel().getSelectedItem()),"Mix NTC ",total,new Staff("Mai Thi Vu"));
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        try {
            alert.setContentText(inventoryService.increaseStock(productInput));
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
            alert.setContentText(e.getMessage());
            alert.show();
        }
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
