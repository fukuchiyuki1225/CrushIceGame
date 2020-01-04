import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;

public class ImageLoader {
	static ImageLoader il = new ImageLoader();

	public static BufferedImage readImage(String path) {
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
