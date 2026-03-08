public abstract class Identifiable {
	private int uuid;
	private static int generation = 0;

	public Identifiable() {
		uuid = generation++;
	}

	public int getUuid() {
		return uuid;
	}
}
