/**
 * Created by:
 * Carl-Johan Dahlman, cjda@kth.se
 * Waleed Hassan, waleedh@kth.se
 * on 14/12/15.
 */
package models;

import javafx.collections.ObservableList;
import java.util.ArrayList;



public interface Screwdriver {

    ObservableList<Album> getAllAlbums();

    ObservableList<Album> getAlbumsBySearch(String searchWord, int item);

    ObservableList<Movie> getAllMovies();

    ObservableList<Movie> getMoviesBySearch(String searchWord, int item);


    void insertAlbum(String title, String genre, int userID, String artistName);

    void insertAlbumOnly(String title, String genre, int userID, int artistID);

    void alterAlbum(int albumID,String title, String genre, String artistName, String coverURL);

    void alterAlbumOnly(int albumID,String title, String genre, int artistID, String coverURL);

    void dropAlbum(int albumID);

    int getArtistByName(String name);

    void insertMovie(String title, String genre, int userID, String directorName);

    void insertMovieOnly(String title, String genre, int userID, int directorID);

    void alterMovie(int movieID,String title, String genre, String coverURL, String directorName);

    void alterMovieOnly(int movieID,String title, String genre, int directorID, String coverURL);

    void dropMovie(int movieID);

    int getDirectorByName(String name);

    User userAuthentication(String username,String password);

    User getUser(String username);

    void insertNewUser(String username, String password);

    void insertNewReview(int userID, int mediaID, java.sql.Date date, String text, int rating, int mediaType);

    double getAvgRating(int id, int mediaType);

    Review checkIfReviewAlreadyExist(int usrID, int mediaID, int mediaType);

    ArrayList<Review> getAlbumReviews(int albumID);

    ArrayList<Review> getMovieReviews(int movieID);

    void deleteReview(int usrID, int mediaID, int mediaType);

    void updateReview(int userID, int albumID, java.sql.Date date, String text, int rating, int mediaType);

}
