/**
 * Created by:
 * Carl-Johan Dahlman, cjda@kth.se
 * Waleed Hassan, waleedh@kth.se
 * on 14/12/15.
 * This class is connected to the database through JDBC Class
 * and works as a model container with all important data structure.
 */

package models;
import controllers.MainController;
import enums.AlbumGenre;
import enums.MovieGenre;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.ArrayList;

public class Model {


    private MainController mainController;
    private int artistID;
    private int directorID;
    private User user;
    private ObservableList<Album> albums;
    private ObservableList<Movie> movies;
    private ObservableList<AlbumGenre> albumGenreList;
    private ObservableList<MovieGenre> movieGenreList;
    private JDBCDatabase database;



    public Model(){
        albums = FXCollections.observableArrayList();
        movies = FXCollections.observableArrayList();
        albumGenreList = FXCollections.observableArrayList();
        movieGenreList = FXCollections.observableArrayList();
        database = new JDBCDatabase();
        for (AlbumGenre ag: AlbumGenre.values()){
            albumGenreList.add(ag);
        }

        for (MovieGenre mg: MovieGenre.values()){
            movieGenreList.add(mg);
        }
    }

    /**
     * sets the mainController.
     * @param mainController
     */
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    /**
     * Checks the user input password/username matches the corresponding in the database.
     * If true login succeed.
     * @param username user input.
     * @param password user input.
     * @return false or true.
     */
    public boolean authenticateUser(String username, String password){

        user = database.userAuthentication(username,password);

        if (user != null){
            System.out.println("User != null  " + user.toString());
            return true;
        }
        return false;
    }
    /**
     * Checks user input if username already exists in the database.
     * If true user registration failed.
     * @param username user input.
     * @return false or true depends if username already exists.
     */
    public boolean checkForUser(String username){

        user = database.getUser(username);

        if (user != null){
            user = null;
            return true;
        }
        return false;
    }

    /**
     * Getter.
     * @return user object.
     */
    public User getUser() {
        return user;
    }


    //* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    //                                              ALBUM
    //* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

    /**
     * fetches the album if the id matching the corresponding id in the model.
     * @param id specific id from the the table view.
     * @return album if it is a match else null.
     */
    public Album getAlbumById(int id) {
        for (Album a : albums){
            if (a.getAlbumID() == id){
                return a;
            }
        }
        return null;
    }

    /**
     * Album list getter and setter.
     * @return ObservableList Album.
     */
    public ObservableList<Album> getAlbums(){
        return albums;
    }
    public void setAlbums(ObservableList<Album> albums) {
        this.albums = albums;
    }

    /**
     * Runs on a separate thread and brings all the albums in the database.
     * Thread setts the album list in a run later buffer so that the UIThread updates the view.
     */
    public void getNewAlbums(){
        Thread thread = new Thread(){
            public void run(){
                ObservableList<Album> newAlbums = database.getAllAlbums();
                javafx.application.Platform.runLater(
                        new Runnable() {
                            @Override
                            public void run() {
                                setAlbums(newAlbums);
                                mainController.getAllAlbumRatings();
                            }
                        }
                );
            }
        };thread.start();
    }

    /**
     * Creates album if artist does not exists.
     * @param title album title
     * @param genre album genre
     * @param artistName artist name
     */
    public void createAlbum(String title, String genre,String artistName){
        database.insertAlbum(title,genre,user.getUserID(),artistName);
    }

    /**
     *Creates album if artist already exists.
     * @param title album title
     * @param genre album genre
     * @param artistID artist id
     */
    public void createAlbumFromExistingArtist(String title, String genre,int artistID){
        database.insertAlbumOnly(title,genre,user.getUserID(),artistID);
    }

    /**
     * calls to the database controller to edit the selected album. will handle if artist does not exist by creating it.
     * @param albumId
     * @param artist
     * @param genre
     * @param title
     * @param url
     */
    public void editAlbum(int albumId,String artist, String genre, String title, String url){

        boolean albumExists = false;

        for(Album a: database.getAllAlbums()){
            if (a.getTitle().toUpperCase().equals(title.toUpperCase())
                    && a.getArtist().toUpperCase().equals(artist.toUpperCase())){
                albumExists = true;
            }
        }

        if(!albumExists){
            Thread thread = new Thread(){
                public void run(){
                    artistID = getArtistId(artist);
                    if (artistID <= 0){

                        database.alterAlbum(albumId,title,genre,artist,url);

                        System.out.println(user.toString() + "editFunktion:");
                        javafx.application.Platform.runLater(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        getNewAlbums();
                                    }
                                }
                        );

                    }else {
                        database.alterAlbumOnly(albumId,title,genre,artistID,url);

                        System.out.println(user.toString() + "editFunktion:");
                        javafx.application.Platform.runLater(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        getNewAlbums();
                                    }
                                }
                        );
                    }


                    //albums = database.getAlbums(queries.getAllAlbums);

                }
            };thread.start();
        }

    }

    /**
     * calls to the database controller to delete the specified album
     * @param albumID ID of the selected album
     */
    public void deleteAlbum(int albumID){
        Thread thread = new Thread(){
            public void run(){
                database.dropAlbum(albumID);
                javafx.application.Platform.runLater(
                        new Runnable() {
                            @Override
                            public void run() {
                                getNewAlbums();
                            }
                        }
                );
            }
        };thread.start();
    }

    /**
     * calls to the database controller to fetch the ID of the name of an artist
     * @param artist
     * @return returns the artist ID
     */
    public int getArtistId(String artist){
        return database.getArtistByName(artist);
    }

    /**
     * calls to the database controller to fetch the albums that matches the searchword.
     * @param searchWord
     * @param item item specifies if searching for title or artist
     */
    public void getSearchForAlbums(String searchWord, int item){
        Thread thread = new Thread(){
            public void run(){
                ObservableList<Album> newAlbums = database.getAlbumsBySearch(searchWord, item);
                javafx.application.Platform.runLater(
                        new Runnable() {
                            @Override
                            public void run() {
                                setAlbums(newAlbums);
                                mainController.refreshAlbums();
                                mainController.getAllAlbumRatings();
                            }
                        }
                );
            }
        };thread.start();
    }

    /**
     * calls to the database controller to update an albums average rating. Will add to runlater to refreshAlbum in mainController
     * @param id
     */
    public void updateAlbumRating(int id){
        int mediaType = 1;
        Thread thread = new Thread(){
            public void run(){
                getAlbumById(id).setRating(database.getAvgRating(id, mediaType));
                javafx.application.Platform.runLater(
                        new Runnable() {
                            @Override
                            public void run() {
                                mainController.refreshAlbums();
                            }
                        }
                );
            }

        };thread.start();

    }

    /**
     * calls to the database controller to add a review to the specific album
     * @param userID
     * @param albumID
     * @param date
     * @param text
     * @param rating
     */
    public void addAlbumReview(int userID, int albumID, java.sql.Date date, String text, int rating){
        int mediaType = 1;
        Thread thread = new Thread(){
            public void run(){


                Review review = database.checkIfReviewAlreadyExist(userID,albumID,mediaType);
                if (review == null){

                    database.insertNewReview(userID,albumID,date,text,rating,mediaType);
                    javafx.application.Platform.runLater(
                            new Runnable() {
                                @Override
                                public void run() {
                                    updateAlbumRating(albumID);
                                }
                            }
                    );
                }
            }
        };thread.start();

    }

    /**
     * calls to the database controller to update an album review
     * @param userID
     * @param albumID
     * @param date
     * @param text
     * @param rating
     */
    public void updateAlbumReview(int userID, int albumID, java.sql.Date date, String text, int rating){
        int mediaType = 1;
        Thread thread = new Thread(){
            public void run(){
                database.updateReview(userID,albumID, date, text, rating, mediaType);

                javafx.application.Platform.runLater(
                        new Runnable() {
                            @Override
                            public void run() {
                                updateAlbumRating(albumID);
                            }
                        }
                );
            }
        };thread.start();

    }

    /**
     * calls to the database controller to delete album review;
     * @param userID
     * @param albumID
     */
    public void deleteAlbumReview(int userID, int albumID){
        int mediaType = 1;
        Thread thread = new Thread(){
            public void run(){
                database.deleteReview(userID,albumID,mediaType);
            }
        };thread.start();
    }

    /**
     * calls to the database controller to fetch all the reviews for a specific album
     * @param albumID
     * @return List of reviews
     */
    public ArrayList<Review> getAlbumReviews(int albumID){
        return database.getAlbumReviews(albumID);
    }

    /**
     * calls to the database controller to check if there's a review that matches the parameters
     * @param userID
     * @param albumID
     * @return If found it will return a review
     */
    public Review getAlbumReview(int userID, int albumID){
        int mediaType = 1;

        Review review = database.checkIfReviewAlreadyExist(userID, albumID, mediaType);

        return review;
    }

    /**
     * get the album genre list
     * @return list of album genres
     */
    public ObservableList<AlbumGenre> getAlbumGenreList() {
        return albumGenreList;
    }


    //* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    //                                              MOVIE
    //* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

    /**
     * gets a movie from the movie list that matches the ID
     * @param id
     * @return returns a movie or null if no match
     */
    public Movie getMovieById(int id){
        for (Movie m : movies){
            if (m.getMovieID() == id){
                return m;
            }
        }
        return null;
    }

    /**
     * calls to the database controller to create a movie which director does not yet exists in the database
     * @param title
     * @param genre
     * @param directorName
     */
    public void createMovie(String title, String genre, String directorName){
        database.insertMovie(title,genre, user.getUserID(), directorName);
    }

    /**
     * calls to the database controller to create a movie that already have a director present in the database
     * @param title
     * @param genre
     * @param directorID
     */
    public void createMovieFromExistingDirector(String title, String genre, int directorID){
        database.insertMovieOnly(title,genre, user.getUserID(), directorID);
    }

    /**
     * calls to the database controller to delete a movie by id. Will add to runlater getNewMovies, see doc for more info
     * @param movieId
     */
    public void deleteMovie(int movieId){
        Thread thread = new Thread(){
            public void run(){
                database.dropMovie(movieId);
                javafx.application.Platform.runLater(
                        new Runnable() {
                            @Override
                            public void run() {
                                getNewMovies();
                            }
                        }
                );
            }

        };thread.start();
    }

    /**
     * calls to the database controller to get the ID of the name of a director
     * @param name
     * @return
     */
    public int getDirectorId(String name){
        return database.getDirectorByName(name);
    }

    /**
     * calls to the database controller to create a user
     * @param username
     * @param password
     */
    public void createUser(String username, String password){
        database.insertNewUser(username,password);
    }

    /**
     * calls to the database controller to alter a movie. Will also add to runlater to call getNewMovies, see doc for more info
     * @param movieID
     * @param director
     * @param genre
     * @param title
     * @param url
     */
    public void editMovie(int movieID,String director, String genre, String title, String url){

        boolean movieExists = false;
        //this.artist = artist;

        for(Movie m: database.getAllMovies()){
            if (m.getTitle().toUpperCase().equals(title.toUpperCase())
                    && m.getDirector().toUpperCase().equals(director.toUpperCase())){
                movieExists = true;
            }
        }

        if(!movieExists){
            Thread thread = new Thread(){
                public void run(){
                    directorID = getDirectorId(director);
                    if (directorID <= 0){
                        database.alterMovie(movieID,title,genre,url,director);

                    }else {

                        database.alterMovieOnly(movieID,title,genre,directorID,url);
                    }
                    System.out.println(user.toString() + "editFunktion:");
                    getNewMovies();
                }
            };thread.start();
        }
    }

    /**
     * Calls to the database controller to add a review to a movie
     * @param userID
     * @param movieID
     * @param date
     * @param text
     * @param rating
     */
    public void addMovieReview(int userID, int movieID, java.sql.Date date, String text, int rating){
        int mediaType = 2;
        Thread thread = new Thread(){
            public void run(){

                Review review = database.checkIfReviewAlreadyExist(userID,movieID,mediaType);
                if (review == null){
                    database.insertNewReview(userID,movieID,date,text,rating,mediaType);
                    javafx.application.Platform.runLater(
                            new Runnable() {
                                @Override
                                public void run() {
                                    updateMovieRating(movieID);
                                }
                            }
                    );
                }
            }
        };thread.start();

    }

    /**
     * calls for the database controller to update a review. Will add to "runlater" call for uppdateMovieRating
     * @param userID
     * @param movieID
     * @param date
     * @param text
     * @param rating
     */
    public void updateMovieReview(int userID, int movieID, java.sql.Date date, String text, int rating){
        int mediaType = 2;
        Thread thread = new Thread(){
            public void run(){

                database.updateReview(userID, movieID, date, text, rating, mediaType);
                javafx.application.Platform.runLater(
                        new Runnable() {
                            @Override
                            public void run() {
                                updateMovieRating(movieID);
                            }
                        }
                );
            }
        };thread.start();
    }

    /**
     * calls to the database controller to update rating for movie by id
     * @param id
     */
    public void updateMovieRating(int id){
        int mediaType = 2;
        Thread thread = new Thread(){
            public void run(){
                getMovieById(id).setRating(database.getAvgRating(id, mediaType));
                javafx.application.Platform.runLater(
                        new Runnable() {
                            @Override
                            public void run() {
                                mainController.refreshMovies();
                            }
                        }
                );
            }

        };thread.start();
    }

    /**
     * calls to the database controller to fetch the review that matches userID and movieID
     * @param userID
     * @param movieID
     * @return review
     */
    public Review getMovieReview(int userID, int movieID){
        int mediaType = 2;

        Review review = database.checkIfReviewAlreadyExist(userID,movieID,mediaType);

        return review;
    }

    /**
     * calls to the database controller to fetch a list of all review that matches the movieID
     * @param movieID
     * @return list of reviews
     */
    public ArrayList<Review> getMovieReviews(int movieID){

        return database.getMovieReviews(movieID);
    }


    /**
     * calls to the database controller to Delete the review
     * @param userID userID
     * @param movieID movieID
     */
    public void deleteMovieReview(int userID, int movieID){
        int mediaType = 2;
        Thread thread = new Thread(){
            public void run(){
                database.deleteReview(userID,movieID,mediaType);
            }
        };thread.start();
    }

    /**
     * calls to the database controller to fetch the all the movies. Will also add to the "runlater" calls to
     * setMovies and getAllMovieRationgs from mainController
     */
    public void getNewMovies() {
        Thread thread = new Thread(){
            public void run(){
                movies = database.getAllMovies();
                javafx.application.Platform.runLater(
                        new Runnable() {
                            @Override
                            public void run() {
                                setMovies(movies);
                                mainController.getAllMovieRatings();
                            }
                        }
                );
            }
        };thread.start();
    }

    /**
     * Gets a movie from the list of movies that matches the index value
     * @param index index of movie
     * @return Returns a movie
     */
    public Movie getMovie(int index){
        return movies.get(index);
    }

    /**
     * Geter for the avaiable list of movies
     * @return A list of movies
     */
    public ObservableList<Movie> getMovies() {
        return movies;
    }

    /**
     * Sets the list of available movies in the model
     * @param movies An observable list of the type Movie
     */
    public void setMovies(ObservableList<Movie> movies) {
        this.movies = movies;
    }

    /**
     * Getter for the movie genre list
     * @return Returns a list of genres
     */
    public ObservableList<MovieGenre> getMovieGenreList() {
        return movieGenreList;
    }

    /**
     * This method calls to the database controller class and asks for the movies that matches the search word.
     * It will after that push instructions to the "runlater" in javafx to call on setMovies and will also tell it to call on refreshMovies and
     * getAllMovieRatings from the mainController.
     * @param searchWord guess what ?
     * @param item Specifies if the item of search is title, genre or rating
     */
    public void getSearchForMovies(String searchWord, int item){
        Thread thread = new Thread(){
            public void run(){
                ObservableList<Movie> newMovies = database.getMoviesBySearch(searchWord, item);
                javafx.application.Platform.runLater(
                        new Runnable() {
                            @Override
                            public void run() {
                                setMovies(newMovies);
                                mainController.refreshMovies();
                                mainController.getAllMovieRatings();
                            }
                        }
                );
            }
        };thread.start();
    }
}
