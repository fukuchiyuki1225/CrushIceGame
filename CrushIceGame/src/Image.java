import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

public class Image {
	private static Image ii = new Image();
	private static Map<String, ImageIcon> imageIcons;

	public Image() {
		imageIcons = new HashMap<String, ImageIcon>();
		String[] names = new String[] {
				"start",
				"help",
				"again",
				"to_title",
				"bgm_off",
				"bgm_on",
				"se_off",
				"se_on",
				"cancel",
				"decide",

				"start_2",
				"help_2",
				"again_2",
				"to_title_2",
				"bgm_off_2",
				"bgm_on_2",
				"se_off_2",
				"se_on_2",
				"cancel_2",
				"decide_2",

				"win_msg",
				"win_msg_2",

				"lose_msg",
				"lose_msg_2",

				"your_turn",
				"my_turn",

				"win",
				"lose",

				"wait",
				"disconnect",
				"server_error",
				"no_vacancy",

				"title",
				"help_dialog",
				"help_close",
				"input_ip",

				"sea",
				"logo",
				"item_ice",
				"game_start"
		};

		for (String name: names) {
			try {
				imageIcons.put(name, new ImageIcon(ResourceLoader.load("img/" + name + "png")));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static Image getInstance() {
		return ii;
	}

	public static ImageIcon getImageIcon(String name) {
		return imageIcons.get(name);
	}
}
