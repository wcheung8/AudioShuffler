import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class menuController {

    @FXML
    public Slider progress;

    @FXML
    public Slider volume;

    @FXML
    public Text volume_text;

    @FXML
    public Slider playrate;

    @FXML
    public Text playrate_text;

    @FXML
    public ListView<Song> song_list;

    public void initialize() {

        // progress bar slider listener
        this.progress.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                                Number oldValue, Number newValue) {

                System.out.println("Slider Value Changed (newValue: " + newValue.intValue() + ")\n");
            }
        });

        // volume slider listener
        this.volume.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                                Number oldValue, Number newValue) {

                player.setVolume(newValue.doubleValue());
                String v = "" + newValue.doubleValue();
                volume_text.setText(v.substring(0, Math.min(v.length(), 4)));
            }
        });

        // playrate slider listener
        this.playrate.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                                Number oldValue, Number newValue) {

                player.setPlayrate(newValue.doubleValue());
                String v = "" + newValue.doubleValue();
                playrate_text.setText(v.substring(0, Math.min(v.length(), 4)));
            }
        });


        // songlist click listener
        this.song_list.setItems(FXCollections.observableList(player.songs));
        this.song_list.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("clicked on " + song_list.getSelectionModel().getSelectedItem());
                player.playSong(song_list.getSelectionModel().getSelectedItem());
            }
        });

        //initialize volume and playrate sliders/text
        volume.setValue(player.volume);
        playrate.setValue(player.playrate);

        String v = "" + player.volume;
        volume_text.setText(v.substring(0, Math.min(v.length(), 4)));

        v = "" + player.playrate;
        playrate_text.setText(v.substring(0, Math.min(v.length(), 4)));

    }

    public menuController show() {

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(menuGUI.class.getResource("resources/audio-menu.fxml"));

            Parent root = loader.load();
            Stage mainStage = menuGUI.mainStage;
            Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
            //set Stage boundaries to the lower right corner of the visible bounds of the main screen
            mainStage.setX(primaryScreenBounds.getMinX() + primaryScreenBounds.getWidth() - 600);
            mainStage.setY(primaryScreenBounds.getMinY() + primaryScreenBounds.getHeight() - 430);
            mainStage.setScene(new Scene(root));
            mainStage.getIcons().add(new Image(getClass().getResourceAsStream("resources/player.png")));
            mainStage.setTitle("AudioShuffler");
            mainStage.show();

            return loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public final void handleNextPress() {
        player.next();
    }

    public final void handlePrevPress() {
        player.prev();
    }

    public final void handlePausePress() {
        player.pause();
    }

    // TODO song selection

    // TODO seeking
}




