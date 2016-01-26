package controllers.movieControllers;

import enums.AlbumGenre;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
import java.util.ResourceBundle;

/**
 * Created by Scalman on 15/12/15.
 */
public class ShowMovieController {

    private Stage showStage, primaryStage;
    private Parent show;
    private AlbumGenre genre;
    private ArrayList<Review> reviews;
    private Model model;
    @FXML private Label titleLabel, directorLabel, genreLabel, raitingLabel, submittedByLabel;
    @FXML private TextArea reviewField;
    @FXML private ImageView movieCover;

    public void initialize(URL location, ResourceBundle resources) {

    }
    public void setPrimaryStage(Stage primaryStage){
        this.primaryStage = primaryStage;
    }

    public void setParent(Parent parent){
        this.show = parent;
        showStage = new Stage();
        showStage.setScene(new Scene(show, 460, 372));
        showStage.initModality(Modality.APPLICATION_MODAL);
        //editStage.initOwner(primaryStage);
    }

    public void setModel(Model model){
        this.model = model;
    }

    public void showMovie(Movie movie){


        Thread thread = new Thread() {
            public void run() {
                reviews = model.getMovieReviews(movie.getMovieID());
                javafx.application.Platform.runLater(
                        new Runnable() {
                            @Override
                            public void run() {
                                StringBuilder reviewText = new StringBuilder();
                                for (Review r : reviews){
                                    reviewText.append(r.toString());
                                }
                                reviewField.setText(reviewText.toString());
                            }
                        }
                );
            }

        };thread.start();


        titleLabel.setText(movie.getTitle());
        directorLabel.setText(movie.getDirector());
        genreLabel.setText(movie.getGenre().toString());
        String rating = "" + movie.getRating();
        raitingLabel.setText(rating);
        submittedByLabel.setText(movie.getUsername());

        Image cover = new Image(movie.getCoverUrl());
        movieCover.setImage(cover);
        showStage.show();
    }
}
