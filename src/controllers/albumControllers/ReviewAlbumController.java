package controllers.albumControllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Album;
import models.Model;
import models.Review;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * Created by Scalman on 01/01/16.
 */
public class ReviewAlbumController implements Initializable{

    private Model model;
    private Stage primaryStage, reviewStage;
    private Parent review;
    private ToggleGroup group;
    private int rating;
    private Album album;
    private Review userReview = null;
    private Date date;
    private String text;
    private ArrayList<RadioButton> radioButtons;

    @FXML private TextArea textReview;
    @FXML private RadioButton one,two,three,four,five;
    @FXML private Label title;
    @FXML private ImageView imgView;
    @FXML private Label errorLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        group = new ToggleGroup();
        radioButtons = new ArrayList<>();
        one.setToggleGroup(group);
        radioButtons.add(one);
        two.setToggleGroup(group);
        radioButtons.add(two);
        three.setToggleGroup(group);
        radioButtons.add(three);
        four.setToggleGroup(group);
        radioButtons.add(four);
        five.setToggleGroup(group);
        radioButtons.add(five);
    }

    public void setModel(Model model){
        this.model = model;
    }
    public void setPrimaryStage(Stage primaryStage){
        this.primaryStage = primaryStage;
    }

    public void reviewAlbumStage(){
        Thread thread = new Thread(){
            public void run(){
                System.out.println(album);
                System.out.println("rewAlb innan");
                userReview = model.getAlbumReview(model.getUser().getUserID(),album.getAlbumID());
                System.out.println("efter" + userReview);
                if (userReview != null){
                    javafx.application.Platform.runLater(
                            new Runnable() {
                                @Override
                                public void run() {
                                    System.out.println(userReview);
                                    textReview.setText(userReview.getText());
                                    for (RadioButton rb : radioButtons){
                                        if (userReview.getRating() == Integer.parseInt(rb.getText()))
                                            rb.setSelected(true);
                                    }
                                }
                            }
                    );
                }
            }
        };thread.start();
        reviewStage.show();
    }

    public void setAlbumTable(Album selectedAlbum){
        this.album = selectedAlbum;
        title.setText(this.album.getTitle());

        //System.out.println("picture : " + selectedMovie.getCoverUrl());
        Image img = new Image(album.getCoverUrl());
        imgView.setImage(img);
    }

    public void setParent(Parent parent){
        this.review = parent;
        reviewStage = new Stage();
        reviewStage.setScene(new Scene(review, 600, 400));
        reviewStage.initModality(Modality.APPLICATION_MODAL);
        //editStage.initOwner(primaryStage);
    }

    public void radioButton(){
        rating = 0;
        RadioButton rb = (RadioButton) group.getSelectedToggle();
        rating = Integer.parseInt(rb.getText());
        //System.out.println(rating);
    }

    public void submitReview(){

        date = new Date();
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        System.out.println(date);

        if (userReview == null){
            if (textReview.getText().length() > 0 && rating > 0) {
                model.addAlbumReview(model.getUser().getUserID(),album.getAlbumID(),sqlDate,textReview.getText(),rating);
                reviewStage.close();
                clearFields();
            }else {
                errorLabel.setText("Please make both options");
                return;
            }
        }
        if (userReview != null){
            if (userReview.getRating() != rating || !userReview.getText().equals(textReview) ) {
                System.out.println("uppdaterar");
                model.updateAlbumReview(model.getUser().getUserID(),album.getAlbumID(),sqlDate,textReview.getText(),rating);
                reviewStage.close();
                clearFields();
            }else {
                reviewStage.close();
                clearFields();
            }
        }
    }

    public void deleteReview(){
        if (userReview != null) {
            model.deleteAlbumReview(model.getUser().getUserID(), album.getAlbumID());
            clearFields();
            reviewStage.close();
        }
    }

    public int getRating() {
        return rating;
    }

    private void clearFields(){
        textReview.clear();
        errorLabel.setText("");
        if (group.getSelectedToggle() != null)
            group.getSelectedToggle().setSelected(false);
    }
}
