package sample.Customer;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import sample.Model.Customer;
import sample.NetWork.CustomerService;

public class UpdateCustomerController {
    Customer customer;
    CustomerService customerService = new CustomerService();
    @FXML
    private TextField nameTF = new TextField();

    @FXML
    private TextField phoneTF= new TextField();

    @FXML
    private Label idLB = new Label();

    @FXML
    private TextField addressTF= new TextField();

    @FXML
    private TextField contactPersonTF= new TextField();

    @FXML
    private TextArea noteTA = new TextArea();

    @FXML
    private Button updateBT = new Button();

    @FXML
    void updateCustomer( ) {
        if(phoneTF.getText() ==null || phoneTF.getText().equals(""))
            phoneTF.setText("N/A");
        Customer customer = new Customer(nameTF.getText(),phoneTF.getText());
        customer.setId(this.customer.getId());
        if(addressTF.getText() ==null || addressTF.getText().equals(""))
            addressTF.setText("N/A");
        if(contactPersonTF.getText() ==null || contactPersonTF.getText().equals(""))
            contactPersonTF.setText("N/A");
        if(noteTA.getText() ==null || noteTA.getText().equals(""))
            noteTA.setText("N/A");
        customer.setAddress(addressTF.getText());
        customer.setContactPerson(contactPersonTF.getText());
        customer.setNote(noteTA.getText());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        try {
            alert.setContentText(customerService.updateCustomer(customer));
        } catch (Exception e) {
            alert.setContentText(e.getMessage());
            alert.show();
        }
        alert.show();
    }
    public void setCustomer(Customer customer){
        this.customer = customer;

        idLB.setText("ID: "+String.valueOf(customer.getId()));
        nameTF.setText(customer.getName());
        phoneTF.setText(customer.getPhone());
        addressTF.setText(customer.getAddress());
        contactPersonTF.setText(customer.getContactPerson());
        noteTA.setText(customer.getNote());

    }
}
