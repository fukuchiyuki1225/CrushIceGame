import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class ItemManager implements MouseListener, MouseMotionListener {
	private Map<String, Item> items;
	private GameScreen gs;
	private List<JButton> itemButtons;
	private int count;

	public ItemManager(GameScreen gs) {
		items = new HashMap<String, Item>();
		this.gs = gs;
		itemButtons = new ArrayList<JButton>();
		count = 0;
	}

	public void initialize() {
		Random random = new Random();
		int[] rand = new int[6];
		for (int i = 0; i < 6; i++) {
			rand[i] = random.nextInt(63);
			System.out.println("itemlocation： " + rand[i]);
		}

		for (int i = 0; i < 5; i++) {
			for (int j = 1; j < 6; j++) {
				if (i != j && rand[i] == rand[j]) {
					initialize();
					return;
				}
			}
		}

		String[] itemName = new String[] {
				"goldenHammer1",
				"goldenHammer2",
				"shield1",
				"shield2",
				"warp1",
				"warp2"
		};

		for (int i = 0; i < itemName.length; i++) {
			gs.send("itemInit" + " " + itemName[i] + " " + rand[i]);
		}
	}

	public void putItems(String name, int location) {
		if (name.matches("goldenHammer.*")) {
			items.put(name, new GoldenHammer(location));
		} else if (name.matches("shield.*")) {
			items.put(name, new Shield(location));
		} else if (name.matches("warp.*")) {
			items.put(name, new Warp(location));
		}
	}

	public Map<String, Item> getItems() {
		return items;
	}

	public void digOutItem(int JbNum) {
		for (String key : items.keySet()) {
			if (items.get(key).getLocation() == JbNum) {
				gs.send("digOutItem" + " " + key);
				count++;
				JButton jb = new JButton(new ImageIcon(ImageLoader.loadImage("img/" + key.split("[0-9]")[0] + ".png")));
				System.out.println("img/" + key.split("[0-9]")[0] + ".png");
				gs.setButton(jb, this, this);
				gs.addComponent(jb, 500, 700 + count * 100, 700, 100, 100);
				itemButtons.add(jb);
				break;
			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}
}
