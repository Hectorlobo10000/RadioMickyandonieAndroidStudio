package library.radiomickyandonie;

import com.example.lobo.radiomickyandonie.MainActivity;

import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.MediaPlayer;

import java.util.Hashtable;

/**
 * Created by lobo on 03-26-18.
 */

public class Player {
    public static Hashtable getInstance(MainActivity activity) {
        final LibVLC libVLC = new LibVLC(activity);
        final MediaPlayer mediaPlayer = new MediaPlayer(libVLC);
        return new Hashtable<String, Object>(){{
            put("libVLC", libVLC);
            put("player", mediaPlayer);
        }};
    }
}
