import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingUtilities;

public class ItemManager implements MouseListener, MouseMotionListener {
	private GameScreen gs;
	private Map<String, Item> items;
	private List<JButton> itemButtons;
	private int count;

	public ItemManager() {
		gs = GameScreen.getInstance();
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

		String[] itemName;
		if (random.nextInt(2) == 0) {
			itemName = new String[] {
					"goldenHammer1",
					"goldenHammer2",
					"shield1",
					"warp1",
					"warp2",
					"ghost1"
			};
		} else {
			itemName = new String[] {
					"goldenHammer1",
					"shield1",
					"shield2",
					"warp1",
					"ghost1",
					"ghost2"
			};
		}

		for (int i = 0; i < itemName.length; i++) {
			MesgSend.send("initItems" + " " + itemName[i] + " " + rand[i]);
			System.out.println(itemName[i] + " : " + rand[i]);
		}
	}

	public void initItems(String name, int location) {
		if (name.matches("goldenHammer.*")) {
			items.put(name, new GoldenHammer(location));
		} else if (name.matches("shield.*")) {
			items.put(name, new Shield(location));
		} else if (name.matches("warp.*")) {
			items.put(name, new Warp(location));
		} else if (name.matches("ghost.*")) {
			items.put(name, new Ghost(location));
		}
		items.get(name).initItem();
	}


	public void digOutItem(int JbNum) {
		for (String key : items.keySet()) {
			if (items.get(key).getLocation() == JbNum) {
				MesgSend.send("getItem" + " " + key);
				setItemToList(key, false);
				Sound.play("item");
				break;
			}
		}
	}

	public void setItemToList(String itemName, boolean stolenFlag) {
		if (!gs.isMyTurn()) return;
		count++;
		JButton jb = new JButton(new ImageIcon(ResourceLoader.load("img/" + itemName.split("[0-9]")[0] + ".png")));
		jb.setActionCommand(itemName);
		gs.setButton(jb, this, this);
		if (count <= 3) {
			gs.addComponent(jb, 500, 775 + count * 80, 675, 100, 100);
		} else {
			gs.addComponent(jb, 500, 775 + (count - 3) * 80, 750, 100, 100);
		}
		if (!stolenFlag) jb.setVisible(false);
		else jb.setVisible(true);
		itemButtons.add(jb);
	}

	public void realignItemButtons() {
		int i = 1;
		for (JButton jb : itemButtons) {
			if (i <= 3) {
				jb.setLocation(775 + i * 80, 675);
			} else {
				jb.setLocation(775 + (i - 3) * 80, 750);
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

	public void stolenItems() {
		if (gs.isMyTurn()) return;
		Iterator<JButton> it = itemButtons.iterator();
		while(it.hasNext()) {
			JButton jb = it.next();
			MesgSend.send("stolenItem" + " " + jb.getActionCommand());
			jb.setVisible(false);
			it.remove();
			count--;
		}
		realignItemButtons();
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
		MesgSend.send("useItem");
		items.get(itemName).use();
		count--;
		realignItemButtons();
	}

	public void mousePressed(MouseEvent e) {
		Hammer.getInstance().changeHammerIcon();
	}

	public void mouseReleased(MouseEvent e) {
		Hammer.getInstance().changeHammerIcon();
	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}
}
