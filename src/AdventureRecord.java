public class AdventureRecord {

    private String adventureName;
    private boolean victory;
    private String realmName;
    private Time worldClockTime;

    public AdventureRecord(String adventureName, boolean victory, String realmName, Time worldClockTime) {
        this.adventureName = adventureName;
        this.victory = victory;
        this.realmName = realmName;
        this.worldClockTime = worldClockTime;
    }


    public String serialize() {
    	// Imma just use "|" as a delimiter 
        return adventureName + "|" + victory + "|" + realmName + "|" + worldClockTime.getDay() + "," + worldClockTime.getHour() + "," + worldClockTime.getMinute();
    }

    public static AdventureRecord deserialize(String line) {
        String[] parts = line.split("\\|");
        String adventureName = parts[0];
        boolean victory = Boolean.parseBoolean(parts[1]);
        String realmName = parts[2];
        String[] timeParts = parts[3].split(",");
        Time time = new Time(Integer.parseInt(timeParts[0]),
                             Integer.parseInt(timeParts[1]),
                             Integer.parseInt(timeParts[2]));
        return new AdventureRecord(adventureName, victory, realmName, time);
    }


    public String toDisplayString() {
        String outcome = victory ? "[Victory]" : "[Defeat] ";
        return outcome + " " + adventureName + " in " + realmName
                + " (Day " + worldClockTime.getDay()
                + ", " + String.format("%02d:%02d", worldClockTime.getHour(), worldClockTime.getMinute()) + ")";
    }

    public String getAdventureName() { return adventureName; }
    public boolean isVictory() { return victory; }
    public String getRealmName() { return realmName; }
    public Time getWorldClockTime() { return worldClockTime; }
}