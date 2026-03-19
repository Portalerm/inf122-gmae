public class EventInventoryDelta extends Identifiable {
	private String itemName;
	private int quantityDelta;
	private QuestEvent event;

	public EventInventoryDelta(String itemName, int quantityDelta, QuestEvent event) {
		this.itemName = itemName;
		this.quantityDelta = quantityDelta;
		this.event = event;
	}

	public String getItemName() {
		return itemName;
	}

	public int getQuantityDelta() {
		return quantityDelta;
	}

	public QuestEvent getEvent() {
		return event;
	}
}