/**
 * Created by:
 * Carl-Johan Dahlman, cjda@kth.se
 * Waleed Hassan, waleedh@kth.se
 * on 14/12/15.
 */
package controllers.LoginRegisterController;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Model;

/**
 * This is the controller class for the register new user view
 */
public class RegisterController {


    private Parent register;
    private Stage registerStage;
    private Model model;
    @FXML private Label text;
    @FXML private TextField newUsername, newPassword, retypePassword;


    public void setModel(Model model){
        this.model = model;
    }


    public void setParent(Parent parent){
        this.register = parent;
        registerStage = new Stage();
        registerStage.setScene(new Scene(register, 500,300));
        registerStage.initModality(Modality.APPLICATION_MODAL);
    }

    public void setRegisterScene(){
        registerStage.show();
    }

    /**
     *
     */
    public void registerNewUser(){

        if (!(newPassword.getText().equals(retypePassword.getText()))){
            text.setText("Passwords don't match");
            return;
        }

        if (newUsername.getText().length() <= 4 || newPassword.getText().length() <=4){
            System.out.println("null");
            text.setText("Username or password > 4 char");
            return;
        }
        Thread thread = new Thread() {
            public void run() {

                if (model.checkForUser(newUsername.getText())){
                    javafx.application.Platform.runLater(
                            new Runnable() {
                                @Override
                                public void run() {
                                    text.setText("Username already exists");
                                }
                            }
                    );
                }else {
                    model.createUser(newUsername.getText(),newPassword.getText());
                    javafx.application.Platform.runLater(
                            new Runnable() {
                                @Override
                                public void run() {
                                    newUsername.clear();
                                    newPassword.clear();
                                    retypePassword.clear();
                                    registerStage.close();
                                }
                            }
                    );

                }

            }
        };
        System.out.println("jddjdjddjdjdjjdjdjjdj");
        thread.start();


    }

}
