public class WorldClock {
	private static WorldClock instance;
	private Time time;

	private WorldClock() {
		this.time = new Time(0, 0, 0);
	}

	public static WorldClock getInstance() {
		if (instance == null) {
			instance = new WorldClock();
		}
		return instance;
	}

	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}
}
