import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class menuController {

    @FXML
    public Slider progress;

    @FXML
    public Text current;

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

    @FXML
    public TableView<Action> action_list;

    @FXML
    private TableColumn<Action, String> action;

    @FXML
    private TableColumn<Action, String> keybind;

    @FXML
    public TabPane pane;

    public EventHandler<KeyEvent> bindHotkey = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {

            if (event.getCode().isModifierKey() || action_list.getSelectionModel().getSelectedItem() == null)
                return;

            String s = event.getCode().toString();
            if (event.isControlDown())
                s = "CTRL+" + s;
            if (event.isAltDown())
                s = "ALT+" + s;
            if (event.isShiftDown())
                s = "SHIFT+" + s;
            if (event.isMetaDown())
                s = "WIN+" + s;
            System.out.println(s);

            HotKeyInterface.bind(action_list.getSelectionModel().getSelectedItem(), s);
            action_list.getSelectionModel().clearSelection();
            action_list.refresh();
            HotKeyInterface.saveSettings();
            pane.setOnKeyPressed(e -> e.consume());

            HotKeyInterface.bindAll();
        }
    };

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

        // initialize action list
        this.action_list.setItems(FXCollections.observableList(HotKeyInterface.actions));
        action.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().name));
        keybind.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().keybind));

    }

    public void show() {
        Stage mainStage = menuGUI.mainStage;
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        //set Stage boundaries to the lower right corner of the visible bounds of the main screen
        mainStage.setX(primaryScreenBounds.getMinX() + primaryScreenBounds.getWidth() - 600);
        mainStage.setY(primaryScreenBounds.getMinY() + primaryScreenBounds.getHeight() - 430);
        mainStage.getIcons().add(new Image(getClass().getResourceAsStream("resources/player.png")));
        mainStage.setTitle("AudioShuffler");
        mainStage.show();

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

    public final void handleBind() {
        HotKeyInterface.unbindAll();
        pane.setOnKeyPressed(bindHotkey);
    }


    public void refresh() {
        volume.setValue(player.volume);
        String v = "" + player.volume;
        volume_text.setText(v.substring(0, Math.min(v.length(), 4)));

        playrate.setValue(player.playrate);
        v = "" + player.playrate;
        playrate_text.setText(v.substring(0, Math.min(v.length(), 4)));

    }

    public void setTitle(String s) {
        current.setText(s);
    }


    // TODO seeking
}




