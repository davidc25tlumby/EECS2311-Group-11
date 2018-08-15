package authoringApp;

import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

public class AudioPlayback {
	
	File f;
    AudioInputStream stream;
    AudioFormat format;
    DataLine.Info info;
    Clip clip;

	AudioPlayback(File sound) {
		// TODO Auto-generated constructor stub
		this.f = sound;
	}

	public void play(){
		try{
		    stream = AudioSystem.getAudioInputStream(f);
		    format = stream.getFormat();
		    info = new DataLine.Info(Clip.class, format);
		    clip = (Clip) AudioSystem.getLine(info);
		    clip.open(stream);
		    clip.start();
		}catch (Exception e){
			
		}
	}
}
