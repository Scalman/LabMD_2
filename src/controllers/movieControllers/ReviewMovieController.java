/**
 * Created by:
 * Carl-Johan Dahlman, cjda@kth.se
 * Waleed Hassan, waleedh@kth.se
 * on 14/12/15.
 */

package controllers.movieControllers;
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
import models.Movie;
import models.Review;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class ReviewMovieController implements Initializable{

    private Model model;
    private Stage primaryStage, reviewStage;
    private TableView<Movie> movieTable;
    private Parent review;
    private ToggleGroup group;
    private int rating;
    private Movie movie;
    private Date date;
    private Review userReview = null;
    private String text;
    private ArrayList<RadioButton> radioButtons;

    @FXML private TextArea textReview;
    @FXML private RadioButton one,two,three,four,five;
    @FXML private Label title;
    @FXML private ImageView imgView;
    @FXML private Label empty;
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

    public void reviewMovieStage(){
        Thread thread = new Thread(){
            public void run(){
                System.out.println(movie);
                System.out.println("rewMov innan");
                userReview = model.getMovieReview(model.getUser().getUserID(),movie.getMovieID());
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

    public void setMovie(Movie selectedMovie){
        this.movie = selectedMovie;
        title.setText(this.movie.getTitle());

        //System.out.println("picture : " + selectedMovie.getCoverUrl());
        Image img = new Image(movie.getCoverUrl());
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
                model.addMovieReview(model.getUser().getUserID(),movie.getMovieID(),sqlDate,textReview.getText(),rating);
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
                model.updateMovieReview(model.getUser().getUserID(),movie.getMovieID(),sqlDate,textReview.getText(),rating);
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
            model.deleteMovieReview(model.getUser().getUserID(), movie.getMovieID());
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