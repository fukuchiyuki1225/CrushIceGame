import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;

public class ImageLoader {
	private static ImageLoader il = new ImageLoader();

	private ImageLoader() {
	};

	public static ImageLoader getInstance() {
		return il;
	}

	public BufferedImage load(String path) {
		BufferedImage bi = null;
		try {
			URL url = il.getClass().getClassLoader().getResource(path);
			bi = ImageIO.read(url);
		} catch (Exception e) {
			e.getStackTrace();
		}
		return bi;
	}
}
