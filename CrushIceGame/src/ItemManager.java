import java.awt.Point;
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
import javax.swing.SwingUtilities;

public class ItemManager implements MouseListener, MouseMotionListener {
	private GameScreen gs;
	private MesgSend ms;
	private Sound sound;
	private Map<String, Item> items;
	private List<JButton> itemButtons;
	private int count;

	public ItemManager() {
		gs = GameScreen.getInstance();
		ms = MesgSend.getInstance();
		sound = Sound.getInstance();
		items = new HashMap<String, Item>();
		itemButtons = new ArrayList<JButton>();
		count = 0;
		initLocation();
	}

	public void initLocation() {
		if (!gs.isMyTurn()) return;
		Random random = new Random();
		int[] rand = new int[6];
		for (int i = 0; i < 6; i++) {
			rand[i] = random.nextInt(63);
		}

		for (int i = 0; i < 5; i++) {
			for (int j = 1; j < 6; j++) {
				if (i != j && rand[i] == rand[j]) {
					initLocation();
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
			ms.send("initItems" + " " + itemName[i] + " " + rand[i]);
		}
	}

	public void initItems(String name, int location) {
		if (name.matches("goldenHammer.*")) {
			items.put(name, new GoldenHammer(location));
		} else if (name.matches("shield.*")) {
			items.put(name, new Shield(location));
		} else if (name.matches("warp.*")) {
			items.put(name, new Warp(location));
		}
		items.get(name).initItem();
	}


	public void digOutItem(int JbNum) {
		for (String key : items.keySet()) {
			if (items.get(key).getLocation() == JbNum) {
				ms.send("getItem" + " " + key);
				count++;
				JButton jb = new JButton(new ImageIcon(ResourceLoader.getInstance().load("img/" + key.split("[0-9]")[0] + ".png")));
				jb.setActionCommand(key);
				gs.setButton(jb, this, this);
				if (count <= 3) {
					gs.addComponent(jb, 500, 775 + count * 80, 675, 100, 100);
				} else {
					gs.addComponent(jb, 500, 775 + (count - 3) * 80, 750, 100, 100);
				}
				jb.setVisible(false);
				itemButtons.add(jb);
				sound.play("item");
				break;
			}
		}
	}

	public void realignItemButtons() {
		int i = 1;
		for (JButton jb : itemButtons) {
			if (i <= 3) {
				jb.setLocation(775 + i * 80, 675);
			} else {
				jb.setLocation(775 + (i - 3) * 80, 775);
			}
			i++;
		}
	}

	public void setItemInvisible() {
		for (Item item : items.values()) {
			item.getItemLabel().setVisible(false);
		}
	}

	public void setItemButtons() {
		for (JButton jb : itemButtons) {
			jb.setVisible(true);
		}
	}

	public Map<String, Item> getItems() {
		return items;
	}

	public void mouseDragged(MouseEvent e) {
	}

	public void mouseMoved(MouseEvent e) {
		Point p = e.getPoint();
		SwingUtilities.convertPointToScreen(p, e.getComponent());
		SwingUtilities.convertPointFromScreen(p, e.getComponent().getParent());
		Hammer.getInstance().setHammerLocation(p);
	}

	public void mouseClicked(MouseEvent e) {
		if (!gs.isMyTurn() || gs.getIces().getMoveFlag()) return;
		JButton jb = (JButton) e.getComponent();
		String itemName = jb.getActionCommand();
		jb.setVisible(false);
		itemButtons.remove(jb);
		ms.send("useItem");
		items.get(itemName).use();
		realignItemButtons();
		count--;
	}

	public void mousePressed(MouseEvent e) {

	}

	public void mouseReleased(MouseEvent e) {

	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}
}
