package models;
import java.util.Date;
/**
 * Created by:
 * Carl-Johan Dahlman, cjda@kth.se
 * Waleed Hassan, waleedh@kth.se
 * on 14/12/15.
 * Model contains data about a review.
 */
public class Review {

    private Date date;
    private int rating;
    private String text;
    private int userID;
    private int albumID;
    private String username;


    public Review(Date date, int rating, String text) {
        this.date = date;
        this.rating = rating;
        this.text = text;
    }
    public Review(Date date, int rating, String text, int userID, String username) {
        this.date = date;
        this.rating = rating;
        this.text = text;
        this.userID = userID;
        this.username = username;
    }

    /**
     *A buckle of
     */
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String toString(){
        String info ="Date: " + date + "\n" +
                        "Rating: " + rating + "\n" +
                        "Review: " + text + "\n" +
                        "By: " + username  + "\n\n";
        return info;
    }

}
