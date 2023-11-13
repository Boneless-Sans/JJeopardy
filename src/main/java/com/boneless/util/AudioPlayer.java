package com.boneless.util;

import javax.sound.sampled.*;
import java.io.File;

public class AudioPlayer {
    // Remove the 'player' instance and make the play method static.
    public static void play(String fileName) {
        // Add "src/resource/assets" before the file name.
        String fullPath = "src/main/resources/assets/audio/" + fileName;

        try {
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(new File(fullPath)));
            clip.start();
        } catch (Exception exc) {
            exc.printStackTrace(System.out);
        }
    }
}
