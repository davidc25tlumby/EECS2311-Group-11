package authoringApp;

import java.io.File;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

/**
 * Plays audio files.
 * 
 * @author Xiahan Chen, Huy Hoang Minh Cu, Qasim Mahir
 */
public class AudioPlayback {

	File f;
	AudioInputStream stream;
	AudioFormat format;
	DataLine.Info info;
	Clip clip;

	/**
	 * Sets the audio file to be played.
	 * 
	 * @param sound
	 *            The audio file.
	 */
	AudioPlayback(File sound) {
		// TODO Auto-generated constructor stub
		this.f = sound;
	}

	/**
	 * Plays the audio file.
	 */
	public void play() {
		try {
			stream = AudioSystem.getAudioInputStream(f);
			format = stream.getFormat();
			info = new DataLine.Info(Clip.class, format);
			clip = (Clip) AudioSystem.getLine(info);
			clip.open(stream);
			clip.start();
		} catch (Exception e) {

		}
	}
}
