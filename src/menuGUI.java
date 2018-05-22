import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class menuGUI extends Application {

    /**
     * Where all the FX stuff is displayed.
     */
    public static Stage mainStage;
    static menuController controller;

    @Override
    public void start(Stage stage) throws Exception {
        //Set up instance instead of using static load() method
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(player.class.getResource("resources/audio-menu.fxml"));
        Parent root = loader.load();

        //Now we have access to getController() through the instance... don't forget the type cast
        controller = loader.getController();

        mainStage = stage;
        Scene scene = new Scene(root);


        Platform.setImplicitExit(false);
        stage.setScene(scene);
        controller.initialize();
    }

    public static void main(String[] args) {
        launch(args);
    }


}
