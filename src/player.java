import com.melloware.jintellitype.JIntellitype;
import com.melloware.jintellitype.JIntellitypeException;
import javafx.application.Application;
import javafx.embed.swing.JFXPanel;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class player {

    static Song current;
    static ArrayList<Song> songs = new ArrayList<Song>();
    static int[] indexToDefault;
    static int[] defaultToIndex;
    static int currentTrack;

    static boolean paused = false;
    static boolean muted = false;
    static double volume = 0.5;
    static double playrate = 1.0;

    public static void main(String[] args) throws IOException {
        JFXPanel fxPanel = new JFXPanel();
        trayGUI stage = new trayGUI();

        stage.createAndShowGUI();

        try {
            JIntellitype.getInstance();
        } catch (JIntellitypeException e) {
            JOptionPane.showMessageDialog(null, "AudioShuffler already running!");
            System.exit(0);
        }

        // next check to make sure JIntellitype DLL can be found and we are on a Windows operating System
        if (!JIntellitype.isJIntellitypeSupported()) {
            JOptionPane.showMessageDialog(null, "JIntellitype not supported!");
            quit();
        }

        HotKeyInterface mainFrame = new HotKeyInterface();
        mainFrame.setTitle("AudioShuffler");
        mainFrame.setVisible(false);
        mainFrame.initJIntellitype();

        // extract files from current directory
        File folder = new File(System.getProperty("user.dir"));

        // TODO load all songs at once -> dynamic loading
        for (File f : folder.listFiles()) {
            if (f.isFile() && (f.getName().endsWith(".mp3") || f.getName().endsWith(".m4a"))) {
                // add song and title to queue
                songs.add(new Song(f));
            }
        }

        if (songs.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No songs to play!");
            quit();
        }

        // set next track to play
        shuffle();

        // get and play first track
        next();

        Application.launch(menuGUI.class);

    }

    public static void playSong(Song s) {

        current.media.stop();
        current = s;
        current.media.play();
        currentTrack = defaultToIndex[current.index];

    }

    public static void shuffle() {

        indexToDefault = new int[songs.size()];
        defaultToIndex = new int[songs.size()];
        for (int i = 0; i < indexToDefault.length; i++) {
            indexToDefault[i] = i;
        }

        // use Fisher-Yates shuffle to select for circular linked
        Random rand = new Random();
        for (int i = indexToDefault.length - 1; i >= 0; i--) {
            int temp = indexToDefault[i];
            int random = i - rand.nextInt(i + 1);
            indexToDefault[i] = indexToDefault[random];
            defaultToIndex[indexToDefault[random]] = i;
            indexToDefault[random] = temp;
        }
    }

    public static void normal() {

        indexToDefault = new int[songs.size()];
        for (int i = 0; i < indexToDefault.length; i++) {
            indexToDefault[i] = i;
            defaultToIndex[i] = i;
        }

    }

    public static int getindexToDefault() {

        if (currentTrack < songs.size() - 1) {
            currentTrack++;
        } else {
            currentTrack = 0;
        }
        return indexToDefault[currentTrack];

    }

    public static int getPrevTrack() {

        if (currentTrack > 0) {
            currentTrack--;
        } else {
            currentTrack = songs.size() - 1;
        }

        return indexToDefault[currentTrack];

    }

    public static void next() {

        // pause current track
        if (current != null)
            current.media.stop();

        // switch to next
        current = songs.get(getindexToDefault());

        // update rate and incrementVolume
        current.media.setRate(playrate);
        current.media.setVolume(volume);

        // update tooltips and slider bar
        trayGUI.trayIcon.setToolTip("Now Playing: " + current.title);

        //play song
        current.media.play();


    }

    public static void pause() {

        if (paused = !paused) {
            current.media.pause();
        } else {
            current.media.play();
        }

    }

    public static void prev() {
        // pause current track
        if (current != null)
            current.media.stop();

        // switch to next
        current = songs.get(getPrevTrack());

        // update rate and incrementVolume
        current.media.setRate(playrate);
        current.media.setVolume(volume);

        // update tooltips and slider bar
        trayGUI.trayIcon.setToolTip("Now Playing: " + current.title);

        //play song
        current.media.play();
    }

    public static void mute() {
        current.media.setMute(!muted);
        muted = !muted;
    }

    public static void quit() {
        JIntellitype.getInstance().cleanUp();
        HotKeyInterface.saveSettings();
        System.exit(0);
    }

    public static void incrementPlayrate(double change) {
        if (playrate + change > 0 && playrate + change < 2.0)
            playrate += change;
        current.media.setRate(playrate);
        HotKeyInterface.saveSettings();
    }

    public static void incrementVolume(double change) {
        if (volume + change > 0 && volume + change < 1.0)
            volume += change;
        current.media.setVolume(volume);
        HotKeyInterface.saveSettings();
    }

    public static void setPlayrate(double newValue) {
        if (newValue > 0 && newValue < 2.0) {
            current.media.setRate(newValue);
            playrate = newValue;
            HotKeyInterface.saveSettings();
        }
    }

    public static void setVolume(double newValue) {
        if (newValue > 0 && newValue < 1.0) {
            current.media.setVolume(newValue);
            volume = newValue;
            HotKeyInterface.saveSettings();
        }
    }

}
