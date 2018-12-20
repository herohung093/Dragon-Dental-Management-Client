package sample.Customer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sample.Model.Customer;
import sample.NetWork.CustomerService;

import java.io.IOException;

public class CreateCustomerController {
    @FXML
    MenuBar mainMenu;
    @FXML
    private TextField nameTF = new TextField();

    @FXML
    private TextField phoneTF= new TextField();

    @FXML
    private TextField addressTF= new TextField();

    @FXML
    private TextField contactPersonTF= new TextField();

    @FXML
    private TextArea noteTA = new TextArea();

    @FXML
    private Button createBT = new Button();
    CustomerService customerService = new CustomerService();
    @FXML
    private void createCustomer(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        String name = nameTF.getText();
        if(name==null || name.equals("")){
            alert.setContentText("Customer Name can not be empty");
            alert.show();
        }else {
            try{

                Customer customer = new Customer(name,phoneTF.getText()+""
                        ,addressTF.getText()+"",contactPersonTF.getText()
                        +""+noteTA.getText()+"");
                alert.setContentText(customerService.createCustomer(customer));
                alert.showAndWait();
            }
            catch (Exception e){
                alert.setContentText("Error: " + e.getMessage());
                alert.showAndWait();
            }
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
    @FXML
    private void moveToMixProduct() throws IOException {
        VBox findOrderParent = FXMLLoader.load(getClass().getResource("/sample/Inventory/MixProductView.fxml"));
        Scene findOrderScene = new Scene(findOrderParent);

        Stage window = (Stage) mainMenu.getScene().getWindow();
        window.setScene(findOrderScene);
        window.show();
    }
}
