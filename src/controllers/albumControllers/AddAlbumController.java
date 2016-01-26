/**
 * Created by:
 * Carl-Johan Dahlman, cjda@kth.se
 * Waleed Hassan, waleedh@kth.se
 * on 14/12/15.
 */

package controllers.albumControllers;

import enums.AlbumGenre;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Album;
import models.Model;
import models.Movie;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * This is the controller class for the add album view.
 */
public class AddAlbumController implements Initializable {

    private Stage addStage, primaryStage;
    private Parent add;
    private Model model;
    private int artistId;

    @FXML private TextField addTitle;
    @FXML private TextField addArtist;
    @FXML private ChoiceBox<AlbumGenre> addGenre;
    @FXML private TextField addUrl;

    public void initialize(URL location, ResourceBundle resources) {

    }

    /**
     * Initialize the primary stage.
     * @param primaryStage
     */
    public void setPrimaryStage(Stage primaryStage){
        this.primaryStage = primaryStage;
    }
    /**
     * Initialize the model so this scene makes changes on the same data.
     * @param model
     */
    public void setModel(Model model){
        this.model = model;
    }
    /**
     * Initialize the choice boxes.
     */
    public void setChoiceBoxes(){

        addGenre.setItems(model.getAlbumGenreList());

    }
    /**
     * Initialize the the scene and sets prepare the stage before show time.
     * @param parent
     */
    public void setParent(Parent parent){
        this.add = parent;
        addStage = new Stage();
        addStage.setScene(new Scene(add, 460,260));
        addStage.initModality(Modality.APPLICATION_MODAL);
    }
    /**
     * Shows the stage.
     */
    public void createAlbum(){
        addStage.show();
    }

    /**
     * Sends a request to the database on a new thread.
     * Sends a new request to create if not exist.
     */
    public void saveAlbum(){

        boolean albumExists = false;

        for(Album a: model.getAlbums()){
            if (a.getTitle().toUpperCase().equals(addTitle.getText().toUpperCase())
                    && a.getArtist().toUpperCase().equals(addArtist.getText().toUpperCase())){
                albumExists = true;
            }
        }

        if (!albumExists) {
                Thread thread = new Thread() {
                    public void run() {
                        artistId = model.getArtistId(addArtist.getText());
                        System.out.println("innan skapad artist" + artistId);
                        if (artistId <= 0) {
                            model.createAlbum(addTitle.getText(),addGenre.getValue().toString(),addArtist.getText());
                        }
                        else {
                            model.createAlbumFromExistingArtist(addTitle.getText(),addGenre.getValue().toString(),artistId);
                        }
                        System.out.println(model.getUser().toString() + " Added an album");
                        model.getNewAlbums();

                        javafx.application.Platform.runLater(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        clearTextFields();
                                    }
                                }
                        );
                    }
                };
                thread.start();
        }
        addStage.close();
    }
    /**
     * Cancel mode.
     */
    public void abortEdit(){
        addStage.close();
        clearTextFields();
    }
    private void getArtistId(){
        Thread thread = new Thread(){
            public void run(){
                artistId = model.getArtistId(addArtist.getText());
                System.out.println("Innuti get artist id  " + artistId);
            }
        };thread.start();
    }
    /**
     * Clears the text fields.
     */
    private void clearTextFields(){
        addArtist.clear();
        addTitle.clear();
        addUrl.clear();
        addGenre.setValue(null);
    }
}
