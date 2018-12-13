package sample.Customer;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import sample.Model.Customer;
import sample.NetWork.CustomerService;

public class CreateCustomerController {
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
    void findStock() {

    }

    @FXML
    void moveToCreateOrder() {

    }

    @FXML
    void moveToFindOrder() {

    }

    @FXML
    void moveToShowProducts() {

    }
}
