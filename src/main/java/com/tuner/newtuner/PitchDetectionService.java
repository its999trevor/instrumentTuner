package com.tuner.newtuner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import be.tarsos.dsp.io.jvm.JVMAudioInputStream;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;


import javax.sound.sampled.*;
import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.pitch.PitchProcessor.PitchEstimationAlgorithm;

@Service
public class PitchDetectionService {
    private Thread pitchDetectionThread; 
    private TargetDataLine line; // Store the audio input line.
    private volatile boolean pitchDetectionRunning = false;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void startPitchDetection() {
        if (!isPitchDetectionRunning()) {
            pitchDetectionRunning = true;
            try {
                int sampleRate = 44100;
                int bufferSize = 1024;

                // Set up audio input from the microphone.
                AudioFormat format = new AudioFormat(44100, 16, 1, true, false);
                DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
                line = (TargetDataLine) AudioSystem.getLine(info);
                line.open(format);
                line.start();

                AudioInputStream audioInputStream = new AudioInputStream(line);
                JVMAudioInputStream audioStream = new JVMAudioInputStream(audioInputStream);

                // Create an AudioDispatcher.
                AudioDispatcher dispatcher = new AudioDispatcher(audioStream, bufferSize, 0);

                // Define the pitch detection handler.
                dispatcher.addAudioProcessor(new PitchProcessor(PitchEstimationAlgorithm.YIN, sampleRate, bufferSize, createPitchHandler()));

                // Start processing audio from the microphone.
                pitchDetectionThread = new Thread(dispatcher);
                pitchDetectionThread.start();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
       
        return;
    }

    public void stopPitchDetection() {
        if (isPitchDetectionRunning()) {
            pitchDetectionRunning = false;
    
            if (pitchDetectionThread != null) {
                pitchDetectionThread.interrupt();
            }
            if (line != null) {
                line.stop();
                line.close();
            }
    
            // Additional cleanup (close audio input, release resources, etc.) here.
    
            System.out.println("stop");
    
            messagingTemplate.convertAndSend("/topic/pitch", "Stop!");
        }
    }

    public boolean isPitchDetectionRunning() {
        return pitchDetectionRunning;
    }
    

    private void sendPitchToClient(String note) {
        // Send the note to a specific topic ("/topic/note")
        messagingTemplate.convertAndSend("/topic/note", note);
    }
    


    
    

    private PitchDetectionHandler createPitchHandler() {
        return new PitchDetectionHandler() {
            @Override
            public void handlePitch(PitchDetectionResult result, AudioEvent audioEvent) {
                if (Thread.currentThread().isInterrupted()) {
                    return;
                }
                float pitchInHz = result.getPitch();
                if (pitchInHz >= 110 && pitchInHz < 196) {
                String note = getNoteFromPitch(pitchInHz);
                System.out.println("Pitch: " + pitchInHz + " Hz - Note: " + note);
                sendPitchToClient(note);    
                }

            }
        };
    }

    public String getNoteFromPitch(float pitchInHz) {
        // Define the note ranges and corresponding notes.
        if (pitchInHz >= 110 && pitchInHz < 123.47) {
            return "A";
        } else if (pitchInHz >= 123.47 && pitchInHz < 130.81) {
            return "B";
        } else if (pitchInHz >= 130.81 && pitchInHz < 146.83) {
            return "C";
        } else if (pitchInHz >= 146.83 && pitchInHz < 164.81) {
            return "D";
        } else if (pitchInHz >= 164.81 && pitchInHz <= 174.61) {
            return "E";
        } else if (pitchInHz >= 174.61 && pitchInHz < 185) {
            return "F";
        } else if (pitchInHz >= 185 && pitchInHz < 196) {
            return "G";
        } else {
            return "Unknown";
        }
    }
}
