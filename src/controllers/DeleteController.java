/**
 * Created by:
 * Carl-Johan Dahlman, cjda@kth.se
 * Waleed Hassan, waleedh@kth.se
 * on 14/12/15.
 */
package controllers;

import models.Album;
import models.Model;
import models.Movie;

public class DeleteController {
    private Model model;

    public void setModel(Model model){
        this.model = model;
    }

    public void deleteAlbum(Album selectedAlbum){
        if (selectedAlbum.getAlbumID() >=0){
            model.deleteAlbum(selectedAlbum.getAlbumID());
        }
    }
    public void deleteMovie(Movie selectedMovie){
        if (selectedMovie.getMovieID() >=0){

            model.deleteMovie(selectedMovie.getMovieID());
        }
    }
}
