package models;

import enums.AlbumGenre;

import java.util.ArrayList;

/**
 * Created by:
 * Carl-Johan Dahlman, cjda@kth.se
 * Waleed Hassan, waleedh@kth.se
 * on 14/12/15.
 * Model contains data about albums.
 */
public class Album {
    private String date;
    private String title;
    private int albumID;
    private AlbumGenre genre;
    private double rating;
    private String releaseDate;
    private String artist;
    private String noOfSongs;
    private String username;
    private String coverUrl;


    public Album(int albumID, String title, String artist, AlbumGenre genre, String coverUrl, String username){
        this.albumID = albumID;
        this.title = title;
        this.genre = genre;
        this.artist = artist;
        this.coverUrl = coverUrl;
        this.username = username;
    }

    /**
     * A buckle of getters and setters.
     */
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getArtist() {
        return artist;
    }
    public void setArtist(String artist) {
        this.artist = artist;
    }
    
    public AlbumGenre getGenre() {
        return genre;
    }
    public void setGenre(AlbumGenre genre) {
        this.genre = genre;
    }

    public double getRating() {
        return rating;
    }
    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCoverUrl() {
        return coverUrl;
    }
    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getRealeseDate() {
        return releaseDate;
    }
    public void setRealeseDate(String realeseDate) {
        this.releaseDate = releaseDate;
    }

    public String getNoOfSongs() {
        return noOfSongs;
    }
    public void setNoOfSongs(String noOfSongs) {
        this.noOfSongs = noOfSongs;
    }

    public int getAlbumID() {
        return albumID;
    }

    public String toString(){
        String info ="Title: "+ title +
                        "Artist: " + artist +
                        "Genre: " + genre +
                        "CoverUrl: " + coverUrl;
        return info;
    }
}
