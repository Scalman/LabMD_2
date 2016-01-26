/**
 * Created by:
 * Carl-Johan Dahlman, cjda@kth.se
 * Waleed Hassan, waleedh@kth.se
 * on 14/12/15.
 */
package controllers.albumControllers;

import enums.AlbumGenre;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Album;
import models.Model;
import models.Review;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * This is the controller class for the show album view.
 */
public class ShowAlbumController implements Initializable {

    private Stage showStage, primaryStage;
    private Parent show;
    private Model model;
    private AlbumGenre genre;
    private ArrayList<Review> reviews = new ArrayList<>();
    @FXML private Label titleLabel, artistLabel, genreLabel, raitingLabel, submittedByField;
    @FXML private ImageView albumCover;
    @FXML private TextArea reviewArea;

    public void initialize(URL location, ResourceBundle resources) {

    }
    /**
     * Initialize the primary stage.
     * @param primaryStage
     */
    public void setPrimaryStage(Stage primaryStage){
        this.primaryStage = primaryStage;

    }

    public void setModel(Model model) {
        this.model = model;
    }

    /**
     * Initialize the the scene and sets the stage before show time.
     * @param parent
     */
    public void setParent(Parent parent){
        this.show = parent;
        showStage = new Stage();
        showStage.setScene(new Scene(show, 460, 372));
        showStage.initModality(Modality.APPLICATION_MODAL);
    }

    /**
     * Takes the selected albums data and fill album text labels.
     * @param album
     */
    public void showAlbum(Album album) {

        Thread thread = new Thread() {
            public void run() {
                reviews = model.getAlbumReviews(album.getAlbumID());
                javafx.application.Platform.runLater(
                        new Runnable() {
                            @Override
                            public void run() {
                                StringBuilder reviewText = new StringBuilder();
                                for (Review r : reviews){
                                    reviewText.append(r.toString());
                                }
                                reviewArea.setText(reviewText.toString());
                            }
                        }
                );
            }

        };thread.start();


            titleLabel.setText(album.getTitle());
            artistLabel.setText(album.getArtist());
            genreLabel.setText(album.getGenre().toString());
            String rating = "" + album.getRating();
            raitingLabel.setText(rating);
            submittedByField.setText(album.getUsername());

            Image cover = new Image(album.getCoverUrl());
            albumCover.setImage(cover);
            showStage.show();
    }
}

