public class Item extends Identifiable {
	private ItemInfo info;

	public Item(ItemInfo info) {
		this.info = info;
	}

	public Item(Item item) {
		this.info = new ItemInfo(item.info);
	}

	public ItemInfo getInfo() {
		return info;
	}

	public void setInfo(ItemInfo info) {
		this.info = info;
	}
}
