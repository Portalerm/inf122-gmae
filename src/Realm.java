public class Realm extends Identifiable {
	private String name;
	private String description;
	private MapIdentity mapIdentity;
	private LocalTimeRule timeRule;

	public Realm(String name, String description, LocalTimeRule timeRule) {
		this.name = name;
		this.description = description;
		this.mapIdentity = new MapIdentity();
		this.timeRule = timeRule;
	}

	public Realm(String name, LocalTimeRule timeRule) {
		this(name, null, timeRule);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public MapIdentity getMapIdentity() {
		return mapIdentity;
	}

	public LocalTimeRule getTimeRule() {
		return timeRule;
	}

	public void setTimeRule(LocalTimeRule timeRule) {
		this.timeRule = timeRule;
	}
}
