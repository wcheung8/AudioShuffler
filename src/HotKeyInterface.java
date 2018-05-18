import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.IntellitypeListener;
import com.melloware.jintellitype.JIntellitype;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Based on JIntellitype's demo file
 */
public class HotKeyInterface extends JFrame implements HotkeyListener, IntellitypeListener {

    public final int NEXT = 0;
    public final int PREV = 1;
    public final int PAUSE = 2;
    public final int QUIT = 3;

    public static Properties prop = new Properties();

    //    public String[] methods = {"NEXT", "PREV", "PAUSE", "QUIT"};
    public static String[] methods = {"NEXT", "PREV", "PAUSE", "QUIT", "VOLUME_UP", "VOLUME_DOWN", "RATE_UP", "RATE_DOWN"};
    public static Map<String, Integer> mapping = new HashMap<>();

    public HotKeyInterface() {


        loadSettings();
        // initialize hotkeys based on properties file
        for (int i = 0; i < methods.length; i++) {
            int modifier = Integer.parseInt(prop.getProperty(methods[i] + "_MODIFIER"));
            int keycode = Integer.parseInt(prop.getProperty(methods[i] + "_KEYCODE"));
            JIntellitype.getInstance().registerHotKey(i, modifier, keycode);
            mapping.put(methods[i], i);
        }

        // set incrementVolume and incrementPlayrate
        player.playrate = Double.parseDouble(prop.getProperty("PLAYRATE"));
        player.volume = Double.parseDouble(prop.getProperty("VOLUME"));

        saveSettings();

    }

    public void onHotKey(int aIdentifier) {
        if (aIdentifier == mapping.get("NEXT")) {
            player.next();
        } else if (aIdentifier == mapping.get("PREV")) {
            player.prev();
        } else if (aIdentifier == mapping.get("PAUSE")) {
            player.pause();
        } else if (aIdentifier == mapping.get("QUIT")) {
            JIntellitype.getInstance().cleanUp();
            player.quit();
        } else if (aIdentifier == mapping.get("VOLUME_UP")) {
            player.incrementVolume(0.05);
        } else if (aIdentifier == mapping.get("VOLUME_DOWN")) {
            player.incrementVolume(-0.05);
        } else if (aIdentifier == mapping.get("RATE_UP")) {
            player.incrementPlayrate(0.05);
        } else if (aIdentifier == mapping.get("RATE_DOWN")) {
            player.incrementPlayrate(-0.05);
        }
    }

    public void onIntellitype(int aCommand) {

    }

    public static void loadSettings() {
        try {
            prop.load(new FileInputStream("./shuffler-settings.ini"));
        } catch (FileNotFoundException e) {
            /* set defaults */

            prop.setProperty("NEXT_MODIFIER", "" + JIntellitype.MOD_CONTROL);
            prop.setProperty("NEXT_KEYCODE", "" + 191);

            prop.setProperty("PREV_MODIFIER", "" + JIntellitype.MOD_CONTROL);
            prop.setProperty("PREV_KEYCODE", "" + 190);

            prop.setProperty("PAUSE_MODIFIER", "" + JIntellitype.MOD_CONTROL);
            prop.setProperty("PAUSE_KEYCODE", "" + 188);

            prop.setProperty("QUIT_MODIFIER", "" + 0);
            prop.setProperty("QUIT_KEYCODE", "" + 112);

            prop.setProperty("VOLUME_UP_MODIFIER", "" + JIntellitype.MOD_CONTROL);
            prop.setProperty("VOLUME_UP_KEYCODE", "" + 38);

            prop.setProperty("VOLUME_DOWN_MODIFIER", "" + JIntellitype.MOD_CONTROL);
            prop.setProperty("VOLUME_DOWN_KEYCODE", "" + 40);

            prop.setProperty("RATE_UP_MODIFIER", "" + JIntellitype.MOD_CONTROL);
            prop.setProperty("RATE_UP_KEYCODE", "" + 39);

            prop.setProperty("RATE_DOWN_MODIFIER", "" + JIntellitype.MOD_CONTROL);
            prop.setProperty("RATE_DOWN_KEYCODE", "" + 37);

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
            prop.store(new FileOutputStream("./shuffler-settings.ini"), "AudioShuffler Settings");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setHotkey(int method, int modifier, int keycode) {
        JIntellitype.getInstance().unregisterHotKey(method);
        JIntellitype.getInstance().registerHotKey(method, modifier, keycode);
    }

    /**
     * Initialize the JInitellitype library making sure the DLL is located.
     */
    public void initJIntellitype() {
        try {
            JIntellitype.getInstance().addHotKeyListener(this);
            JIntellitype.getInstance().addIntellitypeListener(this);
        } catch (RuntimeException e) {
            System.out.println("Either you are not on Windows, or there is a problem with the JIntellitype library!");
        }
    }
/*
    public void registerHotkey(Object hotkeyId, int modifiers, int keyCode) {
        if (!initialized) {
            return;
        }
        try {
            int id = getId(hotkeyId);
            int mod = getModFromModifiers(modifiers);
            // Remove previously under this id registered hotkey (if there is
            // any)
            JIntellitype.getInstance().unregisterHotKey(id);
            LOGGER.info("[Global Hotkeys] Trying to register hotkey: " + id + "/" + mod + "/" + keyCode);
            JIntellitype.getInstance().registerHotKey(id, mod, keyCode);
            hotkeys.put(id, hotkeyId);
        } catch (JIntellitypeException ex) {
            LOGGER.info("[Global Hotkeys] Couldn't register hotkey: " + ex);
        }
    }
*/
}