package assignment_2.HelperClasses;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * sound player
 * Created by martin on 19/04/2015.
 */
public class Sounds {

    private static final String soundFolder = "assignment_2/sounds/";

    public static synchronized void playSound(final String filename) {
        new Thread(() -> {
            try {
                Clip clip = AudioSystem.getClip();
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(
                        Sounds.class.getClassLoader().getResourceAsStream(soundFolder + filename));
                clip.open(audioInputStream);
                clip.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
