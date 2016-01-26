/**
 * Created by:
 * Carl-Johan Dahlman, cjda@kth.se
 * Waleed Hassan, waleedh@kth.se
 * on 14/12/15.
 */
package controllers.movieControllers;

import enums.AlbumGenre;
import enums.MovieGenre;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Album;
import models.Model;
import models.Movie;

import java.net.URL;
import java.util.ResourceBundle;


public class EditMovieController implements Initializable {

    private Stage editStage, primaryStage;
    private Parent edit;
    private AlbumGenre genre;
    private Movie selectedMovie;
    private Model model;
    private int index;
    private TableView<Movie> movieTable;

    @FXML
    private TextField editTitle, editDirector, editUrl;
    @FXML private ChoiceBox editGenre;
    @FXML private ImageView movieCover;
    @FXML private Button editSubmit;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setModel(Model model){
        this.model = model;
    }

    public void setPrimaryStage(Stage primaryStage){
        this.primaryStage = primaryStage;
    }
    public void setMovieTable(TableView<Movie> movieTable){
        this.movieTable = movieTable;
    }

    public void setChoiceBoxes(){

        editGenre.setItems(model.getMovieGenreList());
    }

    public void setParent(Parent parent){
        this.edit = parent;
        editStage = new Stage();
        editStage.setScene(new Scene(edit, 460, 260));
        editStage.initModality(Modality.APPLICATION_MODAL);
        //editStage.initOwner(primaryStage);
    }

    public void editMovieItems(){

        selectedMovie = movieTable.getSelectionModel().getSelectedItem();
        this.index = movieTable.getSelectionModel().getSelectedIndex();

        editTitle.setText(selectedMovie.getTitle());
        editDirector.setText(selectedMovie.getDirector());
        editGenre.setValue(selectedMovie.getGenre());

        editUrl.setText(selectedMovie.getCoverUrl());

        Image img = new Image(selectedMovie.getCoverUrl());
        movieCover.setImage(img);

        editStage.show();

    }

    public void saveMovie(){

        model.getMovie(index).setTitle(editTitle.getText());
        model.getMovie(index).setDirector(editDirector.getText());
        model.getMovie(index).setGenre((MovieGenre) editGenre.getValue());
        model.getMovie(index).setCoverUrl(editUrl.getText());

        model.editMovie(selectedMovie.getMovieID(), editDirector.getText(),
                editGenre.getValue().toString(),editTitle.getText(),editUrl.getText());



        movieTable.refresh();

        editStage.close();

    }

    public void abortEdit(){
        editStage.close();
    }



}
