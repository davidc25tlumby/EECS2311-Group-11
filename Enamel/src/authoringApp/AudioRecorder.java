package authoringApp;


import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import org.apache.commons.io.FileUtils;

public class AudioRecorder {
	AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);
	DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
	File audioFile;
	TargetDataLine targetLine = null;
	AudioRecorder(){
		try {
			targetLine = (TargetDataLine) AudioSystem.getLine(info);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}
	
	public void startRecording(){
		try{
			targetLine.open();
			
			System.out.println("Start recording");
			targetLine.start();
			
			Thread thread = new Thread(){
				@Override public void run(){
					AudioInputStream audioStream = new AudioInputStream(targetLine);
					audioFile = new File("TEMP_AUDIO_FILE.wav");
					try {
						AudioSystem.write(audioStream, AudioFileFormat.Type.WAVE, audioFile);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			};
			thread.start();
		}catch(Exception e){
			
		}
	}
	
	public void stopRecording(){
		targetLine.stop();
		targetLine.close();
	}
	
	public void writeSoundFile(File f){
		try {
			FileUtils.copyFile(audioFile, f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
