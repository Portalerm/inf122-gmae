public enum TimeDisplayPreference {
	WORLD,
	REALM,
	BOTH;

	public String getDisplayName() {
		switch (this) {
		case WORLD: return "World";
		case REALM: return "Realm";
		case BOTH: return "Both";
		default: return name();
		}
	}
}
