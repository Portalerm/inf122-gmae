import java.util.ArrayList;
import java.util.List;

public class QuestEvent extends Identifiable {
	private String name;
	private Time startTime;
	private Time endTime;
	private Realm realm;
	private Inventory loot;
	private List<QuestEventShare> shared;

	public QuestEvent(String name, Time startTime, Time endTime, Realm realm, Inventory loot) {
		this.name = name;
		this.startTime = startTime;
		this.endTime = endTime;
		this.realm = realm;
		this.loot = loot;
		this.shared = new ArrayList<>();
	}

	public QuestEvent(String name, Time startTime, Time endTime, Realm realm) {
		this(name, startTime, endTime, realm, new Inventory());
	}

	public QuestEvent(String name, Time startTime, Realm realm) {
		this(name, startTime, null, realm, new Inventory());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Time getStartTime() {
		return startTime;
	}

	public void setStartTime(Time startTime) {
		this.startTime = startTime;
	}

	public Time getEndTime() {
		return endTime;
	}

	public void setEndTime(Time endTime) {
		this.endTime = endTime;
	}

	public Realm getRealm() {
		return realm;
	}

	public void setRealm(Realm realm) {
		this.realm = realm;
	}

	public Inventory getLoot() {
		return loot;
	}

	public void setLoot(Inventory loot) {
		this.loot = new Inventory(loot.getItems());
	}

	public void applyLoot(Inventory inventory) {
		for (Item item : loot.getItems()) {
			inventory.addItem(item);
		}
	}

	public List<QuestEventShare> getShared() {
		return shared;
	}

	public void setShared(List<QuestEventShare> shared) {
		this.shared = new ArrayList<>(shared);
	}

	public void share(User sharedWith, User sender, PermissionLevel permissionLevel) {
		shared.add(new QuestEventShare(this, sharedWith, sender, permissionLevel));
	}
}
