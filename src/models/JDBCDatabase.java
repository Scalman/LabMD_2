/**
 * Created by:
 * Carl-Johan Dahlman, cjda@kth.se
 * Waleed Hassan, waleedh@kth.se
 * on 14/12/15.
 */

package models;

import enums.AlbumGenre;
import enums.MovieGenre;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.util.*;


public class JDBCDatabase implements Screwdriver {
    private String server;
    private String username = "mediaapp";
    private String password = "password";
    private SQLQueries queries;

    public JDBCDatabase (){
        this.server = "jdbc:mysql://localhost:3306/media?UseClientEnc=UTF8";
        queries = new SQLQueries();
    }

    /**
     * Creates a review for a movie or album depended on which mediaType is declared
     * @param userID
     * @param mediaID
     * @param date
     * @param text
     * @param rating
     * @param mediaType
     */
    @Override
    public void insertNewReview(int userID, int mediaID, java.sql.Date date, String text, int rating, int mediaType) {
        PreparedStatement stmt = null;
        Connection connection = null;

        try {
            connection = setupTheDatabaseConnectionSomehow();

            stmt = mediaType == 1 ? connection.prepareStatement(queries.addReviewAlbum()): connection.prepareStatement(queries.addReviewMovie());

            stmt.setInt(1,userID);
            stmt.setInt(2,mediaID);
            stmt.setDate(3,date);
            stmt.setString(4,text);
            stmt.setInt(5,rating);

            stmt.executeUpdate();
        }
        catch(Exception e) {
            javax.swing.JOptionPane.showMessageDialog(null,
                    "Database error, " + e.toString());
        }finally {
            try{
                connection.close();
                stmt.close();
                System.out.println("Connection closed");
            }catch (java.sql.SQLException sqlE){ System.out.println(sqlE.getMessage()); }

        }
    }

    /**
     * Checks the database if a review for an existing movie or album exists depended on which mediaType is declared
     * @param usrID
     * @param mediaID
     * @param mediaType
     * @return
     */
    @Override
    public Review checkIfReviewAlreadyExist(int usrID, int mediaID, int mediaType) {
        PreparedStatement stmt = null;
        Connection connection = null;
        Review review = null;
        try {
            connection = setupTheDatabaseConnectionSomehow();
            if (mediaType == 1){
                stmt = connection.prepareStatement(queries.albumAlreadyReviewed());
                stmt.setInt(1,usrID);
                stmt.setInt(2,mediaID);
            }


            if (mediaType == 2){
                stmt = connection.prepareStatement(queries.movieAlreadyReviewed());
                stmt.setInt(1,usrID);
                stmt.setInt(2,mediaID);
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                review = new Review(rs.getDate("RevDate"), rs.getInt("Rating"), rs.getString("Review"));
                System.out.println(review);
            }

        }catch (java.sql.SQLException sql){
            System.out.println(sql.getMessage());
        }
        return review;
    }

    /**
     * Searches the database for an artist by name
     * @param name
     * @return Returns ArtistID
     */
    @Override
    public int getArtistByName(String name) {
        PreparedStatement stmt = null;
        int id = -1;
        Connection connection = null;

        try {
            connection = setupTheDatabaseConnectionSomehow();
            stmt = connection.prepareStatement(queries.getArtistByName());
            stmt.setString(1,name);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                id = rs.getInt("ID");
                System.out.println(id);
            }
            //return artistId;
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(null,
                    "Database error, " + e.toString());
        }finally {
            try{
                stmt.close();
                connection.close();
                System.out.println("Connection closed");
            }catch (java.sql.SQLException sqlE){ System.out.println(sqlE.getMessage()); }
        }
        return id;
    }

    /**
     * Gets the average rating for a mediaType by going through their reviews
     * @param id
     * @param mediaType
     * @return Returns average rating
     */
    @Override
    public double getAvgRating(int id, int mediaType) {

        double avg = 0;
        PreparedStatement stmt = null;
        Connection connection = null;

        try {
            connection = setupTheDatabaseConnectionSomehow();


            stmt = mediaType == 1 ? connection.prepareStatement(queries.getAlbumRating()) : connection.prepareStatement(queries.getMovieRating());

            stmt.setInt(1,id);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                avg = rs.getDouble("avg(Rating)");
                System.out.println(avg);
            }

        }
        catch(Exception e) {
            javax.swing.JOptionPane.showMessageDialog(null,
                    "Database error, " + e.toString());
        }finally {

            try{
                connection.close();
                stmt.close();
                System.out.println("Connection closed");
            }catch (java.sql.SQLException sqlE){ System.out.println(sqlE.getMessage()); }

        }

        return avg;
    }

    /**
     * Gets all albums from database
     * @return Returns a list of all the albums in the database
     */
    @Override
    public ObservableList<Album> getAllAlbums() {
        ObservableList<Album> albums = null;
        PreparedStatement stmt = null;
        Connection connection = null;

        try {
            connection = setupTheDatabaseConnectionSomehow();
            stmt = connection.prepareStatement(queries.getAllAlbums);
            albums = getAlbumsQuery(stmt);

        }
        catch(Exception e) {
            javax.swing.JOptionPane.showMessageDialog(null,
                    "Database error, " + e.toString());
        }finally {
            try{
                connection.close();
                stmt.close();
                System.out.println("Connection closed");
            }catch (java.sql.SQLException sqlE){ System.out.println(sqlE.getMessage()); }

        }
        return albums;
    }

    /**
     * Gets all albums from database by search word
     * @param searchWord
     * @param item
     * @return Returns a list of all the albums in the database that matched the search word
     */
    @Override
    public ObservableList<Album> getAlbumsBySearch(String searchWord,int item) {
        ObservableList<Album> albums = null;
        PreparedStatement stmt = null;
        Connection connection = null;

        try {
            connection = setupTheDatabaseConnectionSomehow();
            stmt = connection.prepareStatement(queries.searchAlbums(item));
            stmt.setString(1,"%" + searchWord + "%");
            albums = getAlbumsQuery(stmt);

        }
        catch(Exception e) {
            javax.swing.JOptionPane.showMessageDialog(null,
                    "Database error, " + e.toString());
        }finally {
            try{
                connection.close();
                stmt.close();
                System.out.println("Connection closed");
            }catch (java.sql.SQLException sqlE){ System.out.println(sqlE.getMessage()); }

        }
        return albums;
    }

    /**
     * Gets all the movies from the database
     * @return Returns all the movies from the database
     */
    @Override
    public ObservableList<Movie> getAllMovies() {
        ObservableList<Movie> movies = null;
        PreparedStatement stmt = null;
        Connection connection = null;

        try {
            connection = setupTheDatabaseConnectionSomehow();
            stmt = connection.prepareStatement(queries.getAllMovies);

            movies = getMoviesQuery(stmt);

        }
        catch(Exception e) {
            javax.swing.JOptionPane.showMessageDialog(null,
                    "Database error, " + e.toString());
        }finally {
            try{
                connection.close();
                stmt.close();
                System.out.println("Connection closed");
            }catch (java.sql.SQLException sqlE){ System.out.println(sqlE.getMessage()); }

        }
        return movies;
    }

    /**
     * Gets all the movies by search word, or rating
     * @param searchWord
     * @param item
     * @return Returns all the movies from the database that matched the search word
     */
    @Override
    public ObservableList<Movie> getMoviesBySearch(String searchWord, int item) {
        ObservableList<Movie> movies = null;
        PreparedStatement stmt = null;
        Connection connection = null;

        try {
            connection = setupTheDatabaseConnectionSomehow();
            stmt = connection.prepareStatement(queries.searchMovies(item));

            System.out.println(item);
            if (item == 1 || item == 2){
                stmt.setString(1,"%" + searchWord + "%");
            }
            if (item == 3){
                double rating = Double.parseDouble(searchWord);
                int maxRating = (int) rating + 1;
                stmt.setDouble(1,rating);
                stmt.setInt(2,maxRating);
            }

            movies = getMoviesQuery(stmt);

        }
        catch(Exception e) {
            javax.swing.JOptionPane.showMessageDialog(null,
                    "Database error, " + e.toString());
        }finally {
            try{
                connection.close();
                stmt.close();
                System.out.println("Connection closed");
            }catch (java.sql.SQLException sqlE){ System.out.println(sqlE.getMessage()); }

        }

        return movies;
    }

    /**
     * This method is used if the given artist is not represented in the database. The method will therefor
     * first insert the artist into the database and after that insert the album.
     * @param title
     * @param genre
     * @param userID
     * @param artistName
     */
    @Override
    public void insertAlbum(String title, String genre, int userID, String artistName) {

        PreparedStatement insertAlbum = null;
        PreparedStatement insertArtist = null;
        PreparedStatement getArtistID = null;
        Connection connection = null;
        int artistID = 0;


        try {
            connection = setupTheDatabaseConnectionSomehow();
            connection.setAutoCommit(false);

            insertArtist = connection.prepareStatement(queries.insertArtist());
            insertArtist.setString(1,artistName);
            insertArtist.executeUpdate();
            System.out.println("inserted artist...");


            getArtistID = connection.prepareStatement(queries.getArtistByName());
            getArtistID.setString(1,artistName);
            ResultSet rs = getArtistID.executeQuery();
            System.out.println("fetching artistID...");
            while (rs.next()){
                artistID = rs.getInt("ID");
                System.out.println("got artistID: " + artistID);
            }

            insertAlbum = connection.prepareStatement(queries.insertAlbumQuery());
            insertAlbum.setString(1,title);
            insertAlbum.setString(2,genre);
            insertAlbum.setInt(3,artistID);
            insertAlbum.setInt(4,userID);
            insertAlbum.executeUpdate();
            System.out.println("Inserted Album");
            connection.commit();
        }

        catch (SQLException e){
            if (connection != null){
                try{
                    System.err.print("Transaction is being rolled back");
                    connection.rollback();

                }catch (SQLException except){
                    System.out.println(except.getMessage());
                }
            }

        }
        finally {
            try{
                insertAlbum.close();
                insertArtist.close();
                getArtistID.close();

                connection.setAutoCommit(true);
                connection.close();
                System.out.println("Connection closed");
            }catch (java.sql.SQLException sqlE){ System.out.println(sqlE.getMessage()); }

        }

    }

    /**
     * This method is used if the given artist is already represented in the database. The method will therefor
     * only insert the album.
     * @param title
     * @param genre
     * @param userID
     * @param artistID
     */
    @Override
    public void insertAlbumOnly(String title, String genre, int userID, int artistID) {
        PreparedStatement insertAlbum = null;
        Connection connection = null;

        try {
            connection = setupTheDatabaseConnectionSomehow();

            insertAlbum = connection.prepareStatement(queries.insertAlbumQuery());
            insertAlbum.setString(1,title);
            insertAlbum.setString(2,genre);
            insertAlbum.setInt(3,artistID);
            insertAlbum.setInt(4,userID);
            insertAlbum.executeUpdate();

        }catch(Exception e) {
            javax.swing.JOptionPane.showMessageDialog(null, "Database error, " + e.toString());
        }
        finally {
            try{
                insertAlbum.close();
                connection.close();
                System.out.println("Connection closed");
            }catch (java.sql.SQLException sqlE){ System.out.println(sqlE.getMessage()); }

        }
    }

    /**
     * Creates a new user
     * @param username
     * @param password
     */
    @Override
    public void insertNewUser(String username, String password) {
        PreparedStatement stmt = null;
        Connection connection = null;

        try {
            connection = setupTheDatabaseConnectionSomehow();
            stmt = connection.prepareStatement(queries.insertUser());
            stmt.setString(1,username);
            stmt.setString(2,password);
            stmt.executeUpdate();

        }
        catch(Exception e) {
            javax.swing.JOptionPane.showMessageDialog(null,
                    "Database error, " + e.toString());
        }finally {
            try{
                connection.close();
                stmt.close();
                System.out.println("Connection closed");
            }catch (java.sql.SQLException sqlE){ System.out.println(sqlE.getMessage()); }

        }
    }

    /**
     * This method is used if the given artist is not represented in the database. The method will therefor
     * first insert the artist into the database and after that alter the album.
     * @param albumID
     * @param title
     * @param genre
     * @param artistName
     * @param coverURL
     */
    @Override
    public void alterAlbum(int albumID,String title, String genre, String artistName, String coverURL) {
        PreparedStatement editAlbum = null;
        PreparedStatement getArtistID = null;
        PreparedStatement insertArtist = null;
        int artistID = 0;

        Connection connection = null;


        try {
            connection = setupTheDatabaseConnectionSomehow();
            connection.setAutoCommit(false);

            insertArtist = connection.prepareStatement(queries.insertArtist());
            insertArtist.setString(1,artistName);
            insertArtist.executeUpdate();
            System.out.println("inserted artist...");

            getArtistID = connection.prepareStatement(queries.getArtistByName());
            getArtistID.setString(1,artistName);
            System.out.println("Fetch ArtistID...");
            ResultSet rs = getArtistID.executeQuery();
            while (rs.next()){
                artistID = rs.getInt("ID");
            }


            editAlbum = connection.prepareStatement(queries.editAlbum());
            editAlbum.setString(1,title);
            editAlbum.setString(2,genre);
            editAlbum.setInt(3,artistID);
            editAlbum.setString(4,coverURL);
            editAlbum.setInt(5,albumID);
            editAlbum.executeUpdate();

            connection.commit();

        }catch (SQLException e){
            if (connection != null){
                try{
                    System.err.print("Transaction is being rolled back");
                    connection.rollback();

                }catch (SQLException except){
                    System.out.println(except.getMessage());
                }
            }

        }
        finally {
            try{
                editAlbum.close();
                insertArtist.close();
                getArtistID.close();
                connection.setAutoCommit(true);
                connection.close();
                System.out.println("Connection closed");
            }catch (java.sql.SQLException sqlE){ System.out.println(sqlE.getMessage()); }

        }
    }

    /**
     * This method is used if the given artist is already represented in the database. The method will therefor
     * olnly alter the album.
     * @param albumID
     * @param title
     * @param genre
     * @param artistID
     * @param coverURL
     */
    @Override
    public void alterAlbumOnly(int albumID,String title, String genre, int artistID, String coverURL) {
        PreparedStatement editAlbum = null;
        Connection connection = null;


        try {
            connection = setupTheDatabaseConnectionSomehow();

            editAlbum = connection.prepareStatement(queries.editAlbum());
            editAlbum.setString(1,title);
            editAlbum.setString(2,genre);
            editAlbum.setInt(3,artistID);
            editAlbum.setString(4,coverURL);
            editAlbum.setInt(5,albumID);
            editAlbum.executeUpdate();

        }catch(Exception e){
            javax.swing.JOptionPane.showMessageDialog(null,
                    "Database error, " + e.toString());
        } finally{
            try{
                editAlbum.close();
                connection.close();
                System.out.println("Connection closed");
            }catch (java.sql.SQLException sqlE){ System.out.println(sqlE.getMessage()); }

        }
    }

    /**
     * Deletes album by albumID
     * @param albumID
     */
    @Override
    public void dropAlbum(int albumID) {

        PreparedStatement stmt = null;
        Connection connection = null;

        try {
            connection = setupTheDatabaseConnectionSomehow();
            stmt = connection.prepareStatement(queries.dropAlbumQuery());
            stmt.setInt(1,albumID);
            stmt.executeUpdate();

        }
        catch(Exception e) {
            javax.swing.JOptionPane.showMessageDialog(null,
                    "Database error, " + e.toString());
        }finally {
            try{
                connection.close();
                stmt.close();
                System.out.println("Connection closed");
            }catch (java.sql.SQLException sqlE){ System.out.println(sqlE.getMessage()); }

        }
    }

    /**
     * This method is used if the given director is not represented in the database. The method will therefor
     * first insert the director into the database and after that insert the movie.
     * @param title
     * @param genre
     * @param userID
     * @param directorName
     */
    @Override
    public void insertMovie(String title, String genre, int userID, String directorName) {
        PreparedStatement insertMovie = null;
        PreparedStatement insertDirector = null;
        PreparedStatement getDirectorID = null;
        Connection connection = null;
        int directorID = 0;

        try {
            connection = setupTheDatabaseConnectionSomehow();
            connection.setAutoCommit(false);

            insertDirector = connection.prepareStatement(queries.insertDirector());
            insertDirector.setString(1, directorName);
            insertDirector.executeUpdate();
            System.out.println("Inserted director");

            getDirectorID = connection.prepareStatement(queries.getDirectorByName());
            getDirectorID.setString(1,directorName);
            ResultSet rs =  getDirectorID.executeQuery();
            System.out.println("Getting directorID...");
            while (rs.next()) {
                directorID = rs.getInt("ID");
                System.out.println("Got directorID: "+directorID);
            }


            insertMovie = connection.prepareStatement(queries.insertMovieQuery());
            insertMovie.setString(1,title);
            insertMovie.setString(2,genre);
            insertMovie.setInt(3,directorID);
            insertMovie.setInt(4,userID);
            insertMovie.executeUpdate();
            System.out.println("Inserted movie...");

            connection.commit();

        }
        catch (SQLException e){
            if (connection != null){
                try{
                    System.err.print("Transaction is being rolled back");
                    connection.rollback();

                }catch (SQLException except){
                    System.out.println(except.getMessage());
                }
            }
        }

        finally {
            try{

                getDirectorID.close();
                insertDirector.close();
                insertMovie.close();
                connection.setAutoCommit(true);
                connection.close();

                System.out.println("Connection closed");
            }catch (java.sql.SQLException sqlE){ System.out.println(sqlE.getMessage()); }

        }
    }

    /**
     * This method is used if the given director is already represented in the database. The method will therefor
     * only insert the movie.
     * @param title
     * @param genre
     * @param userID
     * @param directorID
     */
    @Override
    public void insertMovieOnly(String title, String genre, int userID, int directorID) {
        PreparedStatement insertMovie = null;
        Connection connection = null;

        try {
            connection = setupTheDatabaseConnectionSomehow();

            insertMovie = connection.prepareStatement(queries.insertMovieQuery());
            insertMovie.setString(1,title);
            insertMovie.setString(2,genre);
            insertMovie.setInt(3,directorID);
            insertMovie.setInt(4,userID);
            insertMovie.executeUpdate();

        }catch(Exception e){
            javax.swing.JOptionPane.showMessageDialog(null,
                    "Database error, " + e.toString());
        } finally{
            try{
                insertMovie.close();
                connection.close();
                System.out.println("Connection closed");
            }catch (java.sql.SQLException sqlE){ System.out.println(sqlE.getMessage()); }

        }
    }

    /**
     * This method is used if the given director is not represented in the database. The method will therefor
     * first insert the director into the database and after that alter the movie.
     * @param movieID
     * @param title
     * @param genre
     * @param coverURL
     * @param directorName
     */
    @Override
    public void alterMovie(int movieID,String title, String genre, String coverURL, String directorName) {
        PreparedStatement editMovie = null;
        PreparedStatement insertDirector = null;
        PreparedStatement getDirectorID = null;
        Connection connection = null;
        int directorID = 0;

        try {
            connection = setupTheDatabaseConnectionSomehow();
            connection.setAutoCommit(false);

            insertDirector = connection.prepareStatement(queries.insertDirector());
            insertDirector.setString(1,directorName);
            insertDirector.executeUpdate();
            System.out.println("Inserted director");

            getDirectorID = connection.prepareStatement(queries.getDirectorByName());
            getDirectorID.setString(1,directorName);
            ResultSet rs = getDirectorID.executeQuery();
            System.out.println("Getting directorID....");
            while (rs.next()){
                directorID = rs.getInt("ID");
                System.out.println("Got directorID: " + directorID);
            }

            System.out.println(title);
            System.out.println(genre);
            System.out.println(directorID);
            System.out.println(coverURL);
            System.out.println(movieID);

            editMovie = connection.prepareStatement(queries.editMovie());
            editMovie.setString(1,title);
            editMovie.setString(2,genre);
            editMovie.setInt(3,directorID);
            editMovie.setString(4,coverURL);
            editMovie.setInt(5,movieID);
            editMovie.executeUpdate();
            System.out.println("altered movie");

            connection.commit();

        }
        catch(Exception e) {
            if (connection != null) {
                try {
                    System.err.print("Transaction is being rolled back");
                    connection.rollback();

                } catch (SQLException except) {
                    System.out.println(except.getMessage());
                }
            }
        }
        finally {
            try{
                editMovie.close();
                insertDirector.close();
                getDirectorID.close();
                connection.setAutoCommit(true);
                connection.close();
                System.out.println("Connection closed");
            }catch (java.sql.SQLException sqlE){
                System.out.println(sqlE.getMessage());
            }
        }
    }

    /**
     * This method is used if the given director is already represented in the database. The method will therefor
     * only alter the movie.
     * @param movieID
     * @param title
     * @param genre
     * @param directorID
     * @param coverURL
     */
    @Override
    public void alterMovieOnly(int movieID,String title, String genre, int directorID, String coverURL) {
        PreparedStatement stmt = null;
        Connection connection = null;

        try {
            connection = setupTheDatabaseConnectionSomehow();
            stmt = connection.prepareStatement(queries.editMovie());
            stmt.setString(1,title);
            stmt.setString(2,genre);
            stmt.setInt(3,directorID);
            stmt.setString(4,coverURL);
            stmt.setInt(5,movieID);
            stmt.executeUpdate();

        }
        catch(Exception e) {
            javax.swing.JOptionPane.showMessageDialog(null,
                    "Database error, " + e.toString());
        }finally {
            try{
                connection.close();
                stmt.close();
                System.out.println("Connection closed");
            }catch (java.sql.SQLException sqlE){ System.out.println(sqlE.getMessage()); }

        }
    }

    /**
     * Deletes the movie by movieID
     * @param movieID
     */
    @Override
    public void dropMovie(int movieID) {
        PreparedStatement stmt = null;
        Connection connection = null;

        try {
            connection = setupTheDatabaseConnectionSomehow();
            stmt = connection.prepareStatement(queries.dropMovieQuery());
            stmt.setInt(1,movieID);
            stmt.executeUpdate();

        }
        catch(Exception e) {
            javax.swing.JOptionPane.showMessageDialog(null,
                    "Database error, " + e.toString());
        }finally {
            try{
                connection.close();
                stmt.close();
                System.out.println("Connection closed");
            }catch (java.sql.SQLException sqlE){ System.out.println(sqlE.getMessage()); }

        }
    }

    /**
     * Gets the director by name
     * @param name
     * @return Returns directorID
     */
    @Override
    public int getDirectorByName(String name) {
        PreparedStatement stmt = null;
        int id = -1;
        Connection connection = null;

        try {
            connection = setupTheDatabaseConnectionSomehow();
            stmt = connection.prepareStatement(queries.getDirectorByName());
            stmt.setString(1,name);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                id = rs.getInt("ID");
                System.out.println(id);
            }
            //return artistId;
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(null,
                    "Database error, " + e.toString());
        }finally {
            try{
                stmt.close();
                connection.close();
                System.out.println("Connection closed");
            }catch (java.sql.SQLException sqlE){ System.out.println(sqlE.getMessage()); }
        }
        return id;
    }

    /**
     * Gets a user from username
     * @param username
     * @return Returns a user
     */
    @Override
    public User getUser(String username) {
        PreparedStatement stmt = null;
        User user = null;
        Connection connection = null;

        try {
            connection = setupTheDatabaseConnectionSomehow();
            stmt = connection.prepareStatement(queries.getUser());
            stmt.setString(1,username);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                user = new User(rs.getInt("ID"), rs.getString("Username"));
                System.out.println("1 " + user.toString());
            }

        }
        catch(Exception e) {
            javax.swing.JOptionPane.showMessageDialog(null,
                    "Database error, " + e.toString());
        }finally {
            try{
                connection.close();
                stmt.close();
                System.out.println("Connection closed");
            }catch (java.sql.SQLException sqlE){ System.out.println(sqlE.getMessage()); }

        }
        return user;
    }

    /**
     * Authenticates user information
     * @param username
     * @param password
     * @return Returns a user
     */
    @Override
    public User userAuthentication(String username,String password) {
        PreparedStatement stmt = null;
        User user = null;
        Connection connection = null;

        try {
            connection = setupTheDatabaseConnectionSomehow();
            stmt = connection.prepareStatement(queries.authenticateUser());
            stmt.setString(1,username);
            stmt.setString(2,password);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                user = new User(rs.getInt("ID"), rs.getString("Username"));
                System.out.println("1 " + user.toString());
            }

        }
        catch(Exception e) {
            javax.swing.JOptionPane.showMessageDialog(null,
                    "Database error, " + e.toString());
        }finally {
            try{
                connection.close();
                stmt.close();
                System.out.println("Connection closed");
            }catch (java.sql.SQLException sqlE){ System.out.println(sqlE.getMessage()); }

        }
        return user;
    }

    /**
     * Deletes a the review that matches the parameters
     * @param userID
     * @param mediaID
     * @param mediaType
     */
    @Override
    public void deleteReview(int userID, int mediaID, int mediaType) {
        PreparedStatement stmt = null;
        Connection connection = null;

        try {
            connection = setupTheDatabaseConnectionSomehow();
            stmt = mediaType == 1 ? connection.prepareStatement(queries.deleteAlbumReview()) : connection.prepareStatement(queries.deleteMovieReview());
            stmt.setInt(1,userID);
            stmt.setInt(2,mediaID);

            stmt.executeUpdate();
        }
        catch(Exception e) {
            javax.swing.JOptionPane.showMessageDialog(null,
                    "Database error, " + e.toString());
        }finally {
            try{
                connection.close();
                stmt.close();
                System.out.println("Connection closed");
            }catch (java.sql.SQLException sqlE){ System.out.println(sqlE.getMessage()); }

        }
    }

    /**
     * Updates the review specified from the parameters
     * @param userID
     * @param albumID
     * @param date
     * @param text
     * @param rating
     * @param mediaType
     */
    @Override
    public void updateReview(int userID, int albumID, java.sql.Date date, String text, int rating, int mediaType) {
        PreparedStatement stmt = null;
        Connection connection = null;

        try {
            connection = setupTheDatabaseConnectionSomehow();

            stmt = mediaType == 1 ? connection.prepareStatement(queries.updateAlbumReview()) : connection.prepareStatement(queries.updateMovieReview());

            stmt.setDate(1,date);
            stmt.setString(2,text);
            stmt.setInt(3,rating);
            stmt.setInt(4,userID);
            stmt.setInt(5,albumID);

            stmt.executeUpdate();
        }
        catch(Exception e) {
            javax.swing.JOptionPane.showMessageDialog(null,
                    "Database error, " + e.toString());
        }finally {
            try{
                connection.close();
                stmt.close();
                System.out.println("Connection closed");
            }catch (java.sql.SQLException sqlE){ System.out.println(sqlE.getMessage()); }

        }
    }

    /**
     * Gets all the review for a specific albumID
     * @param albumID
     * @return Returns a list of reviews
     */
    @Override
    public ArrayList<Review> getAlbumReviews(int albumID) {
        PreparedStatement stmt = null;
        ArrayList<Review> reviews = new ArrayList<>();
        Connection connection = null;

        try {
            connection = setupTheDatabaseConnectionSomehow();
            stmt = connection.prepareStatement(queries.getAlbumReviews());
            stmt.setInt(1,albumID);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Review tmp = new Review(rs.getDate("RevDate"),rs.getInt("Rating"),rs.getString("Review"), rs.getInt("UserID"),rs.getString("Username"));
                reviews.add(tmp);
            }

        }catch (Exception e){ javax.swing.JOptionPane.showMessageDialog(null,
                "Database error, " + e.toString()); }

        finally {
            if (stmt != null) {
                try{
                    stmt.close();
                    connection.close();
                    System.out.println("Connection closed");
                }catch (java.sql.SQLException sqlE){ System.out.println(sqlE.getMessage()); }
            }
        }

        return reviews;
    }

    /**
     * Gets all the review for a specific movieID
     * @param movieID
     * @return Returns a list of reviews
     */
    @Override
    public ArrayList<Review> getMovieReviews(int movieID) {
        PreparedStatement stmt = null;
        ArrayList<Review> reviews = new ArrayList<>();
        Connection connection = null;

        try {

            connection = setupTheDatabaseConnectionSomehow();
            // Execute the SQL statement
            stmt = connection.prepareStatement(queries.getMovieReviews());
            stmt.setInt(1,movieID);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Review tmp = new Review(rs.getDate("RevDate"),rs.getInt("Rating"),rs.getString("Review"), rs.getInt("UserID"),rs.getString("Username"));
                System.out.println(" 1" + tmp.toString());
                reviews.add(tmp);

            }

        }catch (Exception e){ javax.swing.JOptionPane.showMessageDialog(null,
                "Database error, " + e.toString()); }

        finally {
            if (stmt != null) {
                try{
                    stmt.close();
                    connection.close();
                    System.out.println("Connection closed");
                }catch (java.sql.SQLException sqlE){ System.out.println(sqlE.getMessage()); }
            }
        }

        return reviews;
    }

    /**
     * Gets all the albums from the database
     * @param stmt
     * @return returns a list of albums
     */
    private ObservableList<Album> getAlbumsQuery(PreparedStatement stmt) {
        ObservableList<Album> albums = FXCollections.observableArrayList();
        AlbumGenre tmpGenre = AlbumGenre.OTHER;
        Connection connection = null;

        try {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                for (AlbumGenre ag : AlbumGenre.values()){
                    if (ag.toString().toUpperCase().equals(rs.getString("Genre").toUpperCase())){
                        tmpGenre = ag;
                    }
                }
                Album tmp = new Album(rs.getInt("ID"), rs.getString("Title"),rs.getString("Name"),tmpGenre,rs.getString("CoverUrl"), rs.getString("Username"));
                System.out.println(" 1" + tmp.toString());
                albums.add(tmp);

            }
        }
        catch(Exception e) {
            javax.swing.JOptionPane.showMessageDialog(null,
                    "Database error, " + e.toString());
        }finally {

            if (stmt != null) {
                try{
                    stmt.close();
                }catch (java.sql.SQLException sqlE){ System.out.println(sqlE.getMessage()); }
            }
        }

        return albums;
    }

    /**
     * Gets all the movies from the database
     * @param stmt
     * @return returns a list of movies
     */
    private ObservableList<Movie> getMoviesQuery(PreparedStatement stmt) {

        ObservableList<Movie> movies = FXCollections.observableArrayList();
        MovieGenre tmpGenre = MovieGenre.OTHER;


        try {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                for (MovieGenre ag : MovieGenre.values()){
                    if (ag.toString().toUpperCase().equals(rs.getString("Genre").toUpperCase())){
                        tmpGenre = ag;
                    }
                }
                Movie tmp = new Movie(rs.getInt("ID"), rs.getString("Title"),rs.getString("Name"),tmpGenre,rs.getString("CoverUrl"), rs.getString("Username"));
                System.out.println("1 " + tmp.toString());
                movies.add(tmp);

            }
        }catch(Exception e) {
            javax.swing.JOptionPane.showMessageDialog(null,"Database error, " + e.toString());
        }finally {

            try {
                if (stmt != null){
                    stmt.close();
                }
            }catch (Exception e){
                System.out.println(e);
            }
        }

        return movies;
    }


    /**
     * Sets up the connection to the database
     * @return returns a connection
     */
    private Connection setupTheDatabaseConnectionSomehow(){

        Connection connection = null;

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection(server, username, password);
            System.out.println("Connected!");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return connection;
    }
}
