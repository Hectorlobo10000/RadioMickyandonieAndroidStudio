package library.radiomickyandonie;

import android.net.Uri;

import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;

import java.util.Hashtable;

/**
 * Created by lobo on 03-21-18.
 */

public class Spanish implements IChannel {
    private MediaPlayer mediaPlayer;
    private LibVLC libVLC;
    private Media media;

    public Spanish(Hashtable hashtable) {
        this.mediaPlayer = (MediaPlayer) hashtable.get("player");
        this.libVLC = (LibVLC) hashtable.get("libVLC");
        this.media = new Media(this.libVLC, Uri.parse("http://167.114.13.24:9968/"));
    }

    @Override
    public void prepare() {
        if(mediaPlayer.getMedia() != media) {
            mediaPlayer.stop();
            mediaPlayer.setMedia(media);
        }
    }

    @Override
    public void start() {
        mediaPlayer.play();
    }

    @Override
    public void stop() {
        mediaPlayer.stop();
    }

    @Override
    public void release() {
        mediaPlayer.stop();
        mediaPlayer.release();
    }
}
