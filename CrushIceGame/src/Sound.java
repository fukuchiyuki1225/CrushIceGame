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
	private static Map<String, Clip> sounds;

	private Sound() {
		sounds = new HashMap<String, Clip>();
		String[] names = new String[] {
				"bgm",
				"button",
				"start",
				"turn",
				"pick",
				"crack",
				"broken",
				"item",
				"fall",
				"win",
				"lose"
		};
		for (String name : names) {
			try {
				AudioInputStream ais = AudioSystem.getAudioInputStream(ResourceLoader.getURL("sound/" + name + ".wav"));
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

	public static void play(String name) {
		if (!GameScreen.getInstance().getSeFlag()) return;
		Clip clip = sounds.get(name);
		clip.setFramePosition(0);
		clip.start();
	}

	public static void loop(String name) {
		if (GameScreen.getInstance() != null) {
			if (!GameScreen.getInstance().getBgmFlag()) return;
		}
		Clip clip = sounds.get(name);
		clip.setFramePosition(0);
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}

	public static void stop(String name) {
		Clip clip = sounds.get(name);
		clip.stop();
		clip.setFramePosition(0);
	}

	public static void stop() {
		for (Clip sound : sounds.values()) {
			sound.stop();
		}
	}
}
