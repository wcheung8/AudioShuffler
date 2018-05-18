import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class Song {

    static int n;

    MediaPlayer media;
    String title;
    String artist;
    int index;

    public Song(File f) {

        if (f.isFile() && (f.getName().endsWith(".mp3") || f.getName().endsWith(".m4a"))) {

            this.media = new MediaPlayer(new Media(f.toURI().toString()));
            media.setOnEndOfMedia(
                    new Runnable() {
                        public void run() {
                            player.next();
                        }
                    });

            this.title = f.getName().replace(".mp3", "").replace(".m4a", "");
            this.index = n;
            n++;
            
        }


    }

    public String toString() {
        return this.title;
    }


}

