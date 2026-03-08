public enum CampaignVisibility {
	PRIVATE,
	PUBLIC;

	public String getDisplayName() {
		switch (this) {
		case PRIVATE: return "Private";
		case PUBLIC: return "Public";
		default: return name();
		}
	}
}
