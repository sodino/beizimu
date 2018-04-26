package bei.zi.mu.util

import android.media.MediaPlayer

/**
 * Created by sodino on 2018/4/23.
 */
public class MP3Player {
    companion object {
        private val player : MediaPlayer by lazy { MediaPlayer() }

        @Synchronized fun play(mp3Path : String) {
            if (player.isPlaying) {
                player.stop()
            }
            player.reset()
            player.setDataSource(mp3Path)
            player.prepare()
            player.start()
        }

    }
}