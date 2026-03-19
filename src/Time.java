public class Time implements Comparable<Time> {
	private int day, hour, minute;

	public Time(int day, int hour, int minute) {
		this.day = day;
		this.hour = hour;
		this.minute = minute;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	@Override
	public int compareTo(Time o) {
		if (day != o.day) {
			return day - o.day;
		}
		if (hour != o.hour) {
			return hour - o.hour;
		}
		return minute - o.minute;
	}
}