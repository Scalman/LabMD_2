package models;

import enums.MovieGenre;
import java.util.ArrayList;

/**
 * Created by:
 * Carl-Johan Dahlman, cjda@kth.se
 * Waleed Hassan, waleedh@kth.se
 * on 14/12/15.
 * Model contains data about Movie.
 */
public class Movie {

    private String title;
    private String director;
    private MovieGenre genre;
    private double rating;
    private int movieID;
    private String coverUrl;
    private ArrayList<Review> reviews;
    private String username;

    public Movie (int movieID,String title, String director, MovieGenre genre,String coverUrl, String username){
        this.movieID = movieID;
        this.title = title;
        this.director = director;
        this.genre = genre;
        this.coverUrl = coverUrl;
        this.username = username;
        reviews = new ArrayList<>();
    }

    /**
     *A buckle of getters and setters.
     */
    public void addReviews(Review review) {
        this.reviews.add(review);
    }

    public int getMovieID() {
        return movieID;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public MovieGenre getGenre() {
        return genre;
    }

    public void setGenre(MovieGenre genre) {
        this.genre = genre;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }


    public String getUsername() {
        return username;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public String toString(){
        String info ="Title: " + title +
                        "Director: " + director +
                        "Genre: " + genre +
                        "CoverUrl: " + coverUrl;
        return info;
    }

}
