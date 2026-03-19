public class LocalTime {
	private int days;
	private int hours;
	private int minutes;

	public LocalTime(int days, int hours, int minutes) {
		this.days = days;
		this.hours = hours;
		this.minutes = minutes;
	}

	public LocalTime() {
		this(0, 0, 0);
	}

	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
	}

	public int getHours() {
		return hours;
	}

	public void setHours(int hours) {
		this.hours = hours;
	}

	public int getMinutes() {
		return minutes;
	}

	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}
}