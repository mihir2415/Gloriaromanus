package unsw.gloriaromanus;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;


/*
Code inspired from https://www.youtube.com/watch?v=TErboGLHZGA 
author Max O'Didily 
*/
public class BackgroundMusic {

    private Clip clip;

    /**
     * 
     * @param musicPath is the path through which music is added
     */
    public BackgroundMusic(String musicPath) {
        try {
            File musicFile = new File(musicPath);
            if(musicFile.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicFile);
                clip = AudioSystem.getClip();
                clip.open(audioInput);
                setVolume(-15);
            }
        } catch (Exception e) {
            System.out.println("Can't find file!");
        }
    }

    /**
     * Plays the music
     */
    public void play() {

        try {
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (NullPointerException e) {
            System.out.println("Clip is NULL!!");
        }
        
    }

    /**
     * Stops the music
     */
    public void stop() {
        try {
            clip.stop();
        } catch (NullPointerException e) {
            System.out.println("Clip is NULL!!");
        }
    }

    /**
     * 
     * @param value the volume to set it at
     */
    public void setVolume(float value) {
        FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        volume.setValue(value);
    }
}
