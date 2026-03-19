public interface LocalTimeRule {
	public LocalTime toLocal(Time worldTime);
	public Time toWorld(LocalTime local);
	public String describe();
}