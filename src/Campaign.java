import java.util.ArrayList;
import java.util.List;

public class Campaign extends Identifiable {
	private String name;
	private CampaignVisibility visibility;
	private boolean archived;
	private List<QuestEvent> questEvents;
	private List<CampaignShare> shared;

	public Campaign(String name, CampaignVisibility visibility) {
		this.name = name;
		this.visibility = visibility;
		this.archived = false;
		this.questEvents = new ArrayList<>();
		this.shared = new ArrayList<>();
	}

	public Campaign(String name) {
		this(name, CampaignVisibility.PRIVATE);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void rename(String newName) {
		this.name = newName;
	}

	public CampaignVisibility getVisibility() {
		return visibility;
	}

	public void setVisibility(CampaignVisibility visibility) {
		this.visibility = visibility;
	}

	public boolean isArchived() {
		return archived;
	}

	public void setArchived(boolean archived) {
		this.archived = archived;
	}

	public void archive() {
		this.archived = true;
	}

	public void unarchive() {
		this.archived = false;
	}

	public List<QuestEvent> getQuestEvents() {
		return questEvents;
	}

	public void setQuestEvents(List<QuestEvent> questEvents) {
		this.questEvents = new ArrayList<>(questEvents);
	}

	public void addQuestEvent(QuestEvent questEvent) {
		questEvents.add(questEvent);
	}

	public void updateQuestEvent(QuestEvent old, QuestEvent replace) {
		old.setName(replace.getName());
		old.setStartTime(replace.getStartTime());
		old.setEndTime(replace.getEndTime());
		old.setRealm(replace.getRealm());
		old.setLoot(replace.getLoot());
	}

	public void removeQuestEvent(QuestEvent questEvent) {
		questEvents.removeIf(q -> q.getUuid() == questEvent.getUuid());
	}

	public List<QuestEvent> getDayView(Time time) {
		return questEvents.stream().filter(q -> q.getStartTime().getDay() == time.getDay()).toList();
	}

	public List<QuestEvent> getWeekView(Time time) {
		return questEvents.stream().filter(q -> q.getStartTime().getDay() / 7 == time.getDay() / 7).toList();
	}

	public List<QuestEvent> getMonthView(Time time) {
		return questEvents.stream().filter(q -> q.getStartTime().getDay() / 30 == time.getDay() / 30).toList();
	}

	public List<QuestEvent> getYearView(Time time) {
		return questEvents.stream().filter(q -> q.getStartTime().getDay() / 360 == time.getDay() / 360).toList();
	}

	public List<CampaignShare> getShared() {
		return shared;
	}

	public void share(User sharedWith, User sender, PermissionLevel permissionLevel) {
		shared.add(new CampaignShare(this, sharedWith, sender, permissionLevel));
	}
}
