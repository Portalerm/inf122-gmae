public class ItemInfo {
	private String name;
	private String description;
	private Rarity rarity;

	public ItemInfo(String name, String description, Rarity rarity) {
		this.name = name;
		this.description = description;
		this.rarity = rarity;
	}

	public ItemInfo(ItemInfo item) {
		this.name = item.name;
		this.description = item.description;
		this.rarity = item.rarity;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Rarity getRarity() {
		return rarity;
	}

	public void setRarity(Rarity rarity) {
		this.rarity = rarity;
	}
}