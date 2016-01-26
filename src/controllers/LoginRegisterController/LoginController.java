/**
 * Created by:
 * Carl-Johan Dahlman, cjda@kth.se
 * Waleed Hassan, waleedh@kth.se
 * on 14/12/15.
 */
package controllers.LoginRegisterController;

import controllers.MainController;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Model;

/**
 * This is the controller class for the login view.
 */
public class LoginController {

    private Stage loginStage,primaryStage;
    private Parent add,root;
    private Model model;
    private MainController mainController;
    private RegisterController registerController;

    @FXML private PasswordField password;
    @FXML private TextField userName;
    @FXML private Label text;
    //@FXML private MenuItem registerId;

    /**
     *
     * @param model
     */
    public void setModel(Model model){
        this.model = model;
    }

    /**
     * Initialize the primary stage.
     * @param primaryStage
     * @param root
     */
    public void setPrimaryStage(Stage primaryStage,Parent root){
        this.primaryStage = primaryStage;
        this.root = root;
    }

    /**
     * Initialize the the scene and sets the stage before show time.
     * @param mainController
     * @param registerController
     */
    public void setMainRegisterController(MainController mainController,RegisterController registerController) {
        this.mainController = mainController;
        this.registerController = registerController;
    }

    /**
     * Set the parent
     * @param parent
     */
    public void setParent(Parent parent){
        this.add = parent;
        loginStage = new Stage();
        loginStage.setScene(new Scene(add, 600,400));
    }

    /**
     * Shows login stage
     */
    public void setLoginScene(){
        loginStage.show();
    }

    /**
     * This checks the login information against the database and confirms or denies the user access to the program
     */
    public void checkLogin(){

        System.out.println(password.getText().toString() + " " + userName.getText().toString());

        if (password.getText().isEmpty() || userName.getText().isEmpty()){
            text.setText("wrong password or username!");
        }else {
            Thread thread = new Thread() {
                public void run() {
                    if (model.authenticateUser(userName.getText(),password.getText())) {
                        javafx.application.Platform.runLater(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        loginStage.close();
                                        mainController.toMenue();
                                    }
                                }
                        );
                    }
                    else{
                        javafx.application.Platform.runLater(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        text.setText("wrong password or username!");
                                    }
                                }
                        );
                    }
                }
            };
            thread.start();
        }
    }

    /**
     * This is the controller method for launching the register controller.
     */
    public void registerNewUser(){
        userName.clear();
        password.clear();
        registerController.setRegisterScene();
    }

}
