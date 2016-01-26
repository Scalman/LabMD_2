/**
 * Created by:
 * Carl-Johan Dahlman, cjda@kth.se
 * Waleed Hassan, waleedh@kth.se
 * on 14/12/15.
 */
package models;

public class SQLQueries {

    public final String getAllAlbums = "Select Album.ID, Album.Title, Artist.Name, Album.Genre, Album.CoverUrl, User.Username " +
                                        "From Album,Artist,User " +
                                        "Where Album.ArtistID = Artist.ID AND Album.UserID = User.ID;";

    public final String getAllMovies = "Select Movie.ID, Movie.Title, Director.Name, Movie.Genre, Movie.CoverUrl, User.Username " +
            "From Movie,Director,User " +
            "WHERE Movie.DirectorID = Director.ID AND Movie.UserID = User.ID;";

    public String insertAlbumQuery(){
        return "INSERT INTO ALBUM (Title,Genre,artistID,UserID) " +
                "VALUES(?,?,?,?)";

    }

    public String dropAlbumQuery(){
        return "DELETE FROM ALBUM WHERE ALBUM.ID = ?";

    }

    public String insertMovieQuery(){
        return "INSERT INTO Movie (Title,Genre,DirectorID, UserID) " +
                "VALUES(?,?,?,?)";

    }

    public String dropMovieQuery(){
        return "DELETE FROM MOVIE WHERE MOVIE.ID = ?";

    }

    public String getArtistByName(){
        return "SELECT ID FROM Artist WHERE Artist.name = ?";

    }
    public String getDirectorByName(){
        return "SELECT ID FROM Director WHERE Director.name = ?";

    }

    public String insertDirector(){

        return "INSERT INTO Director(name) VALUES(?)";
    }

    public String insertArtist(){

        return "INSERT INTO Artist(name) VALUES(?)";
    }

    public String authenticateUser(){
        return  "SELECT ID,Username FROM USER WHERE Username = ? AND Password = ?";
    }

    public String getUser(){
        return  "SELECT ID,Username FROM USER WHERE Username = ?";
    }

    //// TODO: 01/01/16
    public String albumAlreadyReviewed(){
        return  "SELECT * FROM AlbumReview WHERE userID = ? AND albumID = ?";
    }

    public String movieAlreadyReviewed(){
        return  "SELECT UserId,MovieID FROM MovieReview WHERE userID = ? AND movieID = ?";
    }

    public String insertUser(){
        return "INSERT INTO User(username,password) VALUES(?,?)";
    }

    public String editAlbum(){
        return  "UPDATE Album SET Title = ?, Genre = ?,ArtistID = ?, CoverURL = ? WHERE id = ? ";
    }

    public String editMovie(){
        return  "UPDATE Movie SET Title = ?, Genre = ?,DirectorID = ?, CoverURL = ? WHERE id = ?";
    }

    public String addReviewAlbum(){

        return "INSERT INTO AlbumReview(UserID,AlbumID,RevDate,Review,Rating) VALUES(?,?,?,?,?)";
    }
    public String updateAlbumReview(){
        return "UPDATE AlbumReview SET RevDate = ?, Review = ?, Rating = ? WHERE UserID = ? AND AlbumID = ? ";
    }
    public String deleteAlbumReview(){
        return "DELETE FROM AlbumReview WHERE UserID = ? AND AlbumID = ? ";
    }
    public String getAlbumReviews(){
        return "SELECT RevDate,Rating,Review, AlbumReview.UserID, AlbumID, Username FROM AlbumReview,User WHERE AlbumID = ? AND AlbumReview.UserID = User.ID";
    }
    public String getAlbumRating(){
        return "SELECT avg(Rating) FROM AlbumReview WHERE AlbumID = ?";
    }
    public String getMovieRating(){
        return "SELECT avg(Rating) FROM MovieReview WHERE MovieID = ?";
    }
    public String updateMovieReview(){
        return "UPDATE MovieReview SET RevDate = ?, Review = ?, Rating = ? WHERE UserID = ? AND MovieID = ? ";
    }
    public String deleteMovieReview(){
        return "DELETE FROM MovieReview WHERE UserID = ? AND MovieID = ? ";
    }

    public String searchAlbums(int item){
        if (item == 1){
            return "Select Album.ID, Album.Title, Artist.Name, Album.Genre, Album.CoverUrl,User.Username " +
                    "From Album,Artist,User " +
                    "Where Album.ArtistID = Artist.ID " +
                    "AND Album.title LIKE ? AND Album.UserID = User.ID;";
        }
        if (item == 2){
            return "Select Album.ID, Album.Title, Artist.Name, Album.Genre, Album.CoverUrl, User.Username " +
                    "From Album,Artist,User " +
                    "Where Album.ArtistID = Artist.ID " +
                    "AND Artist.Name LIKE ? AND Album.UserID = User.ID;";
        }
        return "FEL";
    }

    public String searchMovies(int item){

        if (item == 1){
            return "Select Movie.ID, Movie.Title, Director.Name, Movie.Genre, Movie.CoverUrl,User.Username " +
                    "From Movie,Director,User " +
                    "Where Movie.DirectorID = Director.ID " +
                    "AND Director.Name LIKE ? AND Movie.UserID = User.ID;";
        }
        if (item == 2){
            return "Select Movie.ID, Movie.Title, Director.Name, Movie.Genre, Movie.CoverUrl, User.Username " +
                    "From Movie,Director,User " +
                    "Where Movie.DirectorID = Director.ID " +
                    "AND Movie.Genre LIKE ? AND Movie.UserID = User.ID;";
        }
        if (item == 3){

            return "Select Movie.ID, Movie.Title, Director.Name, Movie.Genre, Movie.CoverUrl, User.Username, avg(MovieReview.Rating) " +
                    "From Movie,Director,User,MovieReview " +
                    "Where Movie.DirectorID = Director.ID " +
                    "AND Movie.ID = MovieReview.MovieID " +
                    "AND ? <= (SELECT avg(Rating) FROM MovieReview Where Movie.ID = MovieReview.MovieID GROUP BY Movie.ID) " +
                    "AND ? > (SELECT avg(Rating) FROM MovieReview Where Movie.ID = MovieReview.MovieID GROUP BY Movie.ID) " +
                    "AND Movie.UserID = User.ID " +
                    "GROUP BY Movie.ID;";
        }
        return "FEL";
    }
    public String getMovieReviews(){
        return "SELECT RevDate,Rating,Review, MovieReview.UserID, MovieID, Username FROM MovieReview,User WHERE MovieID = ? AND MovieReview.UserID = User.ID";
    }

    public String addReviewMovie(){
        return "INSERT INTO MovieReview(UserID,MovieID,RevDate,Review,Rating) VALUES(?,?,?,?,?)";
    }

}
