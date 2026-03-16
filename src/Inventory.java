import java.util.ArrayList;
import java.util.List;

public class Inventory {
	private List<Item> items;

	public Inventory(List<Item> items) {
		this.items = new ArrayList<>();
		for (Item item : items) {
			this.items.add(new Item(item));
		}
	}

	public Inventory() {
		this(new ArrayList<>());
	}

	public boolean isEmpty() {
		return items.isEmpty();
	}

	public List<Item> getItems() {
		return new ArrayList<>(items);
	}

	public void addItem(Item item) {
		items.add(item);
	}

	public Item findItem(int itemId) {
		for (int i = 0; i < items.size(); ++i) {
			Item item = items.get(i);
			if (item.getUuid() == itemId) {
				return item;
			}
		}
		return null;
	}

    public Item findItemByIndex(int index) {
        return findItem(items.get(index).getUuid());
    }

	public void removeItem(int itemId) {
		Item item = findItem(itemId);
		if (item != null) {
			items.remove(item);
		}
	}

    public void removeItemByIndex(int index) {
        removeItem(items.get(index).getUuid());
    }

	public void updateItem(int itemId, ItemInfo itemInfo) {
		Item item = findItem(itemId);
		if (item != null) {
			item.setInfo(itemInfo);
		}
	}

	@Override
	public String toString() {
		if (items.isEmpty()) {
			return "Inventory: (empty)";
		}
		StringBuilder sb = new StringBuilder();
		sb.append("Inventory:\n");
        int counter = 1;
		for (Item item : items) {
			sb.append("  ").append(counter).append(" ").append(item.getInfo().getName()).append("\n");
            counter++;
		}
		return sb.toString();
	}
}
