/**
 * Created by:
 * Carl-Johan Dahlman, cjda@kth.se
 * Waleed Hassan, waleedh@kth.se
 * on 14/12/15.
 */

package controllers;

import controllers.albumControllers.AddAlbumController;
import controllers.albumControllers.EditAlbumController;
import controllers.albumControllers.ReviewAlbumController;
import controllers.albumControllers.ShowAlbumController;
import controllers.movieControllers.AddMovieController;
import controllers.movieControllers.EditMovieController;
import controllers.movieControllers.ReviewMovieController;
import controllers.movieControllers.ShowMovieController;
import javafx.scene.Parent;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import models.Album;
import models.Model;
import models.Movie;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import main.Main;
public class MainController implements Initializable {

    private AddAlbumController addAlbumController;
    private DeleteController deleteController;
    private EditAlbumController editAlbumController;
    private ShowAlbumController showAlbumController;
    private AddMovieController addMovieController;
    private EditMovieController editMovieController;
    private ShowMovieController showMovieController;
    private ReviewMovieController reviewMovieController;
    private ReviewAlbumController reviewAlbumController;
    private Model model;
    private ArrayList<RadioButton> albumSearchRadioButtons = new ArrayList<>();
    private ArrayList<RadioButton> movieSearchRadioButtons = new ArrayList<>();
    private ToggleGroup movieSearchGroup, albumSearchGroup;

    private Parent main;
    private Stage primaryStage;
    private ObservableList <Album> albums;
    @FXML private TableView <Album> albumTable;
    @FXML private TableView <Movie> movieTable;
    @FXML private TextField albumSearchField, movieSearchField;
    @FXML private RadioButton albumTitleSearch, albumArtistSearch, movieRatingSearch, movieDirectorSearch, movieGenreSearch;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        movieSearchGroup = movieDirectorSearch.getToggleGroup();
        albumSearchGroup = albumArtistSearch.getToggleGroup();

        albumSearchRadioButtons.add(albumTitleSearch);
        albumSearchRadioButtons.add(albumArtistSearch);

        movieSearchRadioButtons.add(movieDirectorSearch);
        movieSearchRadioButtons.add(movieRatingSearch);
        movieSearchRadioButtons.add(movieGenreSearch);

    }

    public void setPrimaryStage(Stage primaryStage){
        this.primaryStage = primaryStage;
    }

    public void setModel(Model model){
        this.model = model;
    }

    public void setMain(Parent main) {
        this.main = main;
    }

    public void setControllers(AddAlbumController addAlbumController, EditAlbumController editAlbumController,
                               DeleteController deleteController, ShowAlbumController showAlbumController,
                               AddMovieController addMovieController, EditMovieController editMovieController,
                               ShowMovieController showMovieController, ReviewMovieController reviewMovieController,
                               ReviewAlbumController reviewAlbumController){

        this.reviewAlbumController = reviewAlbumController;

        this.addAlbumController = addAlbumController;
        addAlbumController.setPrimaryStage(primaryStage);

        this.editAlbumController = editAlbumController;
        editAlbumController.setPrimaryStage(primaryStage);
        editAlbumController.setAlbumTable(albumTable);
        // todo set albumTable


        this.showAlbumController = showAlbumController;
        showAlbumController.setPrimaryStage(primaryStage);

        this.deleteController = deleteController;

        this.addMovieController = addMovieController;
        addMovieController.setPrimaryStage(primaryStage);

        this.editMovieController = editMovieController;
        editMovieController.setPrimaryStage(primaryStage);
        editMovieController.setMovieTable(movieTable);

        this.showMovieController = showMovieController;

        this.reviewMovieController = reviewMovieController;


    }

    public void toMenue(){

        primaryStage.setScene(new Scene(main, Main.WIDTH, Main.HEIGHT));
        primaryStage.show();
    }




    // - - - - - - - - - Albums - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public void reviewAlbum(){
        Album selected = albumTable.getSelectionModel().getSelectedItem();
        reviewAlbumController.setAlbumTable(selected);
        reviewAlbumController.reviewAlbumStage();
    }

    public void getAllAlbumRatings(){
        for (Album a: model.getAlbums()){
            model.updateAlbumRating(a.getAlbumID());
        }
    }

    public void getAlbums(){
        model.getNewAlbums();
    }


    public void refreshAlbums(){
        albumTable.setItems(model.getAlbums());
        albumTable.refresh();
    }

    public void showSelectedAlbum(MouseEvent me){
        Album selected = albumTable.getSelectionModel().getSelectedItem();
        if (me.getClickCount() == 2 && !(selected == null)) {
            showAlbumController.showAlbum(selected);
        }
    }
    public void addAlbum(){
        addAlbumController.createAlbum();
    }

    public void editAlbum(ActionEvent e) {
        Album selected = albumTable.getSelectionModel().getSelectedItem();
        if (!(selected == null)){
            editAlbumController.editAlbumItems(selected);
        }
    }

    public void searchForAlbum(){
        System.out.println("Searching for album");
        System.out.println(albumSearchField.getText());

        RadioButton rb = (RadioButton) albumSearchGroup.getSelectedToggle();
        if (albumSearchField.getText().length() <= 0){
            model.getNewAlbums();
        }
        if (albumSearchField.getText().length() > 0 && rb.getText().equals("Title")){
            System.out.println("Search for title");
            model.getSearchForAlbums(albumSearchField.getText(), 1);
        }
        if (albumSearchField.getText().length() > 0 && rb.getText().equals("Artist")){
            System.out.println("Search for Artist");
            model.getSearchForAlbums(albumSearchField.getText(), 2);
        }

    }

    public void deleteAlbum(){
        Thread thread = new Thread(){
            public void run(){
                deleteController.deleteAlbum(albumTable.getSelectionModel().getSelectedItem());
                getAlbums();
            }
        };thread.start();
    }

    // - - - - - - - - - - Movies - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public void editMovie() {
        Movie selected = movieTable.getSelectionModel().getSelectedItem();
        if (!(selected == null)) {
            editMovieController.editMovieItems();
        }
    }

    public void reviewMovie(){
        Movie selected = movieTable.getSelectionModel().getSelectedItem();
        reviewMovieController.setMovie(selected);
        reviewMovieController.reviewMovieStage();
    }

    public void showSelectedMovie(MouseEvent me){
        Movie selected = movieTable.getSelectionModel().getSelectedItem();
        if (me.getClickCount() == 2 && !(selected == null)) {
            showMovieController.showMovie(selected);
        }
    }

    public void showMovies(){
        model.getNewMovies();
    }
    public void refreshMovies(){
        movieTable.setItems(model.getMovies());
        movieTable.refresh();
    }
    public void addMovie(){
        addMovieController.createMovie();
    }
    public void deleteMovie(){
        Thread thread = new Thread(){
            public void run(){
                deleteController.deleteMovie(movieTable.getSelectionModel().getSelectedItem());
                showMovies();
            }
        };thread.start();
    }

    public void getAllMovieRatings(){
        for (Movie m: model.getMovies()){
            model.updateMovieRating(m.getMovieID());
        }
    }
    public void searchForMovies(){
        System.out.println("Searching for Movie");
        System.out.println(movieSearchField.getText());

        RadioButton rb = (RadioButton) movieSearchGroup.getSelectedToggle();
        if (movieSearchField.getText().length() <= 0){
            model.getNewMovies();
        }
        if (movieSearchField.getText().length() > 0 && rb.getText().equals("Director")){
            System.out.println("Search for director movie");
            model.getSearchForMovies(movieSearchField.getText(), 1);
        }
        if (movieSearchField.getText().length() > 0 && rb.getText().equals("Genre")){
            System.out.println("Search for genre movie");
            model.getSearchForMovies(movieSearchField.getText(), 2);
        }
        if (movieSearchField.getText().length() > 0 && rb.getText().equals("Rating")){
            System.out.println("Search for rating movie");
            model.getSearchForMovies(movieSearchField.getText(), 3);
        }
    }



    public void exitMediaCenter(){
        //todo close connection to database
    }

}

