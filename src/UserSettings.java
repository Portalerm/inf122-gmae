public class UserSettings {
	private Realm currentRealm;
	private Theme theme;
	private TimeDisplayPreference timeDisplayPreference;

	public UserSettings(Realm currentRealm, Theme theme, TimeDisplayPreference timeDisplayPreference) {
		this.currentRealm = currentRealm;
		this.theme = theme;
		this.timeDisplayPreference = timeDisplayPreference;
	}

	public UserSettings() {
		this(null, Theme.DARK, TimeDisplayPreference.BOTH);
	}

	public Realm getCurrentRealm() {
		return currentRealm;
	}

	public void setCurrentRealm(Realm currentRealm) {
		this.currentRealm = currentRealm;
	}

	public Theme getTheme() {
		return theme;
	}

	public void setTheme(Theme theme) {
		this.theme = theme;
	}

	public TimeDisplayPreference getTimeDisplayPreference() {
		return timeDisplayPreference;
	}

	public void setTimeDisplayPreference(TimeDisplayPreference timeDisplayPreference) {
		this.timeDisplayPreference = timeDisplayPreference;
	}
}
