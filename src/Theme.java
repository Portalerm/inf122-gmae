public enum Theme {
	LIGHT,
	DARK;

	public String getDisplayName() {
		switch (this) {
		case LIGHT: return "Light";
		case DARK: return "Dark";
		default: return name();
		}
	}
}