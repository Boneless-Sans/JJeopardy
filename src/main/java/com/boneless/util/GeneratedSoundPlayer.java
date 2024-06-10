package com.boneless.util;

import javax.sound.sampled.*;

public class GeneratedSoundPlayer {

    public static void main(String[] args) {
        new GeneratedSoundPlayer().play();
    }

    public void play() {
        // Audio format parameters
        float sampleRate = 44100;
        int sampleSizeInBits = 16;
        int channels = 1;
        boolean signed = true;
        boolean bigEndian = false;

        AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);

        // Create a SourceDataLine for playback
        try (SourceDataLine line = AudioSystem.getSourceDataLine(format)) {
            line.open(format);
            line.start();

            // Generate sound data (sine wave)
            byte[] buffer = generateSineWaveBuffer(440, 2, sampleRate);

            // Write the data to the line
            line.write(buffer, 0, buffer.length);

            // Finish playback
            line.drain();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    /**
     * Generate a sine wave buffer.
     *
     * @param frequency  the frequency of the sine wave in Hz
     * @param duration   the duration of the sound in seconds
     * @param sampleRate the sample rate in samples per second
     * @return a byte array containing the generated sound data
     */
    private byte[] generateSineWaveBuffer(double frequency, int duration, float sampleRate) {
        int numSamples = (int) (duration * sampleRate);
        byte[] buffer = new byte[2 * numSamples];

        double angle = 2.0 * Math.PI * frequency / sampleRate;

        for (int i = 0; i < numSamples; i++) {
            short sample = (short) (Math.sin(i * angle) * Short.MAX_VALUE);
            buffer[2 * i] = (byte) (sample & 0xff);
            buffer[2 * i + 1] = (byte) ((sample >> 8) & 0xff);
        }

        return buffer;
    }
}
