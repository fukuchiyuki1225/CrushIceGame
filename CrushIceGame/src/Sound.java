import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound {
	private static Sound sound = new Sound();
	private Map<String, Clip> sounds;

	private Sound() {
		sounds = new HashMap<String, Clip>();
		String[] names = new String[] {
				"bgm",
				"button",
				"pick",
				"crack",
				"broken",
				"fall",
				"win",
				"lose"
		};
		for (String name : names) {
			try {
				AudioInputStream ais = AudioSystem.getAudioInputStream(ResourceLoader.getInstance().getURL("sound/" + name + ".wav"));
				AudioFormat af = ais.getFormat();
				DataLine.Info info = new DataLine.Info(Clip.class, af);
				Clip clip = (Clip)AudioSystem.getLine(info);
				clip.open(ais);
				sounds.put(name, clip);
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			} catch (UnsupportedAudioFileException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static Sound getInstance() {
		return sound;
	}

	public void play(String name) {
		Clip clip = sounds.get(name);
		clip.setFramePosition(0);
		clip.start();
	}

	public void loop(String name) {
		sounds.get(name).loop(1);
	}

	public void stop(String name) {
		sounds.get(name).stop();
	}
}
