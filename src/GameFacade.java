import java.util.List;

public class GameFacade {
	private User user;
	private WorldClock worldClock;
	private CampaignService campaignService;
	private QuestEventService questEventService;
	private PlayerProfileService profileService;

	public GameFacade(User user) {
		this.user = user;
		this.worldClock = WorldClock.getInstance();
		this.campaignService = new CampaignService();
		this.questEventService = new QuestEventService();
		this.profileService = new PlayerProfileService();
	}

	public GameFacade() {
		this(new User());
	}

	public PlayerProfile findProfile(String name) {
		return profileService.findByName(name);
	}

	public PlayerProfile createProfile(String name) {
		return profileService.createProfile(name);
	}

	public List<String> getAllProfileNames() {
		return profileService.getAllProfileNames();
	}

	public void recordAdventure(PlayerProfile profile, AdventureRecord record) {
		profileService.recordAdventure(profile, record);
	}

	public void createCharacter(String name, char classChoice) {
		Character character = CharacterFactory.createCharacter(name, classChoice);
		user.addCharacter(character);
	}

	public Character createCharacterForProfile(PlayerProfile profile, String name, char classChoice) {
		Character character = CharacterFactory.createCharacter(name, classChoice);
		profile.setActiveCharacter(character);
		return character;
	}

	public Campaign createCampaign(String name, CampaignVisibility visibility) {
		return campaignService.createCampaign(user, name, visibility);
	}

	public void renameCampaign(Campaign campaign, String newName) {
		campaignService.renameCampaign(user, campaign, newName);
	}

	public void archiveCampaign(Campaign campaign) {
		campaignService.archiveCampaign(user, campaign);
	}

	public void shareCampaign(Campaign campaign, User recipient, PermissionLevel perm) {
		campaignService.shareCampaign(user, campaign, recipient, perm);
	}

	public QuestEvent addQuestEvent(Campaign campaign, String title, Time start, Time end, Realm realm) {
		return questEventService.addEvent(user, campaign, title, start, end, realm);
	}

	public void shareQuestEvent(QuestEvent event, User recipient, PermissionLevel perm) {
		questEventService.shareEvent(user, event, recipient, perm);
	}

	public void addParticipant(QuestEvent event, Character character) {
		questEventService.addParticipant(user, event, character);
	}

	public List<Character> getCharacters() {
		return user.getCharacters();
	}

	public List<Campaign> getCampaigns() {
		return user.getOwnedCampaigns();
	}

	public Campaign getCampaign(int index) {
		return user.getOwnedCampaigns().get(index);
	}

	public UserSettings getSettings() {
		return user.getSettings();
	}

	public void toggleTheme() {
		UserSettings settings = user.getSettings();
		if (settings.getTheme() == Theme.DARK) {
			settings.setTheme(Theme.LIGHT);
		} else {
			settings.setTheme(Theme.DARK);
		}
	}

	public void setTimeDisplayPreference(TimeDisplayPreference pref) {
		user.getSettings().setTimeDisplayPreference(pref);
	}

	public Time getWorldTime() {
		return worldClock.getTime();
	}

	public User getUser() {
		return user;
	}

	public MiniAdventure launchMiniAdventure(MiniAdventure adventure, Character p1, Character p2, Realm realm) {
		adventure.initialize(p1, p2, realm);
		return adventure;
	}
}