import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;

public class ResourceLoader {
	private static ResourceLoader rl = new ResourceLoader();

	private ResourceLoader() {
	};

	public static ResourceLoader getInstance() {
		return rl;
	}

	public BufferedImage load(String path) {
		BufferedImage bi = null;
		try {
			bi = ImageIO.read(getURL(path));
		} catch (Exception e) {
			e.getStackTrace();
		}
		return bi;
	}

	public URL getURL(String path) {
		return rl.getClass().getClassLoader().getResource(path);
	}
}
