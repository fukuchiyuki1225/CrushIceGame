import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ItemManager {
	Map<String, Item> items;

	public ItemManager() {
		this.items = new HashMap<String, Item>();
	}

	public void initialize(GameScreen gs) {
		Random random = new Random();
		int[] rand = new int[6];
		for (int i = 0; i < 6; i++) {
			rand[i] = random.nextInt(63);
			System.out.println("itemlocationF " + rand[i]);
		}

		for (int i = 0; i < 5; i++) {
			for (int j = 1; j < 6; j++) {
				if (i != j && rand[i] == rand[j]) {
					initialize(gs);
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
}
