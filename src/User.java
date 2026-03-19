import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class User extends Identifiable {
	private String name;
	private List<Campaign> ownedCampaigns;
	private List<CampaignShare> sharedCampaigns;
	private List<Character> characters;
	private List<QuestEventShare> sharedQuestEvents;
	private UserSettings settings;

	public User(String name) {
		this.name = name;
		this.ownedCampaigns = new ArrayList<>();
		this.sharedCampaigns = new ArrayList<>();
		this.characters = new ArrayList<>();
		this.sharedQuestEvents = new ArrayList<>();
		this.settings = new UserSettings();
	}

	public User() {
		this("");
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addCampaign(Campaign campaign) {
		ownedCampaigns.add(campaign);
	}

	public void removeCampaign(Campaign campaign) {
		ownedCampaigns.removeIf(c -> c.getUuid() == campaign.getUuid());
	}

	public List<Campaign> getOwnedCampaigns() {
		return Collections.unmodifiableList(ownedCampaigns);
	}

	public List<CampaignShare> getSharedCampaigns() {
		return Collections.unmodifiableList(sharedCampaigns);
	}

	public void addSharedCampaign(CampaignShare share) {
		sharedCampaigns.add(share);
	}

	public List<Campaign> allParticipatingCampaigns() {
		List<Campaign> result = new ArrayList<>(ownedCampaigns);
		for (CampaignShare cs : sharedCampaigns) {
			result.add(cs.getCampaign());
		}
		return result;
	}

	public void addCharacter(Character character) {
		characters.add(character);
	}

	public void removeCharacter(Character character) {
		characters.removeIf(c -> c.getUuid() == character.getUuid());
	}

	public List<Character> getCharacters() {
		return Collections.unmodifiableList(characters);
	}

	public List<QuestEventShare> getSharedQuestEvents() {
		return Collections.unmodifiableList(sharedQuestEvents);
	}

	public void addSharedQuestEvent(QuestEventShare share) {
		sharedQuestEvents.add(share);
	}

	public UserSettings getSettings() {
		return settings;
	}

	public void setSettings(UserSettings settings) {
		this.settings = settings;
	}
}