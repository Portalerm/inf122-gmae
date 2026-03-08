import java.util.ArrayList;
import java.util.List;

public class Character extends Identifiable {
	private String name;
	private String classType;
	private int level;
	private Inventory inventory;
	private List<EventParticipation> participatingEvents;

	public Character(String name, String classType) {
		this.name = name;
		this.classType = classType;
		this.level = 0;
		this.inventory = new Inventory();
		this.participatingEvents = new ArrayList<>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClassType() {
		return classType;
	}

	public void setClassType(String classType) {
		this.classType = classType;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}

	public List<EventParticipation> getParticipatingEvents() {
		return participatingEvents;
	}

	public void addParticipation(EventParticipation participation) {
		participatingEvents.add(participation);
	}
}
