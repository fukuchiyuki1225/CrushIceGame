import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ItemManager {
	Map<String, Item> items;

	public ItemManager() {
		this.items = new HashMap<String, Item>();
		initialize();
	}

	public void initialize() {
		Random random = new Random();
		int[] rand = new int[6];
		for (int i = 0; i < 6; i++) {
			rand[i] = random.nextInt(63);
			System.out.println("itemlocationF " + rand[i]);
		}

		for (int i = 0; i < 5; i++) {
			for (int j = 1; j < 6; j++) {
				if (i != j && rand[i] == rand[j]) {
					initialize();
					return;
				}
			}
		}

		items.put("goldenHammer1", new GoldenHammer(rand[0]));
		items.put("goldenHammer2", new GoldenHammer(rand[1]));
		items.put("shield1", new Shield(rand[2]));
		items.put("shield2", new Shield(rand[3]));
		items.put("warp1", new Warp(rand[4]));
		items.put("warp2", new Warp(rand[5]));
	}
}
