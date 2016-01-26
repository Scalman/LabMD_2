package main;

import controllers.*;
import controllers.LoginRegisterController.LoginController;
import controllers.LoginRegisterController.RegisterController;
import controllers.albumControllers.AddAlbumController;
import controllers.albumControllers.EditAlbumController;
import controllers.albumControllers.ReviewAlbumController;
import controllers.albumControllers.ShowAlbumController;
import controllers.movieControllers.AddMovieController;
import controllers.movieControllers.EditMovieController;
import controllers.movieControllers.ReviewMovieController;
import controllers.movieControllers.ShowMovieController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import models.JDBCDatabase;
import models.Model;

import java.io.IOException;

/**
 * Created by Scalman on 10/12/15.
 */
public class InitFxml {


        public void initializeLoadersAndControllers(Stage primaryStage) throws IOException {

            Model model = new Model();
            DeleteController dCtrl = new DeleteController();
            dCtrl.setModel(model);

            //- - - - - - - - - - - MOVIES - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
            Parent addMovie = null;
            FXMLLoader addMovieLoader = new FXMLLoader();
            addMovie = addMovieLoader.load(getClass().getResource("/fxml/movieFxml/addMovie.fxml").openStream());
            AddMovieController aMCtrl = addMovieLoader.getController();
            if (aMCtrl == null) System.out.println("add movie controllern är null");
            aMCtrl.setParent(addMovie);
            aMCtrl.setModel(model);
            aMCtrl.setChoiceBoxes();

            Parent editMovie = null;
            FXMLLoader editMovieLoader = new FXMLLoader();
            editMovie = editMovieLoader.load(getClass().getResource("/fxml/movieFxml/editMovie.fxml").openStream());
            EditMovieController eMCtrl = editMovieLoader.getController();
            if (eMCtrl == null) System.out.println("edit movie controllern är null");
            eMCtrl.setParent(editMovie);
            eMCtrl.setModel(model);
            eMCtrl.setChoiceBoxes();

            Parent showMovie = null;
            FXMLLoader showMovieLoader = new FXMLLoader();
            showMovie = showMovieLoader.load(getClass().getResource("/fxml/movieFxml/showMovie.fxml").openStream());
            ShowMovieController sMCtrl = showMovieLoader.getController();
            if (sMCtrl == null) System.out.println("edit album controllern är null");
            sMCtrl.setParent(showMovie);
            sMCtrl.setModel(model);

            Parent reviewMovie = null;
            FXMLLoader reviewMovieLoader = new FXMLLoader();
            reviewMovie = reviewMovieLoader.load(getClass().getResource("/fxml/movieFxml/reviewMovie.fxml").openStream());
            ReviewMovieController rMCtrl = reviewMovieLoader.getController();
            if (rMCtrl == null) System.out.println("edit album controllern är null");
            rMCtrl.setParent(reviewMovie);
            rMCtrl.setModel(model);

            //- - - - - - - - - - - ALBUMS - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
            Parent editAlbum = null;
            FXMLLoader editAlbumLoader = new FXMLLoader();
            editAlbum = editAlbumLoader.load(getClass().getResource("/fxml/albumFxml/editRecord.fxml").openStream());
            EditAlbumController eACtrl = editAlbumLoader.getController();
            if (eACtrl == null) System.out.println("edit album controllern är null");
            eACtrl.setParent(editAlbum);
            eACtrl.setModel(model);
            eACtrl.setChoiceBoxes();

            Parent showAlbum = null;
            FXMLLoader showAlbumLoader = new FXMLLoader();
            showAlbum = showAlbumLoader.load(getClass().getResource("/fxml/albumFxml/showAlbum.fxml").openStream());
            ShowAlbumController sACtrl = showAlbumLoader.getController();
            if (sACtrl == null) System.out.println("edit album controllern är null");
            sACtrl.setParent(showAlbum);
            sACtrl.setModel(model);


            Parent addAlbum = null;
            FXMLLoader addAlbumLoader = new FXMLLoader();
            addAlbum = addAlbumLoader.load(getClass().getResource("/fxml/albumFxml/addAlbum.fxml").openStream());
            AddAlbumController aACtrl = addAlbumLoader.getController();
            if (aACtrl == null) System.out.println("add album controllern är null");
            aACtrl.setParent(addAlbum);
            aACtrl.setModel(model);
            aACtrl.setChoiceBoxes();

            Parent reviewAlbum = null;
            FXMLLoader reviewAlbumLoader = new FXMLLoader();
            reviewAlbum = reviewAlbumLoader.load(getClass().getResource("/fxml/albumFxml/reviewAlbum.fxml").openStream());
            ReviewAlbumController rACtrl = reviewAlbumLoader.getController();
            if (rACtrl == null) System.out.println("edit album controllern är null");
            rACtrl.setParent(reviewAlbum);
            rACtrl.setModel(model);

            //- - - - - - - - - - - -main.Main Controller - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
            Parent root = null;
            FXMLLoader loader = new FXMLLoader();
            System.out.println(getClass().getResource("/fxml/main.fxml"));
            root = loader.load(getClass().getResource("/fxml/main.fxml").openStream());
            MainController mCtrl = loader.getController();
            if (mCtrl == null) System.out.println("main controllern är null");
            mCtrl.setPrimaryStage(primaryStage);
            mCtrl.setModel(model);
            mCtrl.setMain(root);
            mCtrl.setControllers(aACtrl, eACtrl, dCtrl, sACtrl, aMCtrl,eMCtrl,sMCtrl,rMCtrl,rACtrl);
            mCtrl.getAlbums();
            mCtrl.showMovies();

            // Model
            model.setMainController(mCtrl);
            //- - - - - - - - - - - -Register Controller - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

            Parent registerNewUser = null;
            FXMLLoader registerNewLoader = new FXMLLoader();
            registerNewUser = registerNewLoader.load(getClass().getResource("/fxml/register.fxml").openStream());
            RegisterController rUCtrl = registerNewLoader.getController();
            if (rUCtrl == null) System.out.println("add album controllern är null");
            rUCtrl.setParent(registerNewUser);
            rUCtrl.setModel(model);

            //- - - - - - - - - - - -Login Controller - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

            Parent login = null;
            FXMLLoader loginLoader = new FXMLLoader();
            login = loginLoader.load(getClass().getResource("/fxml/login.fxml").openStream());
            LoginController lINCtrl = loginLoader.getController();
            if (lINCtrl == null) System.out.println("add album controllern är null");
            lINCtrl.setParent(login);
            lINCtrl.setModel(model);
            lINCtrl.setMainRegisterController(mCtrl,rUCtrl);
            lINCtrl.setPrimaryStage(primaryStage,root);
            lINCtrl.setLoginScene();

        }
}


