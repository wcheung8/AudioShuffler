import javafx.application.Platform;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;

public class trayGUI {

    static PopupMenu popup = new PopupMenu();
    static TrayIcon trayIcon;
    static SystemTray tray = SystemTray.getSystemTray();

    public void createAndShowGUI() throws IOException {
        // Check the SystemTray support
        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            return;
        }
        trayIcon = new TrayIcon(ImageIO.read(getClass().getResource(("resources/player.png"))));
        trayIcon.setImageAutoSize(true);

        // Create a popup menuGUI components
        MenuItem pause = new MenuItem("Play/Pause");
        MenuItem prev = new MenuItem("Previous");
        MenuItem next = new MenuItem("Next");
        CheckboxMenuItem mute = new CheckboxMenuItem("Mute");
        MenuItem exitItem = new MenuItem("Exit");

        // Add components to popup menuGUI
        popup.add(pause);
        popup.add(mute);
        popup.addSeparator();
        popup.add(prev);
        popup.add(next);
        popup.addSeparator();
        popup.add(exitItem);

        trayIcon.setPopupMenu(popup);

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            player.quit();
        }

        trayIcon.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Platform.runLater(new Runnable() {
                    public void run() {
                        menuGUI.controller.show();
                    }
                });
            }
        });

        pause.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                player.pause();
            }
        });

        mute.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                player.mute();
            }
        });

        prev.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                player.prev();
            }
        });

        next.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                player.next();
            }
        });

        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                player.quit();
            }
        });
    }

}
