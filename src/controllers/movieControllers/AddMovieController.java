/**
 * Created by:
 * Carl-Johan Dahlman, cjda@kth.se
 * Waleed Hassan, waleedh@kth.se
 * on 14/12/15.
 */
package controllers.movieControllers;

import enums.MovieGenre;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Model;
import models.Movie;

import java.net.URL;
import java.util.ResourceBundle;


public class AddMovieController implements Initializable {


    private Stage addStage, primaryStage;
    private Parent add;
    private Model model;
    private int directorID;
    @FXML private TextField addTitle;
    @FXML private TextField addMovieDirector;
    @FXML private ChoiceBox<MovieGenre> addGenre;
    @FXML private TextField addUrl;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
    public void setChoiceBoxes(){

        addGenre.setItems(model.getMovieGenreList());
    }
    public void setModel(Model model){
        this.model = model;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setParent(Parent add) {
        this.add = add;
        addStage = new Stage();
        addStage.setScene(new Scene(add, 460,260));
        addStage.initModality(Modality.APPLICATION_MODAL);
    }

    public void createMovie(){
        addStage.show();
    }

    public void saveMovie(){

        boolean movieExists = false;

        for(Movie m: model.getMovies()){
            if (m.getTitle().toUpperCase().equals(addTitle.getText().toUpperCase())
                    && m.getDirector().toUpperCase().equals(addMovieDirector.getText().toUpperCase())){
                movieExists = true;
            }
        }

        if (!movieExists) {
            Thread thread = new Thread() {
                public void run() {
                    directorID = model.getDirectorId(addMovieDirector.getText());
                    if (directorID <= 0) {
                        model.createMovie(addTitle.getText(),addGenre.getValue().toString(),addMovieDirector.getText());
                    }
                    else {
                        model.createMovieFromExistingDirector(addTitle.getText(),addGenre.getValue().toString(),directorID);
                    }
                    System.out.println(model.getUser().toString() + " Added a movie");
                    model.getNewMovies();
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

    public void abortEdit(){
        addStage.close();
        clearTextFields();
    }

    private void clearTextFields(){
        addMovieDirector.clear();
        addTitle.clear();
        addUrl.clear();
        addGenre.setValue(null);

    }
}
