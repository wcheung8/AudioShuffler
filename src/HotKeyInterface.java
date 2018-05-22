import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.IntellitypeListener;
import com.melloware.jintellitype.JIntellitype;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Based on JIntellitype's demo file
 */
public class HotKeyInterface extends JFrame implements HotkeyListener, IntellitypeListener {


    public static Properties prop = new Properties();

    public static ArrayList<Action> actions = new ArrayList<Action>();
    public static Map<Integer, Action> idToAction = new HashMap<>();
    public static Map<String, Action> keyToAction = new HashMap<>();

    public HotKeyInterface() {


        loadSettings();

        // initialize hotkeys based on properties file
        actions.add(new Action("NEXT", () -> {
            player.next();
        }));
        actions.add(new Action("PREV", () -> {
            player.prev();
        }));
        actions.add(new Action("PAUSE", () -> {
            player.pause();
        }));
        actions.add(new Action("QUIT", () -> {
            player.quit();
        }));
        actions.add(new Action("VOLUME_UP", () -> {
            player.incrementVolume(0.1);
        }));
        actions.add(new Action("VOLUME_DOWN", () -> {
            player.incrementVolume(-0.1);
        }));
        actions.add(new Action("PLAYRATE_UP", () -> {
            player.incrementPlayrate(0.2);
        }));
        actions.add(new Action("PLAYRATE_DOWN", () -> {
            player.incrementPlayrate(-0.2);
        }));

        // set incrementVolume and incrementPlayrate
        player.playrate = Double.parseDouble(prop.getProperty("PLAYRATE"));
        player.volume = Double.parseDouble(prop.getProperty("VOLUME"));

    }

    public void onHotKey(int aIdentifier) {
        System.out.println(idToAction.get(aIdentifier).name);
        idToAction.get(aIdentifier).act.run();
    }

    public void onIntellitype(int aCommand) {

    }

    public static void loadSettings() {
        try {
            prop.load(new FileInputStream("./audio-settings.ini"));
        } catch (FileNotFoundException e) {

            /* set defaults */
            prop.setProperty("NEXT", "CTRL+SLASH");
            prop.setProperty("PREV", "CTRL+PERIOD");
            prop.setProperty("PAUSE", "CTRL+COMMA");
            prop.setProperty("QUIT", "F1");
            prop.setProperty("VOLUME_UP", "CTRL+UP");
            prop.setProperty("VOLUME_DOWN", "CTRL+DOWN");
            prop.setProperty("PLAYRATE_UP", "CTRL+RIGHT");
            prop.setProperty("PLAYRATE_DOWN", "CTRL+LEFT");

            prop.setProperty("VOLUME", "" + 0.5);
            prop.setProperty("PLAYRATE", "" + 1.0);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void saveSettings() {
        try {
            prop.setProperty("VOLUME", "" + player.volume);
            prop.setProperty("PLAYRATE", "" + player.playrate);
            prop.store(new FileOutputStream("./audio-settings.ini"), "AudioShuffler Settings");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initJIntellitype() {
        try {
            JIntellitype.getInstance().addHotKeyListener(this);
            JIntellitype.getInstance().addIntellitypeListener(this);
        } catch (RuntimeException e) {
            System.out.println("Either you are not on Windows, or there is a problem with the JIntellitype library!");
        }
    }

    // TODO recompile jintellitype to have corrected keycodes (PERIOD)
    public static void bind(Action a, String hotkey) {
        try {
            if (keyToAction.containsKey(hotkey)) {
                Action r = keyToAction.get(hotkey);
                unbind(r);
            }
            JIntellitype.getInstance().unregisterHotKey(a.id);
            if (hotkey.contains("PERIOD"))
                JIntellitype.getInstance().registerHotKey(a.id, JIntellitype.MOD_CONTROL, 190);
            else
                JIntellitype.getInstance().registerHotKey(a.id, hotkey);
            prop.setProperty(a.name, hotkey);

            idToAction.put(a.id, a);
            keyToAction.put(hotkey, a);
            a.keybind = hotkey;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void unbind(Action s) {

        if (s.keybind.equals(""))
            return;

        prop.remove(s.name);
        keyToAction.remove(s.keybind);
        idToAction.remove(s.id);
        JIntellitype.getInstance().unregisterHotKey(s.id);
        s.keybind = "";

        saveSettings();

    }

    public static void bindAll() {
        for (Action s : HotKeyInterface.actions) {
            if (!s.keybind.equals(""))
                JIntellitype.getInstance().registerHotKey(s.id, s.keybind);
        }
    }

    public static void unbindAll() {
        for (Action s : HotKeyInterface.actions) {
            JIntellitype.getInstance().unregisterHotKey(s.id);
        }
    }

}