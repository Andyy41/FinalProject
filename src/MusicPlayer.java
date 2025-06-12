import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.io.IOException;

public class MusicPlayer {
    private Clip clip;
    private final int skipFrames = 0; // Skipping 0.5 seconds (44100 * 0.5 = 22050 frames)

    // Load audio asynchronously in a separate thread
    public MusicPlayer(String filePath) {
        new Thread(() -> {
            try {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File(filePath));
                clip = AudioSystem.getClip();
                clip.open(audioIn);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start(); // Load the audio file in a separate thread
    }

    // Play the music starting at the specified offset (0.5 seconds skip)
    public void playMusic() {
        if (clip != null && !clip.isRunning()) {
            clip.setFramePosition(skipFrames);  // Skip the first 0.5 seconds
            clip.loop(Clip.LOOP_CONTINUOUSLY);  // Loop the music
            clip.start();  // Start the music immediately
        }
    }

    // Stop the music (if needed)
    public void stopMusic() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }
}
